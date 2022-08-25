#-----------------------------------------------------------------------------------------
'''
    DESCRIPTION: Base class - works for different OS and different browsers and config files
    AUTHOR: Sree Karumuri
'''
#-----------------------------------------------------------------------------------------
package com.mck.rp.base;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Platform;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariDriver;
import io.github.bonigarcia.wdm.WebDriverManager;


public class BasePage {

    public static ThreadLocal<RemoteWebDriver> tlDriver = new ThreadLocal<RemoteWebDriver>();
    public RemoteWebDriver driver;
    public Properties prop;
    ChromeOptions co;

    public static synchronized RemoteWebDriver getDriver() {
        return tlDriver.get();
    }

    public void setDriver(RemoteWebDriver driver) {
        this.driver = driver;
    }

    /**
     * This method is used to initialize the driver based on the given browser name
     *
     * @param prop - Properties file
     * @return driver
     */
    public RemoteWebDriver init_driver(Properties prop) {

        String browserName;

        if (System.getProperty("browser") == null) {
            browserName = prop.getProperty("browser").trim();
        } else {
            browserName = System.getProperty("browser").trim();
        }
        System.out.println("Executing on browser : " + browserName);

        if (browserName.equalsIgnoreCase("chrome")) {
            //To run on a specific version of chrome
            //WebDriverManager.chromedriver().browserVersion("86").setup();
            WebDriverManager.chromedriver().setup();
            if (Boolean.parseBoolean(prop.getProperty("remote"))) {
                System.out.println("Running on Remote Hub through Selenium Grid set up");
                init_remoteWebDriver("chrome");
            } else {
                System.out.println("Running thru regular Selenium Set up");
                tlDriver.set(new ChromeDriver(getChromeOptions()));
            }

        } else if (browserName.equalsIgnoreCase("safari")) {
            WebDriverManager.getInstance(SafariDriver.class).setup();
            tlDriver.set(new SafariDriver());
        } else {
            System.out.println(browserName + " is not found, please pass the correct browser....");
        }

        getDriver().manage().deleteAllCookies();
        getDriver().manage().window().maximize();
        getDriver().get(prop.getProperty("url"));

        return getDriver();
    }

    /**
     * Run on remote machine - hub
     *
     *
     */
    public void init_remoteWebDriver(String browserName) {

        if (browserName.equalsIgnoreCase("chrome")) {
            System.out.println("Running thru Selenium Grid Set up");
            System.setProperty("webdriver.chrome.driver", "chromeDriverPathForGrid");
            DesiredCapabilities cap = new DesiredCapabilities();
            cap.setBrowserName(browserName);
            cap.setPlatform(Platform.ANY);
            ChromeOptions opt = getChromeOptions().merge(cap);
            try {
                tlDriver.set(new RemoteWebDriver(new URL(prop.getProperty("huburl")), opt));
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

        }

    }


    public ChromeOptions getChromeOptions() {
        co = new ChromeOptions();

        //To disable the msg "Chrome is being controlled......"
        co.setExperimentalOption("excludeSwitches", Arrays.asList("enable-automation", "load-extension"));
        //To remove save password popup
        Map<String, Object> prefs = new HashMap<>();
        prefs.put("credentials_enable_service", false);
        prefs.put("profile.password_manager_enabled", false);
        //Default download folder for all downloads
        prefs.put("download.default_directory", "./src/test/resources/Downloads");

        co.setExperimentalOption("prefs", prefs);

        /* Uncomment below when/if required
        co.addArguments("--disable-notifications");
        co.addArguments("--disable-web-security");
        co.addArguments("--ignore-certificate-errors");
        co.addArguments("--allow-running-insecure-content");
        co.addArguments("--allow-insecure-localhost");
         */

        if (Boolean.parseBoolean(prop.getProperty("headless"))) {
            co.addArguments("--headless");
            co.addArguments("--disable-gpu");
            co.addArguments("window-size=1920,1080");
        }
        if (Boolean.parseBoolean(prop.getProperty("incognito"))) co.addArguments("--incognito");
        return co;
    }


    /**
     * this method is used to initialize the properties
     * from config.properties file
     *
     * @return prop
     */
    public Properties init_prop() throws IOException {
        prop = new Properties();
        String path = null;
        String env;

        try {
            env = System.getenv("env");
            System.out.println("Running on Environment: " + env);
            if (env == null) {
                path = "./src/test/resources/properties/config.properties";
            } else {
                switch (env.toLowerCase()) {
                    case "qa":
                        path = "./src/test/resources/properties/qaConfig.properties";
                        break;
                    case "staging":
                        path = "./src/test/resources/properties/stagingConfig.properties";
                        break;
                    case "dev":
                        path = "./src/test/resources/properties/devConfig.properties";
                        break;
                    default:
                        System.out.println("Please pass the correct env value....");
                        break;
                }
            }
            assert path != null;
            FileInputStream ip = new FileInputStream(path);
            prop.load(ip);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return prop;
    }


    // take screenshot: For usage in TestListeners class
    public String getScreenshot() {
        File src = ((TakesScreenshot) getDriver()).getScreenshotAs(OutputType.FILE);
        String path = System.getProperty("user.dir") + "/extentReports/screenshots/" + System.currentTimeMillis() + ".png";
        File destination = new File(path);
        try {
            FileUtils.copyFile(src, destination);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return path;
    }

}
