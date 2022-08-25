package com.mck.rp.utilities;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import io.qameta.allure.Step;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.*;

public class ElementUtil {

    private WebDriver driver;

    public ElementUtil(WebDriver driver) {
        this.driver = driver;
    }

    /**
     * this is used to create the webelement on the basis of by locator
     *
     * @param locator
     * @return webelement
     */
    public WebElement getElement(By locator) {
        WebElement element = null;
        try {
            element = driver.findElement(locator);
        } catch (Exception e) {
            System.out.println("element could not be created..." + locator);
        }

        return element;
    }
    /**
     * this is used to create the webelement on the basis of by locator
     *
     * @param locator
     * @return webelement
     */
    public  List<WebElement>  getElements(By locator) {
        List<WebElement>  element = null;
        try {
             element = driver.findElements(locator);
        } catch (Exception e) {
            System.out.println("element could not be created..." + locator);
        }

        return element;
    }

    public String getText(By locator) {
        //waitForElementPresent(locator,8);
        return getElement(locator).getText();
    }

    public String getTextcontent(By locator) {
        waitForElementPresent(locator, 8);
        return getElement(locator).getAttribute("textContent");
    }

    public void doClick(By locator) {
        getElement(locator).click();
    }

    public void doSendKeys(By locator, String value) {
        getElement(locator).sendKeys(value);
    }


    public boolean doIsDisplayed(By locator) {
        return getElement(locator).isDisplayed();
    }

