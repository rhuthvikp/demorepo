package com.mck.rp.pageObjects;

import com.mck.rp.base.BasePage;
import com.mck.rp.utilities.ElementUtil;
import io.qameta.allure.Step;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;


public class CommonPage extends BasePage {
    private final WebDriver driver;
    //Regimen Analysis Objects
    //Regimen Search - Works in both Pat and Prac Analysis pages
    public By srhRegimen = By.xpath("//input[@type='search']");
    //Search For Regimen in Patient Analyis
    public By srhRegimenPat = By.xpath("//input[@placeholder='Search by regimen, drug or diagnosis']");
    public By patTable = By.xpath("//table[@data-testid='patient-analysis-table']");
    public By pracTable = By.xpath("//table[@data-testid='practice-analysis-table']");
    //Regimen Analysis- Pat Analysis - Use Defaults button
    public By useDefaults = By.xpath("//button[@data-testid='patient-information-use-default-values']");
    public By gridResults = By.xpath("//p[contains(@class, 'MuiTablePagination-caption') and (not(contains(text(), 'Rows')))]");

    //View and Edit Details button in the grid
    public By btnViewEditDts = By.xpath("//a[@data-testid='view-and-edit-details-button']");
    //logout button when u login
    public By btnlogout = By.xpath("//span[contains(text(),'log out')]");
    public By headingGrids = By.xpath("//h2[contains(@class,'MuiTypography-h2')]");
    public By headingPages = By.xpath("//h1[contains(@class,'MuiTypography-h1')]");
    public By drugDetailsTitle = By.xpath("//h1[@data-testid ='page-title']");
    public By gridSrhClear = By.xpath("//button[@type='button' and @title ='clear']");
    public By leftMenu = By.xpath("//ul[@data-testid='vertical-menu']//li");
    public By anlysisCriteriaCollapse = By.xpath("analysis-criteria-collapse");

    //dropdowns in Patient Analysis and Practice
    public By ddDiagnosis = By.xpath("//div[@data-testid='diagnosis-select']");
    public By ddDrugs = By.xpath("//div[@data-testid='drugs-select']");
    public By ddBreakEven = By.xpath("//div[@data-testid='break-even-select']");
    public By ddInsurerFeeSche = By.xpath("//div[@data-testid='insurer-fee-schedule']");
    //Patient Information Page
    public By patInfoCollapse = By.xpath("//button[@data-testid='patient-information-collapse']");
    //public By btnFilterSelectClear = By.xpath("//div[@data-testid='diagnosis-select']//span[@data-testid = 'clear-button']");
    public By patSummaryPatResp = By.xpath("//div[@data-testid='patient-responsibility']//span");
    public By analysisCriteriaCollapse = By.xpath("//button[@data-testid='analysis-criteria-collapse']");
    public By additionalFunding = By.xpath("//div[@data-testid='total-fundings']//span");

    //Practice Analysis Page Specific Elements
    //Comparisontable - number of items selected to compare
    public By comparisonTableH1 = By.xpath("//th//h1");
    public By summaryDrugTable = By.xpath("//form[@data-testid='drug-table-container']//table");
    public By summaryNonDrugTable = By.xpath("//form[@data-testid='non-drug-table-container']//table");
    public By summaryAddDrug = By.xpath("//button[contains(@data-testid,'add-drug')]");
    public By summaryAddNondrug = By.xpath("//button[contains(@data-testid,'add-non-drug')]");
    public By addDrugNondrugDialogH2 = By.xpath("//div[@role='dialog']//h2");
    public By patientInformationCollapse = By.xpath("//button[@data-testid='patient-information-collapse']");

    //Patient Analysis - Patient Information
    public By secondaryPaysPrimaryDeductible = By.xpath("//input[@name='secondaryInsurance.paysPrimaryDeductible']");
    //Pat Analysis - Regimen Summary - Supportive care block elements
    public By suppCareSummary = By.xpath("//div[@data-testid='regimen-summary-supportive-care']");
    ElementUtil elementUtil;
    By primaryOutofPocketWheel = By.xpath("//input[@name='primaryInsurance.remainingOutOfPocket.remaining']//following::button");
    By primaryDeductibleWheel = By.xpath("//input[@name='primaryInsurance.remainingDeductible.remaining']//following::button");
    By secondaryOutofPocketWheel = By.xpath("//input[@name='secondaryInsurance.remainingOutOfPocket.remaining']//following::button");
    By secondaryDeductibleWheel = By.xpath("//input[@name='secondaryInsurance.remainingDeductible.remaining']//following::button");
    By moreButton = By.xpath("//button[@type='button']//h6[contains(text(),'More')]");

