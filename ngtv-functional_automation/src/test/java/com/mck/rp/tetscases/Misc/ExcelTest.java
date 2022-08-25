package com.mck.rp.tetscases.Misc;

import com.mck.rp.base.BasePage;
import com.mck.rp.base.BaseTest;
import com.mck.rp.pageObjects.LoginPage;
import com.mck.rp.pageObjects.CommonPage;
import com.mck.rp.utilities.ElementUtil;
import com.mck.rp.utilities.ExcelReaderUtil;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.Hashtable;
import java.util.Properties;

//This script explains the usage of ExcelReaderUtil class
public class ExcelTest extends BaseTest {
    public String DATA_SHEET = "loginDts";
    public BasePage basePage;
    public LoginPage lp;
    public Properties prop;
    public ElementUtil eu;
    public CommonPage rp;

    @BeforeClass
    public void ExcelTest() throws InterruptedException {
        rp = new CommonPage(driver);
        lp = new LoginPage(driver);
        eu = new ElementUtil(driver);

    }

    /*
    public static void main(String[] args) {

        ExcelReaderUtil excel = new ExcelReaderUtil("./src/test/resources/testdata/TestData.xlsx");
        System.out.println("Total Rows: " + excel.getRowCount("loginDts"));

        System.out.println("Total Columns: " + excel.getColumnCount("loginDts"));
        System.out.println("Cell Data by Row and Col Num: " + excel.getCellData("loginDts", 1, 2));
        System.out.println("Cell Data By Col Name: " + excel.getCellData("loginDts", "emailId", 3));
        System.out.println("Sheet Exists?: " + excel.isSheetExist("login"));

        //System.out.println("Total Rows: " + excel.setCellData("loginDts", "emailId", 4,"AddUserId"));
        System.out.println("Total Rows: " + excel.getRowCount("loginDts"));
    }
*/
    @Test(dataProvider = "getData")
    public void testData(Hashtable<String, String> data) throws IOException, InterruptedException {

        rp.clickTabs("Practice Analysis");
        rp.logout();
        lp.doLogin(data.get("userId"), data.get("password"));
        Thread.sleep(2000);
        rp.clickTabs("Practice Analysis");
        rp.srhRegDrugDiagAndEnter(data.get("srhDrug"));
        System.out.println("Number of returned rows for the search: " + rp.getNumOfGridResults());
    }

    @DataProvider
    public Object[][] getData() {

        ExcelReaderUtil excel = new ExcelReaderUtil("./src/test/resources/testdata/TestData.xlsx");

        int rows = excel.getRowCount(DATA_SHEET);
        int columns = excel.getColumnCount(DATA_SHEET);
        System.out.println("Total rows are : " + rows);
        System.out.println("Total Columns are : " + columns);

        String testName = "LoginTest";

        // Find the test case start row
        int testCaseRowNum;
        for ( testCaseRowNum = 1; testCaseRowNum <= rows; testCaseRowNum++) {
            String testCaseName = excel.getCellData(DATA_SHEET, 0, testCaseRowNum);
            if (testCaseName.equalsIgnoreCase(testName)) break;
        }
        System.out.println("Test case starts from row num: " + testCaseRowNum);

        // Checking total rows in test case with testdata
        int dataStartRowNum = testCaseRowNum + 2;
        int testRows = 0;
        while (!excel.getCellData(DATA_SHEET, 0, dataStartRowNum + testRows).equals(""))  testRows++;
        System.out.println("Total rows of data are : " + testRows);

        // Checking total cols in test case
        int colStartColNum = testCaseRowNum + 1;
        int testCols = 0;
        while (!excel.getCellData(DATA_SHEET, testCols, colStartColNum).equals(""))   testCols++;
        System.out.println("Total columns are : " + testCols);

        // Getting/returning data
        Object[][] data = new Object[testRows][1];

        int i = 0;
        for (int rNum = dataStartRowNum; rNum < (dataStartRowNum + testRows); rNum++) {

            Hashtable<String, String> table = new Hashtable<>();

            for (int cNum = 0; cNum < testCols; cNum++) {

                //System.out.println(excel.getCellData(DATA_SHEET, cNum, rNum));
                String testData = excel.getCellData(DATA_SHEET, cNum, rNum);
                String colName = excel.getCellData(DATA_SHEET, cNum, colStartColNum);
                table.put(colName, testData);
            }

            data[i][0] = table;
            i++;
        }
        return data;
    }
}
