package com.mck.rp.tetscases.Misc;

import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.MalformedURLException;
import java.net.URL;

public class RemoteWdTest {

    public static void main(String args[]) throws MalformedURLException, InterruptedException {

       System.setProperty("webdriver.chrome.driver", "desktop/automationprojects/seleniumserver/chrome");
       var chromeDriver = new RemoteWebDriver(new URL("http://localhost:4444/"), new ChromeOptions());

       chromeDriver.get("https://staging-regimenprofiler.mckesson.com/login");
       Thread.sleep(3000);

    }
}