    //Drugs -
    // div[@role='button']//span[contains(text(),'Number of Billing Units')]

    /**
     * @param driver
     */

    // constructor of the page class:
    public CommonPage(WebDriver driver) {
        this.driver = driver;
        elementUtil = new ElementUtil(driver);
    }

    /**
     * @param mytable
     * @param testString
     * @param colNum
     * @param startingRowNum
     * @param endRowNum
     * @return
     */
    //Change col num based on what you need to validate.
    public void verifyTextWebtableSpecificColAndRows(WebElement mytable, String testString, int colNum,
                                                     int startingRowNum, int endRowNum) {
        boolean flag = false;
        try {
            // to locate rows of table
            List<WebElement> rows_table = mytable.findElements(By.tagName("tr"));
            System.out.println("Number of rows in the table are " + rows_table.size());

            // loop will execute till the last row of table
            for (int row = startingRowNum; row < endRowNum; row++) {
                // to locate columns(cell) of that specific row
                List<WebElement> columns_row = rows_table.get(row).findElements(By.tagName("td"));
                // to calculate num of columns(cells) in that specific row
                int columns_count = columns_row.size();
                System.out.println("Number of columns in the row " + row + " are " + columns_count);

                // loop will execute till the last cell of that specific row
                // To retrieve text from that specific cell
                String celtext = columns_row.get(colNum).getText().trim();
                System.out.println("Cell value of row number " + row + " and columns number 2 is " + celtext);

                if (celtext.contains(testString)) {
                    System.out.println("Verfication passed. Cell value " + celtext + " test string " + testString);
                    flag = true;
                } else {
                    System.out.println("Verfication failed. Cell value " + celtext + " test string " + testString);
                    flag = false;
                }
            }

        } catch (Throwable e) {
            e.printStackTrace();
        }
        //return flag
    }

    /**
     * @param eleName
     * @return
     */
    //Methods
    //Practice Ananlysis - Analysis criteria like height, weight, bsa, coinsuracne etc
    //Patient Analysis - fname, laname, decutible, mrn etc
    public By getInputField(String eleName) {
        By element = By.xpath("//input[@name='" + eleName + "']");
        return element;
    }

    /**
     * @param dataTestId
     * @return: Element
     */

    public By getFilterSelectClear(String dataTestId) {
        By element = By.xpath("//div[@data-testid='" + dataTestId + "']//span[@data-testid = 'clear-button']");
        return element;
    }

    /**
     * @param eleName
     * @return
     */
    public By getBtnTypeSubmit(String eleName) {
        By btn = By.xpath("//button[@type='submit']//span[contains(text(),'" + eleName + "')]");
        return btn;
    }

    /**
     * @param eleName
     * @return
     */
    //Patient Analysis - Logout, Add Secondary Insurance, Use Defaults, Export,
    public By getBtnTypeButton(String eleName) {
        By btn = By.xpath("//button[@type='button']//span[contains(text(),'" + eleName + "')]");
        return btn;
    }

    /**
     * @param inputName
     * @return
     */
    //Pracrtice Configuration - Configure Analysis Views buttons- check if the checkboxes are checked or not
    public boolean getCheckboxChecked(String inputName) {
        boolean flag = false;
        WebElement inputBtn = driver.findElement(By.xpath("//input[@name = '" + inputName + "']//parent::span//parent::span[contains(@class,'MuiButton')]"));
        flag = inputBtn.getAttribute("className").contains("checked");
        return flag;
    }


    //Regimen Manangement button type Button and Strong
    public By getBtnTypeStrong(String eleName) {
        By btn = By.xpath("//button[@type='button']//Strong[contains(text(),'" + eleName + "')]");
        return btn;
    }