    public boolean isElementPresent(By locator) {
        try {
            return getElement(locator).isDisplayed();
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    //**********************Actions Methods ********************
    public void doActionsClick(By locator) {
        Actions ac = new Actions(driver);
        ac.click(getElement(locator)).perform();
    }

    public void doActionsSendKeys(By locator, String value) {
        Actions ac = new Actions(driver);
        ac.sendKeys(getElement(locator), value).perform();
    }

    // Mouse hover with Actions class
    public void mouseHover(By locator, String element) {
        boolean flag = false;
        try {
            WebElement el = driver.findElement(By.cssSelector(element));
            Actions action = new Actions(driver);
            action.moveToElement(el).build().perform();
            flag = true;
        } catch (NoSuchElementException e) {
            flag = false;
        }
        if (flag) {
            System.out.println("Mouse Hover is working on the element " + element);
        }
    }

    // get attribute
    public String getAttribute(By locator, String attribName) {
        boolean flag = false;
        String attVal = null;
        try {
            attVal = driver.findElement(locator).getAttribute(attribName);
            flag = true;
        } catch (NoSuchElementException e) {
            flag = false;
        } finally {
            if (flag) {
                System.out.println("Get Attribute value, returning the attribute value");
            } else {
                System.out.println("Cant return attribute value");
            }
        }
        return attVal;
    }


    // Drag and drop
    public void dragAndDrop(By source, By target) {
        try {

            WebElement src = driver.findElement(source);
            WebElement tgt = driver.findElement(target);
            Actions action = new Actions(driver);
            action.dragAndDrop(src, tgt).build().perform();
        } catch (NoSuchElementException e) {
            e.printStackTrace();
        }
    }

    public void mouseClick(By xpath) {
        try {
            WebElement src = driver.findElement(xpath);
            Actions action = new Actions(driver);
            action.click(src).build().perform();
        } catch (NoSuchElementException e) {
            e.printStackTrace();
        }
    }

    // Scrolling down to the provided scroll value
    public void scrollDown(int scroll) throws InterruptedException {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("window.scrollBy(0, " + scroll + ")");
        TimeUnit.SECONDS.sleep(2);
    }

    // scroll up
    public void scrollUp() throws InterruptedException {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("window.scrollBy(0, 0)");
        TimeUnit.SECONDS.sleep(2);

    }

    public static String removeLastChars(String str, int chars) {
        return str.substring(0, str.length() - chars);
    }

    /**
     * @param locator
     * @param value
     */
    public void selectValuesFromDropDown(By locator, String value) {
        List<WebElement> daysList = driver.findElements(locator);

        for (int i = 0; i < daysList.size(); i++) {
            String text = daysList.get(i).getText();
            if (text.equals(value)) {
                daysList.get(i).click();
                break;
            }
        }
    }

    /**
     * Scroll using Robot class to the bottom of the page. Works same as Scroll methods,
     *
     * @param
     * @throws NoSuchElementException
     */
    public void scrollUsingRobot() {
        try {
            Robot robot = new Robot();
            robot.keyPress(KeyEvent.VK_CONTROL);
            robot.keyPress(KeyEvent.VK_END);
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException | AWTException e) {
            //log.info("Element not visible");
            e.printStackTrace();
        }

    }

    /**
     * @param element
     * @throws NoSuchElementException
     */

    public void scrollToView(By element) throws NoSuchElementException {
        try {
            waitForElementPresent(element, 8);
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", getElement(element));
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            //log.info("Element not visible");
        }

    }

    /**
     * This scrolls the side scroll bar by the number of pixel sizes provided(150, 200 ....)
     * ScrollDown - 100, 200 ...
     * ScrollUp - -100, -200 etc
     * ScrollDown to the bottom of the page - document.body.scrollHeight(add "-" to scroll all the way up)
     * ScrollHorizontal etc - scrollToView method can be used
     *
     * @param value
     * @throws NoSuchElementException
     */
    public void scrollByPixel(int value) throws NoSuchElementException {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("window.scrollBy(0," + value + ")", "");

    }
    public void scrollToBottom() throws NoSuchElementException {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("window.scrollTo(0, document.body.scrollHeight)");

    }


    public String doGetPageTitleWithContains(int timeOut, String title) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeOut));
        wait.until(ExpectedConditions.titleContains(title));
        return driver.getTitle();
    }

    public String doGetPageTitleWithIsTitle(int timeOut, String title) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeOut));
        wait.until(ExpectedConditions.titleIs(title));
        return driver.getTitle();
    }

    public String doGetPageCurrentUrl(int timeOut, String urlValue) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeOut));
        wait.until(ExpectedConditions.urlContains(urlValue));
        return driver.getCurrentUrl();
    }

    public WebElement waitForElementPresent(By locator, int timeOut) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeOut));
        return wait.until(ExpectedConditions.presenceOfElementLocated(locator));
    }

    public WebElement waitForElementToBeClickable(By locator, int timeOut) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeOut));
        return wait.until(ExpectedConditions.elementToBeClickable(locator));
    }
    public void syncWait(int timeOut) throws InterruptedException {
        TimeUnit.SECONDS.sleep(timeOut);
    }
    public WebElement waitForElementToBeVisible(By locator, int timeOut) {
        WebElement element = getElement(locator);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeOut));
        return wait.until(ExpectedConditions.visibilityOf(element));
    }

    public List<WebElement> visibilityOfAllElements(By locator, int timeOut) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeOut));
        return wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(locator));
    }

    public void clickWhenReady(By locator, int timeOut) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeOut));
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(locator));
        element.click();
    }

    public Alert waitForAlertPresent(int timeOut) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeOut));
        return wait.until(ExpectedConditions.alertIsPresent());
    }


    public boolean isElementDisplayed(By locator, int timeout) {
        WebElement element = null;
        boolean flag = false;
        for (int i = 0; i < timeout; i++) {
            try {
                element = driver.findElement(locator);
                flag = element.isDisplayed();
                break;
            } catch (Exception e) {
                System.out.println("waiting for element to be present on the page -->" + i + "secs");
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e1) {
                }
            }
        }
        return flag;
    }

    /**
     * @param locator
     * @return
     */

    public int getGridRowCount(By locator) {
        WebElement table = driver.findElement(locator);
        List<WebElement> tableRows = table.findElements(By.xpath("//tr[contains(@class, 'TableRow-hover')]"));
        return tableRows.size();
    }

    public int getGridColumnCount(By locator) {

        WebElement table = driver.findElement(locator);
        List<WebElement> tableRows = table.findElements(By.xpath("//tr[contains(@class, 'TableRow-hover')]"));
        for (int i = 0; i < tableRows.size(); i++) {
            List<WebElement> tableColumns = tableRows.get(i).findElements(By.xpath("//td[@data-row-index =" + i + " and @data-hoverable='true']"));
            if (tableColumns.size() > 0) {
                return tableColumns.size() + 1;
            }
        }
        return 0;
    }

    /**
     * @param locator
     * @param columnNumber
     * @return
     */

    public String[] getGridColumnData(By locator, int columnNumber) {
        String[] columnData = null;

        WebElement table = driver.findElement(locator);
        List<WebElement> tableRows = table.findElements(By.xpath("//tr[contains(@class, 'TableRow-hover')]"));
        if (tableRows.size() > 0) {
            columnData = new String[tableRows.size()];
            for (int i = 0; i < tableRows.size(); i++) {
                List<WebElement> tableColumns = tableRows.get(i).findElements(By.xpath("//td[@data-hoverable='true']"));
                if (tableColumns.size() > 0) {
                    String text = tableColumns.get(columnNumber).getAttribute("textContent");
                    columnData[i] = text;
                }
            }
            return columnData;
        } else {
            return columnData;
        }
    }

    public List<String> sortItemsList(List<String> listOfString, String order) {
        if (order.equalsIgnoreCase("descending") || order.equalsIgnoreCase("desc")) {
            Collections.sort(listOfString, Collections.reverseOrder());
        } else {
            Collections.sort(listOfString);
        }
        return listOfString;
    }

    public List<Integer> sortItemsNums(List<Integer> listOfString, String order) {
        if (order.equalsIgnoreCase("descending") || order.equalsIgnoreCase("desc")) {
            Collections.sort(listOfString, Collections.reverseOrder());
        } else {
            Collections.sort(listOfString);
        }
        return listOfString;
    }

    @Deprecated
    protected void waitUntilPageLoad(long timeout) {

        WebDriverWait wait = new WebDriverWait(driver, timeout);
        wait.until((ExpectedCondition<Boolean>) driver -> ((JavascriptExecutor) Objects.requireNonNull(driver))
                .executeScript("return document.readyState").toString().equals("complete"));

    }

    public boolean isPageLoadComplete() {
        return ((JavascriptExecutor) driver).executeScript("return document.readyState").equals("loaded")
                || ((JavascriptExecutor) driver).executeScript("return document.readyState").equals("complete");
    }


    public WebElement waitForElementWithFluentWaitConcept(By locator, int timeOut) {
        Wait<WebDriver> wait = new FluentWait<WebDriver>(driver)
                .withTimeout(Duration.ofSeconds(timeOut))
                .pollingEvery(Duration.ofSeconds(2))
                .ignoring(NoSuchElementException.class);

        return wait.until(ExpectedConditions.presenceOfElementLocated(locator));
    }

    public WebElement waitForElementWithFluentWait(final By locator, int timeOut) {
        Wait<WebDriver> wait = new FluentWait<WebDriver>(driver)
                .withTimeout(Duration.ofSeconds(timeOut))
                .pollingEvery(Duration.ofSeconds(2))
                .ignoring(NoSuchElementException.class);

        WebElement element = wait.until(new Function<WebDriver, WebElement>() {
            public WebElement apply(WebDriver driver) {
                return driver.findElement(locator);
            }
        });

        return element;
    }

}