    //Regimen Manangement button type Button and And DataTestid
    public By getBtnTypeByTestId(String eleName) {
        By btn = By.xpath("//button[@data-testid='" + eleName + "']");
        return btn;
    }

    /**
     * @param eleName
     * @return
     */
    //Patient Analysis - Logout, Add Secondary Insurance, Use Defaults, Export,
    public By getConfigFieldsButton(String eleName) {
        By configFields = By.xpath(" //form[@data-testid='" + eleName + "-table-container']//button[@data-testid='configure-fields']");
        return configFields;
    }

    /**
     * @param eleName
     * @return
     */
    //PatAnalysis - Calculate,
    public By getTabs(String eleName) {
        By tab = By.xpath("//div[@role='tablist']//span[contains(text(),'" + eleName + "')]");
        return tab;
    }

    public By getDialogBtn(String buttonName) {
        By tab = By.xpath("//div[@role='dialog']//span[contains(text(),'" + buttonName + "')]");
        return tab;
    }

    /**
     * @param table
     * @param row
     * @param col
     * @return
     */
    //Rows are in odd numbers 1, 3, 5 etc, even numbers are the separators in the grid
    public By getGridcell(String table, int row, int col) {
        //Patient Table = patient-analysis-table PracticeTable = practice-
        By gridRow = By.xpath("//table[@data-testid='" + table + "']//tr[" + row + "]//td[" + col + "]");
        return gridRow;
    }

    /**
     * @return
     */
    // page actions:
    //@Step("getting login page title...")
    public String getGridHeading() {
        return driver.findElement(headingGrids).getAttribute("textContent");
    }

    /**
     * @return
     */
    public String getPageHeading() {
        return driver.findElement(headingPages).getAttribute("textContent");
    }

    /**
     * @param datatestid
     * @param filterName
     * @return
     */
    //Practice Analysis - Regimen library grid dropdowns like breakeven, diagnosis, drug --
    // For clicking on all drop downs inside the table grid
    //Base Formulary Therapeutic class, Classification
    //To click on any dropdowns to open the list - Provide testid and span text
    //No class references while getting objects
    public WebElement getGridFilters(String datatestid, String filterName) {
        WebElement element = driver.findElement(By.xpath("//div[@data-testid='" + datatestid + "']//span[contains(text(), '" + filterName + "')]"));
        return element;
    }

    /**
     * @param eleName
     * @return
     */
    //Practice Analysis - Regimen library grid dropdowns like growth factors, antiematics - For clicking on ddowns
    //Provide the dropdownName - Antiematics, Growth Factors
    public List<WebElement> filtersList(String eleName) {
        List<WebElement> filter = driver.findElements(By.xpath("//legend[contains(text(), '" + eleName + "')]//following::span//child::span"));

        return filter;
    }

    /**
     * @return
     */
    //@Step("getting logout link presence...")
    public boolean isLogoutExist() {
        return elementUtil.doIsDisplayed(btnlogout);
    }

    /**
     * Click on check boxes, radio buttons, input boxes, buttons or any other elements where Span tag's text matches the given text
     *
     * @param text
     */

    public void clickSpanText(String text) {
        try {
            elementUtil.clickWhenReady(By.xpath("//span[contains(text(),'" + text + "')]"), 5);
        } catch (NoSuchElementException e) {
            e.printStackTrace();
        }
    }

    /**
     * logouts the user and brings back to the login page.
     */
    public void logout() {
        elementUtil.doClick(btnlogout);
        clickSpanText("Yes");
    }


    /**
     * @param value
     * @throws NoSuchElementException
     */
    public void srhRegDrugDiagAndEnter(String value) throws NoSuchElementException {
        try {
            WebElement element = elementUtil.getElement(srhRegimen);
            //System.out.println("Srh Value " + element.getAttribute("value"));
            if (!element.getAttribute("value").isEmpty()) {
                elementUtil.clickWhenReady(gridSrhClear, 10);
            }
            element.sendKeys(value);
            element.sendKeys(Keys.ENTER);
            TimeUnit.SECONDS.sleep(1);
        } catch (NoSuchElementException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param table
     * @param row
     * @param col
     */
    @Step("Clicking on the results grid cell with given table id: {0}, row: {1}, and column: {2}")
    public void clickGridCell(String table, int row, int col) {
        try {
            elementUtil.waitForElementPresent(getGridcell(table, row, col), 5);
            elementUtil.clickWhenReady(getGridcell(table, row, col), 5);
            //elementUtil.doClick(getGridcell(table, row, col));
        } catch (NoSuchElementException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param tabName
     */
    @Step("Clicking on the tab inside the page with text: {0}")
    public void clickTabs(String tabName) {
        try {
            elementUtil.clickWhenReady(getTabs(tabName), 8);
        } catch (NoSuchElementException e) {
            e.printStackTrace();
        }
    }

    /**
     * Clicks on the dialog button with the given name, again checks whether the button is still
     * displayed. If its displayed then clicks again (Some dialog buttons need to be clicked twice to be able
     * to take effect) - Actions is working, commented the button check once clicked, uncomment if the click
     * is not working.
     *
     * @param btnName
     */
    public void clickDialogBtn(String btnName) {
        try {
            elementUtil.doActionsClick(getDialogBtn(btnName));
            // if (elementUtil.isElementDisplayed(getDialogBtn(btnName), 5))
            //     elementUtil.doActionsClick(getDialogBtn(btnName));
        } catch (NoSuchElementException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param elementName
     */
    public void clickImg(By elementName) {
        try {
            elementUtil.getElement(elementName);
            elementUtil.clickWhenReady(elementName, 8);
        } catch (NoSuchElementException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param tableHeader
     */
    @Step("Clicking on the result grid with header text: {0}")
    public void clickTableHeaderForSort(String tableHeader) {

        List<WebElement> headers = elementUtil.getElements(By.xpath("//span[@role= 'button']"));
        try {
            for (WebElement element : headers) {
                if (element.getAttribute("textContent").equals(tableHeader)) {
                    element.click();
                }
            }
        } catch (NoSuchElementException e) {
            e.printStackTrace();
        }

    }


    /**
     * @param linkName
     */
    @Step("Clicking on the Link with text: {0}")
    public void clickByLinkText(String linkName) {
        try {
            elementUtil.clickWhenReady((By.linkText(linkName)), 8);
        } catch (NoSuchElementException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param linkName
     */
    @Step("Clicking on the link with matching partial text: {0}")
    public void clickByPartialLinkText(String linkName) {
        try {
            elementUtil.clickWhenReady((By.partialLinkText(linkName)), 8);
            TimeUnit.SECONDS.sleep(4);
        } catch (NoSuchElementException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param btnName
     */
    @Step("Clicking on the button (button type: Submit)  with text: {0}")
    public void clickSubmit(String btnName) {
        try {
            elementUtil.scrollToView(getBtnTypeSubmit(btnName));
            elementUtil.clickWhenReady(getBtnTypeSubmit(btnName), 8);
            TimeUnit.SECONDS.sleep(2);
        } catch (NoSuchElementException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param btnName
     */
    @Step("Clicking on the button (button type: Button)  with text: {0}")
    public void clickButton(String btnName) {
        try {
            elementUtil.clickWhenReady(getBtnTypeButton(btnName), 8);
            TimeUnit.SECONDS.sleep(1);
        } catch (NoSuchElementException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param btnName
     */
    @Step("Clicking on the button (button type: Button)  with text: {0}")
    public void clickButtonTypeStrong(String btnName) {
        try {
            elementUtil.clickWhenReady(getBtnTypeStrong(btnName), 8);
            TimeUnit.SECONDS.sleep(1);
        } catch (NoSuchElementException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param ddTestid
     * @param ddName
     */
    @Step("Clicking on the grid filters in table: {0} and dropdown filter: {1}")
    public void clickGridFilters(String ddTestid, String ddName) {
        try {
            getGridFilters(ddTestid, ddName).click();
            TimeUnit.SECONDS.sleep(1);
        } catch (NoSuchElementException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    //table[@data-testid='regimen-formulary-table']//tr[1]//span[@data-testid='row-checkbox']

    /**
     * @param tableId
     * @param rowId
     */
    @Step("Clicking on Grid CheckBoxes with TableId: {0} and in Row: {1}")
    public void clickGridRowWithCheckBox(String tableId, int rowId) {
        try {
            elementUtil.clickWhenReady(By.xpath("//table[@data-testid='" + tableId + "']//tr[" + rowId + "]//span[@data-testid='row-checkbox']"), 5);
        } catch (NoSuchElementException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     */
    public String subString(String stringValue, String separator, int start) {
        int sepPos = stringValue.indexOf(separator);
        String subStringValue = stringValue.substring(start, sepPos);
        return subStringValue;
    }

    /**
     * @param ddTestname
     * @param itemIndex
     */
    @Step("Analysis Criteria - Clicking on the filter dropdown: {0}, and selecting the item with index: {1}")
    public void analysisCriAntieGfactorSelectItem(String ddTestname, int itemIndex) {
        try {
            elementUtil.clickWhenReady(By.xpath("//div[@data-testname='" + ddTestname + "']"), 8);
            elementUtil.clickWhenReady(By.xpath("//li[@data-testid='select-option_" + itemIndex + "']"), 8);
            TimeUnit.SECONDS.sleep(1);
        } catch (NoSuchElementException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param ddTestname
     * @param itemIndex
     */
    @Step("Regimen Summary - Clicking on the filter dropdown: {0}, and selecting the item with index: {1}")
    public String summaryAntieGfactorSelectItem(String ddTestname, int itemIndex) {
        String itemText = null;
        try {
            By dropdown = By.xpath("//div[@data-testid='regimen-summary-supportive-care']//div[@data-testname='" + ddTestname + "']");
            elementUtil.clickWhenReady(dropdown, 8);
            TimeUnit.SECONDS.sleep(1);
            By ddItem = By.xpath("//li[@data-testid='select-option_" + itemIndex + "']");
            itemText = elementUtil.getElement(ddItem).getAttribute("textContent");
            elementUtil.clickWhenReady(ddItem, 8);
            TimeUnit.SECONDS.sleep(1);
            elementUtil.getElement(dropdown).sendKeys(Keys.TAB);
            TimeUnit.SECONDS.sleep(1);
        } catch (NoSuchElementException | InterruptedException e) {
            e.printStackTrace();
        }
        return itemText;
    }

    /**
     * @param itemIndex
     */
    public void selectListItemByIndex(int itemIndex) {
        try {
            WebElement option = driver.findElement(By.xpath("//li[@data-testid='select-option_" + itemIndex + "']"));
            elementUtil.clickWhenReady(By.xpath("//li[@data-testid='select-option_" + itemIndex + "']"), 8);
            TimeUnit.SECONDS.sleep(2);
            //option.sendKeys(Keys.ESCAPE);
        } catch (NoSuchElementException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param name
     */
    @Step("Selecting the filter dropdown item with matching text: {0}")
    public void selectListItemByName(String name) {
        try {
            WebElement option = driver.findElement(By.xpath("//li[@data-testid='select-option_0']"));
            elementUtil.clickWhenReady(By.xpath("//div[contains(text(),'" + name + "')]"), 8);
            TimeUnit.SECONDS.sleep(1);
            //option.sendKeys(Keys.ESCAPE);
        } catch (NoSuchElementException | InterruptedException e) {

            e.printStackTrace();
        }
    }

    /**
     * Returns the selected item from filters
     *
     * @param filterTestId
     */
    @Step("Getting the selected filter item in the filter dropdown: {0}")
    public String getSelectedFilterItem(String filterTestId) {
        String element = null;
        try {
            element = elementUtil.getTextcontent(By.xpath("//div[@data-testid='" + filterTestId + "']//span"));
        } catch (NoSuchElementException e) {
            e.printStackTrace();
        }
        return element;
    }

    /**
     * @param itemIndex
     */
    public void selectFilterItemByIndex(int itemIndex) {
        try {
            WebElement option = driver.findElement(By.xpath("//li[@data-testid='select-option_" + itemIndex + "']"));
            elementUtil.clickWhenReady(By.xpath("//li[@data-testid='select-option_" + itemIndex + "']"), 8);
            TimeUnit.SECONDS.sleep(1);
            option.sendKeys(Keys.ESCAPE);
        } catch (NoSuchElementException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param name
     */
    public void selectFilterItemByName(String name) {
        try {
            WebElement option = driver.findElement(By.xpath("//li[@data-testid='select-option_0']"));
            elementUtil.clickWhenReady(By.xpath("//div[contains(text(),'" + name + "')]"), 8);
            TimeUnit.SECONDS.sleep(1);
            option.sendKeys(Keys.ESCAPE);
        } catch (NoSuchElementException | InterruptedException e) {

            e.printStackTrace();
        }
    }

    /**
     * @param i
     * @return
     */
    //All the fields in Regimen Summary details page like Cost, Patient Responsibility, Insurance Responsibility
    // and Margin will be returned. There are no specific names for each field so we need to match by the field's
    //number
    public String summaryfields(int i) {
        List<WebElement> summaryFields = driver.findElements(By.xpath("//div[@data-testid='regimen-summary-footer']//div[@data-testid='footer-field']"));
        //System.out.println(dtsCost.size());
        String separator = "$";
        int sepPos = summaryFields.get(i).getAttribute("textContent").indexOf(separator) - 1;
        String summaryFieldsValue = summaryFields.get(i).getAttribute("textContent").substring(sepPos + separator.length());
        return summaryFieldsValue;
    }

    /**
     * @return
     */
    public int filterItemsCount() {
        int listSize = 0;
        try {
            WebElement option = driver.findElement(By.xpath("//li[@data-testid='select-option_0']"));
            List<WebElement> listItems = driver.findElements(By.xpath("//li[contains(@data-testid, 'select-option')]"));
            listSize = listItems.size();
            option.sendKeys(Keys.ESCAPE);
        } catch (NoSuchElementException e) {
            e.printStackTrace();
        }
        return listSize;
    }

    /**
     * @param srhField
     * @param value
     */
    @Step("Sending text: {1} to the search field: {0}")
    public void sendKeysByAction(String srhField, String value) {
        try {
            Actions a = new Actions(driver);
            a.click(elementUtil.getElement(getInputField(srhField))).build().perform();
            elementUtil.getElement(getInputField(srhField)).sendKeys(Keys.END);
            int len = elementUtil.getElement(getInputField(srhField)).getAttribute("value").length();
            for (int i = 0; i < len; i++) {
                elementUtil.getElement(getInputField(srhField)).sendKeys(Keys.BACK_SPACE);
            }
            elementUtil.doSendKeys(getInputField(srhField), value);
            TimeUnit.SECONDS.sleep(1);
        } catch (NoSuchElementException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param srhField
     * @param value
     */
    public void sendValue(String srhField, String value) {
        try {
            Actions a = new Actions(driver);
            a.doubleClick(elementUtil.getElement(getInputField(srhField))).build().perform();
            //a.doubleClick(elementUtil.getElement(getInputField(srhField))).build().perform();
            TimeUnit.SECONDS.sleep(1);
            elementUtil.getElement(getInputField(srhField)).sendKeys(value);
        } catch (NoSuchElementException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     */
    public void pracTableAddToCompare(By tableName, int rowsToCompare) {

        WebElement table = driver.findElement(tableName);
        List<WebElement> tableRows = table.findElements(By.xpath("//tr[contains(@class, 'TableRow-hover')]//span[@data-testid='row-checkbox']"));
        try {
            if (tableRows.size() >= rowsToCompare) {
                for (int i = 1; i <= rowsToCompare; i++) {
                    table.findElement(By.xpath("//tr[contains(@class, 'TableRow-hover')][" + i + "]//span[@data-testid='row-checkbox']")).click();
                    elementUtil.scrollByPixel(150);
                    TimeUnit.SECONDS.sleep(1);
                }
            }
        } catch (NoSuchElementException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param value
     */
    //To click on the left menu items - Regimen Analysis, Practice Analysis, Base Formulary, Practice Account etc
    @Step("Clicking on the Left Menu item: {0}")
    public void clickLeftMenuItem(String value) {
        List<WebElement> menuItems = driver.findElements(leftMenu);
        try {
            for (WebElement element : menuItems) {
                if (element.getAttribute("textContent").equals(value)) {
                    element.click();
                }
            }
        } catch (NoSuchElementException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param value
     */
    //To click on the left menu items - Regimen Analysis, Practice Analysis, Base Formulary, Practice Account etc
    public void clickSubListItem(String value) throws InterruptedException {
        WebElement liItem = driver.findElement(By.xpath("//li[@role='menuitem']//div[contains(text(), '" + value + "')]"));
        elementUtil.clickWhenReady(By.xpath("//li[@role='menuitem']//div[contains(text(), '" + value + "')]"), 8);

        /*
        List<WebElement> liItem = driver.findElements(By.xpath("//li[@role='menuitem']//div"));
        for (WebElement element : liItem) {
            if (element.getAttribute("textContent").equals(value)) {
                element.click();
                TimeUnit.SECONDS.sleep(2);
            }
        }
        */

        //if(liItem.getAttribute("textContent")==value){
        //elementUtil.clickWhenReady((By.xpath("//li[@role='menuitem']//div")), 8);}
    }

    /**
     * @param locator
     * @param
     * @return
     */
    public String getTextContent(By locator) {
        WebElement element = driver.findElement(locator);
        return element.getAttribute("textContent");
    }

    /**
     * @param  locator
     * @param attributeName
     * @return elements attributeValue
     */
    public String getAttributeValue(By locator, String attributeName) {
        WebElement element = driver.findElement(locator);
        return element.getAttribute(attributeName);
    }

    /**
     * @param locator
     * @param i
     * @return
     */
    public String getGridRowData(By locator, int i) {
        WebElement table = driver.findElement(locator);
        WebElement tableRow = table.findElement(By.xpath("//tr[contains(@class, 'TableRow-hover')][" + i + "]"));
        return tableRow.getAttribute("textContent");
    }

    /**
     * Getting specific cell data in the table grid
     * <p>
     * Cell data starts from [2] and then increments based on the cells
     * Eg: If there are 3 rows with 7 columns then it starts from [2][0] t0 [2][20]
     *
     * @param locator
     * @return
     */
    public String[][] getCellData(By locator) {
        WebElement table = driver.findElement(locator);
        List<WebElement> tableRows = table.findElements(By.xpath("//tr[contains(@class, 'TableRow-hover')]"));
        List<WebElement> tableColumns = tableRows.get(tableRows.size() - 1).findElements(By.xpath("//td[@data-hoverable='true']"));
        String[][] webTableData = new String[tableRows.size()][tableColumns.size()];

        for (int i = 0; i < tableRows.size(); i++) {
            List<WebElement> tableColumns1 = tableRows.get(i).findElements(By.xpath("//td[@data-hoverable='true']"));
            webTableData = new String[tableRows.size()][tableColumns1.size()];
            for (int j = 0; j < tableColumns.size(); j++) {
                if (tableColumns1.size() > 0) {
                    String text = tableColumns1.get(j).getAttribute("textContent");
                    webTableData[i][j] = text;
                } else {
                    break;
                }
            }
        }
        return webTableData;
    }

    /**
     * @param locator
     * @param row
     * @return
     */

    public String[] getRowCellData(By locator, int row) {
        WebElement table = driver.findElement(locator);
        String[] webTableData = null;
        List<WebElement> tableRows = table.findElements(By.xpath("//tr[contains(@class, 'TableRow-hover')]"));
        //System.out.println("Table Rows: " + tableRows.size());
        //List<WebElement> tableColumns = tableRows.get(tableRows.size()-1).findElements(By.xpath("//td[@data-hoverable='true']"));
        //System.out.println("Table Columns: " + tableColumns.size());
        //String[][] webTableData = new String[tableRows.size()][tableColumns.size()];

        // for (int i = 0; i < tableRows.size(); i++) {
        List<WebElement> tableColumns1 = tableRows.get(row).findElements(By.xpath("//td[@data-row-index =" + row + " and @data-hoverable='true']"));
        //System.out.println("Table Columns in grid: " + tableColumns1.size());
        webTableData = new String[tableColumns1.size()];
        for (int j = 0; j < tableColumns1.size() - 1; j++) {
            if (tableColumns1.size() > 0) {
                String text = tableColumns1.get(j).getAttribute("textContent");
                webTableData[j] = text;
                //System.out.println("Data: " + j + text);
            } else {
                break;
            }
        }
        //}
        return webTableData;
    }

    /**
     * @param locator
     * @param row
     * @return
     */

    public String[] getRowsColumnData(By locator, int row, int column) {
        String[] webTableData = null;

        WebElement table = driver.findElement(locator);
        List<WebElement> tableRows = table.findElements(By.xpath("//tr[contains(@class, 'TableRow-hover')]"));
        //System.out.println("Table Rows: "+ tableRows.size());
        if (tableRows.size() > 0) {

            webTableData = new String[tableRows.size()];
            for (int i = 0; i < tableRows.size(); i++) {
                List<WebElement> tableColumns = tableRows.get(i).findElements(By.xpath("//td[@data-row-index='" + i + "']"));
                //System.out.println("Table Columns: " + tableColumns.size());

                if (tableColumns.size() > 0) {
                    String text = tableColumns.get(column).getAttribute("textContent");
                    //System.out.println("Col Data: "+ i +  text);
                    webTableData[i] = text;
                } else {
                    break;
                }
            }
        }
        return webTableData;
    }

    /**
     * @param locator
     * @param row
     * @param column
     * @return
     */
    public String getRowColumnData(By locator, int row, int column) {
        String text = null;

        WebElement table = driver.findElement(locator);
        List<WebElement> tableRows = table.findElements(By.xpath("//tr[contains(@class, 'TableRow-hover')]"));
        if (tableRows.size() > 0) {
            List<WebElement> tableColumns = tableRows.get(row).findElements(By.xpath("//td[@data-row-index='" + row + "']"));
            if (tableColumns.size() > 0) {
                text = tableColumns.get(column).getAttribute("textContent");
            }
        }
        return text;
    }

    /**
     * @return
     * @throws NoSuchElementException
     */
    //@Step("Number of rows/results in the table grid")
    public int getNumOfGridResults() throws NoSuchElementException {
        int gridNum = 0;
        try {
            TimeUnit.SECONDS.sleep(1);
            WebElement gridResultNum = driver.findElement(gridResults);
            String result = gridResultNum.getText();
            String separator = "f ";
            int sepPos = result.indexOf(separator);
            gridNum = Integer.parseInt(result.substring(sepPos + separator.length()));
        } catch (NoSuchElementException | InterruptedException e) {
            e.printStackTrace();
        }
        return gridNum;
    }

    /**
     * @param value
     */
    @Step("Selecting the value: {0} in the pagination dropdown")
    public void selectPaginationRows(String value) {
        try {
            Select pagination = new Select(driver.findElement(By.xpath("//select[@aria-label='select rows per page']")));
            pagination.selectByValue(value);
            TimeUnit.SECONDS.sleep(2);
        } catch (NoSuchElementException | InterruptedException e) {

            e.printStackTrace();
        }
    }

    /**
     * @param elementName
     * @param colNum      - Provide the colnum in the table for the colum you want to sort (Index starts from 0)
     * @return
     */
    public List<String> tableColumnList(By elementName, int colNum) {
        List<String> beforeSortList = new ArrayList<>();
        int rowCount = elementUtil.getGridRowCount(elementName);
        for (int i = 0; i < rowCount; i++) {
            beforeSortList.add(getRowColumnData(elementName, i, colNum));
        }
        return beforeSortList;
    }

    /**
     * @param elementName
     * @param colNum      - Provide the colnum in the table for the colum you want to sort (Index starts from 0)
     * @return
     */
    public List<Integer> tableNumColumnList(By elementName, int colNum) {
        List<String> replaceInt = new ArrayList<>(), beforeInt = new ArrayList<>();
        List<Integer> parseInt = new ArrayList<>();
        int rowCount = elementUtil.getGridRowCount(elementName);
        System.out.println("Rows: " + rowCount);
        for (int i = 0; i < rowCount; i++) {
            beforeInt.add(getRowColumnData(elementName, i, colNum));
            replaceInt.add(beforeInt.get(i).replaceAll("[$,.]", ""));
            parseInt.add(Integer.parseInt(replaceInt.get(i)));

        }
        return parseInt;
    }

}
