package com.mck.rp.tetscases.SampleTests.PatientAnalysis;

import com.mck.rp.listeners.allure.AllureReportListener;
import com.mck.rp.pageObjects.LoginPage;
import io.qameta.allure.*;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.mck.rp.base.BaseTest;
import com.mck.rp.pageObjects.CommonPage;
import com.mck.rp.utilities.ElementUtil;
import org.testng.asserts.SoftAssert;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.concurrent.TimeUnit;


@Epic("Module- Regimen Analysis - Patient Analysis")
@Feature("RP-: Smoke Test for Patient Analysis Page")
@Listeners(AllureReportListener.class)
public class PatientAnalysisSmokeTest extends BaseTest {
    ElementUtil eu;
    CommonPage cp;
    LoginPage lp;
    String srhDrug = "alemtuzumab";

    @Severity(SeverityLevel.CRITICAL)
    @BeforeClass
    public void patAnalysisSetUp() {
        cp = new CommonPage(driver);
        lp = new LoginPage(driver);
        eu = new ElementUtil(driver);

        try {
            loginPage.doLogin(prop.getProperty("defaultUsername"), prop.getProperty("defaultPassword"));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    @Description("Patient Analysis - Patient Analysis - Validation of Use Defaults and Calculate and Sort results by Regimen Name and " +
            "Patient Responsibility using Columns Sort functionality")
    @Severity(SeverityLevel.NORMAL)
    @Test(priority = 1, groups = "smoke",description = "Patient Analysis - Patient Analysis - Validation of Use Defaults and Calculate and Sort Results by" +
            "Regimen Name and Patient Responsibility using Columns Sort functionality")
    public void patientAnalysisSearch() throws NoSuchElementException {
        SoftAssert sa = new SoftAssert();
        try {
            Assert.assertTrue(cp.isLogoutExist());
            cp.clickTabs("Patient Analysis");
            sa.assertEquals(cp.getGridHeading(), "Patient Information", "Incorrect page heading");

            eu.doClick(cp.useDefaults);
            cp.clickSubmit("Calculate");
            cp.srhRegDrugDiagAndEnter(srhDrug);

            if (cp.getNumOfGridResults() > 0) {
                sa.assertTrue(eu.isElementPresent(cp.patTable));
                int rowCount = eu.getGridRowCount(cp.patTable);
                for (int i = 0; i < rowCount; i++) {
                    //System.out.println("Cell Date: " + rp.getRowCellData(rp.patTable, i)[0]);
                    sa.assertTrue(cp.getRowCellData(cp.patTable, i)[0].toLowerCase().contains(srhDrug));
                }
                //ExtentReportListener.test.get().log(Status.INFO, "Number of records that match the given search criteria: " + rp.getNumOfGridResults());
                AllureReportListener.saveLogs("Number of records that match the given search criteria: " + cp.getNumOfGridResults());

                //Sort by Name
                List<String> beforeSortList = cp.tableColumnList(cp.patTable, 0);
                List<String> sortList = eu.sortItemsList(beforeSortList, "desc");
                cp.clickTableHeaderForSort("Regimen Name");
                eu.syncWait(3);
                List<String> afterSortList = cp.tableColumnList(cp.patTable, 0);
                //System.out.println("Before: " + beforeSortList + "\r\n" + "Sort: " + sortList + "\r\n" + "After: " + afterSortList);
                sa.assertEquals(sortList, afterSortList, "Sort by Name - Not working as expected");
                cp.clickTableHeaderForSort("Regimen Name");
                eu.syncWait(3);

                //Sort by Insurer Responsibility
                List<Integer> beforeSortNumsList = cp.tableNumColumnList(cp.patTable, 1);
                List<Integer> sortNumsList = eu.sortItemsNums(beforeSortNumsList, "desc");
                cp.clickTableHeaderForSort("Patient Responsibility");
                eu.syncWait(3);
                List<Integer> afterSortNumsList = cp.tableNumColumnList(cp.patTable, 1);
                //System.out.println("Before: " + beforeSortNumsList + "\r\n" + "Sort: " + sortNumsList + "\r\n" + "After: " + afterSortNumsList);
                sa.assertEquals(sortNumsList, afterSortNumsList, "Sort by Insurer Responsibility - Not working as expected");
                //eu.doClick(rp.gridSrhClear);
            } else {
                sa.fail();
                AllureReportListener.saveLogs("No Records exists in the table");
            }
        } catch (NoSuchElementException | InterruptedException e) {
            AllureReportListener.saveLogs("Test Method Failed!!");
        }
        sa.assertAll();
    }

    @Description("Patient Analysis - Patient Report - Regimen Library - Validation of Regimen Library results grid filtering by Drug and Diagnosis filters")
    @Severity(SeverityLevel.NORMAL)
    @Test(priority = 2,groups = "smoke", description = "Patient Analysis - Patient Report - Regimen Library - Validation of Regimen Library results grid filtering by Drug and Diagnosis filters")
    public void patAnalysisGridFilterByDrugAndDiagnosis() throws NoSuchElementException {
        //log.info("PatientAnalysis Page - Search the table grid");
        SoftAssert sa = new SoftAssert();
        try {
            Assert.assertTrue(cp.isLogoutExist());

            /*rp.clickTabs("Patient Analysis");
            sa.assertEquals(rp.getGridHeading(), "Patient Information");
            eu.doClick(rp.useDefaults);
            rp.clickSubmit("Calculate");
             */
            eu.doClick(cp.gridSrhClear);
            if (cp.getNumOfGridResults() > 0) {
                sa.assertTrue(eu.isElementPresent(cp.patTable));
                int beforeFilter = cp.getNumOfGridResults();

                //Diagnosis Filter
                cp.clickGridFilters("diagnosis-select", "Diagnosis");
                cp.selectFilterItemByName("Amyloidosis");
                if (!cp.getTextContent(cp.patTable).contains("search did not match any results")) {
                    //ExtentReportListener.test.get().log(Status.INFO, "Number of records that match the selected Diagnosis filter criteria: " + rp.getNumOfGridResults());
                    AllureReportListener.saveLogs("Number of records that match the selected Diagnosis filter criteria: " + cp.getNumOfGridResults());
                    int afterDiagFilter = cp.getNumOfGridResults();
                    sa.assertNotEquals(beforeFilter, afterDiagFilter, "Diagnosis Filter - Not working as expected");
                } else {
                    AllureReportListener.saveLogs("Diagnosis filter search did not return any results");
                }
                eu.clickWhenReady(cp.getFilterSelectClear("diagnosis-select"), 5);

                //Drugs Filter
                eu.syncWait(5);
                cp.clickGridFilters("drugs-select", "Drug");
                cp.selectFilterItemByIndex(1);
                if (!cp.getTextContent(cp.patTable).contains("search did not match any results")) {
                    AllureReportListener.saveLogs("Number of records that match the selected Drug filter criteria: " + cp.getNumOfGridResults());
                    int afterDrugFilter = cp.getNumOfGridResults();
                    sa.assertNotEquals(beforeFilter, afterDrugFilter, "drugs Filter - Not working as expected");
                } else {
                    AllureReportListener.saveLogs("Drug filter search did not return any results");
                }
                eu.clickWhenReady(cp.getFilterSelectClear("drugs-select"), 5);

                //pagination
                eu.syncWait(3);
                String beforePagination = eu.getElement(cp.gridResults).getText();
                cp.selectPaginationRows("50");
                eu.syncWait(3);
                String afterPagination = eu.getElement(cp.gridResults).getText();
                sa.assertNotEquals(beforePagination, afterPagination, "Pagination - Not working as expected");

            } else {
                sa.fail();
                AllureReportListener.saveLogs("No Records exists in the table");
            }
        } catch (InterruptedException e) {
            AllureReportListener.saveLogs("Test Method Failed!!");
        }
        sa.assertAll();
    }

    @Description("Patient Analysis - Patient Report - Regimen Summary - Validation of View And Edit Details functionality")
    @Severity(SeverityLevel.NORMAL)
    @Test(priority = 3, groups = "smoke",description = "Patient Analysis - Patient Report - Regimen Summary - Validation of View And Edit Details functionality")
    public void patAnalysisTableRowDetails() throws NoSuchElementException {
        SoftAssert sa = new SoftAssert();

        try {
            Assert.assertTrue(cp.isLogoutExist());
  /*          rp.clickTabs("Patient Analysis");
            sa.assertEquals(rp.getGridHeading(), "Patient Information");

            eu.doClick(rp.useDefaults);
            rp.clickSubmit("Calculate");*/
            //eu.doClick(rp.gridSrhClear);
            if (cp.getNumOfGridResults() > 0) {
                cp.srhRegDrugDiagAndEnter(srhDrug);
                AllureReportListener.saveLogs("Number of records that match the given search criteria: " + cp.getNumOfGridResults());

                //Rows are in odd numbers 1, 3, 5 etc, even numbers are the separators in the grid
                String gridRowText = eu.getText(cp.getGridcell("patient-analysis-table", 1, 1));
                cp.clickGridCell("patient-analysis-table", 1, 1);
                eu.scrollToView(cp.btnViewEditDts);
                eu.scrollToView(cp.btnViewEditDts);
                eu.doClick(cp.btnViewEditDts);
                String drugViewDts = eu.getTextcontent(cp.drugDetailsTitle);
                sa.assertEquals(gridRowText, drugViewDts.substring(0, drugViewDts.length() - 1), "Patient Analysis - Table value and Details page value are not matching");
                sa.assertTrue(eu.isElementPresent(cp.getBtnTypeButton("Add Lump Sum")));

            } else {
                sa.fail();
                AllureReportListener.saveLogs("No Records exists in the table");
            }
        } catch (NoSuchElementException e) {
            AllureReportListener.saveLogs("Test Method Failed!!");
        }
        sa.assertAll();
    }

    @Test(priority = 4, groups = "smoke",description = "Patient Analysis - Regimen Summary - Validation of the Patient Responsibility value when Antiemetics and Growth Factor dropdown" +
            " items are changed and Cycles value is changed." + "\r\n" + " Validation of Export functionality in Patient Report page" + "\r\n" + "Validation of Add Lump Sum functionality" + "\r\n" +
            " Validation of Drug and Non drug modal windows are opening when clicked on Add buttons ")
    @Severity(SeverityLevel.NORMAL)
    @Description("Patient Analysis - Regimen Summary - Validation of the Patient Responsibility value when Antiemetics and Growth Factor dropdown" +
            " items are changed and Cycles value is changed." + "\r\n" + " Validation of Export functionality in Patient Report page " + "\r\n" + "Validation of Add Lump Sum functionality" + "\r\n" +
            " Validation of Drug and Non drug modal windows are opening when clicked on Add buttons ")
    public void patAnalysisSummaryAntiGfactorsCycles() throws NoSuchElementException {
        SoftAssert sa = new SoftAssert();

        try {
            cp.clickByLinkText("Patient Analysis");
            sa.assertEquals(cp.getGridHeading(), "Patient Information", "Incorrect grid heading");

            eu.doClick(cp.useDefaults);
            cp.clickSubmit("Calculate");
            cp.srhRegDrugDiagAndEnter(srhDrug);

            if (cp.getNumOfGridResults() > 0) {
                //Export - Main Patient Report
                cp.clickButton("Export");
                cp.clickSubListItem("Export as PDF");

                AllureReportListener.saveLogs("Row Count: " + eu.getGridRowCount(cp.patTable) + " and " +
                        "Column Count " + eu.getGridColumnCount(cp.patTable));

                String regName, regLibPatRes;
                regName = cp.getRowCellData(cp.patTable, 0)[0];
                regLibPatRes = cp.getRowCellData(cp.patTable, 0)[1];

                //System.out.println("Row Detail Name: " + regName + " Patient Responsibility: " + regLibPatRes);

                //Rows are in odd numbers 1, 3, 5 etc, even numbers are the separators in the grid
                String gridRowText = eu.getText(cp.getGridcell("patient-analysis-table", 1, 1));
                //System.out.println("Row Text" + gridRowText);
                cp.clickGridCell("patient-analysis-table", 1, 1);
                eu.scrollToView(cp.btnViewEditDts);
                eu.scrollToView(cp.btnViewEditDts);
                eu.doClick(cp.btnViewEditDts);
                eu.syncWait(2);
                String drugViewDts = eu.getTextcontent(cp.drugDetailsTitle);
                sa.assertEquals(gridRowText, drugViewDts.substring(0, drugViewDts.length() - 1),
                        "Patient Analysis - Table value and (Click and Edit Details) details page value are not matching ");

                String antiemeticSelect = cp.summaryAntieGfactorSelectItem("antiemetic", 1);
                String antiemeticPatRes = cp.getTextContent(cp.patSummaryPatResp);
                if(eu.getTextcontent(cp.suppCareSummary).contains("Antiemetics are not required for this regimen")) {
                    AllureReportListener.saveLogs("Antimetics not required for this");
                }
                else{
                    //System.out.println("Anti: " + antiemeticPatRes);
                    sa.assertNotEquals(antiemeticPatRes, regLibPatRes, "Antiemetics - Patient Responsibility not changing after filter change");
                    //System.out.println("AntiName1: " + rp.getTextContent(rp.summaryDrugTable).toLowerCase());
                    //System.out.println("AntiName1: "+ rp.subString(antiemeticSelect, " ", 0).toLowerCase());
                    sa.assertTrue(cp.getTextContent(cp.summaryDrugTable).toLowerCase().contains(cp.subString(antiemeticSelect, " ", 0).toLowerCase()));

                }



                String growthFactorSelect = cp.summaryAntieGfactorSelectItem("growthFactor", 1);
                String growthFacPatRes = cp.getTextContent(cp.patSummaryPatResp);
                //System.out.println("GF: " + growthFacPatRes);
                sa.assertNotEquals(growthFacPatRes, regLibPatRes, "Growth Factor - Patient Responsibility not changing after GF filter value change ");
                sa.assertTrue(cp.getTextContent(cp.summaryDrugTable).toLowerCase().contains(cp.subString(growthFactorSelect, " ", 0).toLowerCase()));

                cp.sendKeysByAction("numberOfCycles", "10");
                eu.getElement(cp.getInputField("numberOfCycles")).sendKeys(Keys.TAB);
                TimeUnit.SECONDS.sleep(2);
                String cyclesPatRes = cp.getTextContent(cp.patSummaryPatResp);
                //System.out.println("Cyc: " + cyclesPatRes);
                sa.assertNotEquals(cyclesPatRes, regLibPatRes, "Number of Cycles - Patient Responsibility not changing after NOCycles change");

                cp.clickButton("Add Lump Sum");
                String amount = String.format("%.2f", (new Random().nextInt(99) / new Random().nextDouble()));
                cp.sendKeysByAction("fundings[0].name", "Funding");
                cp.sendKeysByAction("fundings[0].amount", amount);
                eu.getElement(cp.getInputField("fundings[0].amount")).sendKeys(Keys.TAB);
                TimeUnit.SECONDS.sleep(2);
                Double funding = Double.parseDouble(amount);
                NumberFormat n = NumberFormat.getCurrencyInstance(Locale.US);
                sa.assertTrue(cp.getTextContent(cp.additionalFunding).contains(n.format(funding)));


                //Export - Summary Page
                cp.clickButton("Export");
                cp.clickSubListItem("Detailed Patient Report (.pdf)");
                eu.syncWait(5);

                //Export - Summary Page
                cp.clickButton("Export");
                cp.clickSubListItem("Summary Patient Report (.pdf)");

                //Add Drug and Non Drug
                eu.scrollToView(cp.summaryAddDrug);
                eu.clickWhenReady(cp.summaryAddDrug, 6);
                cp.clickSubListItem("Add Drug");
                sa.assertTrue(cp.getTextContent(cp.addDrugNondrugDialogH2).contains("Add Drug"));
                if (cp.getTextContent(cp.addDrugNondrugDialogH2).contains("Add Drug")) {
                    cp.clickButton("Cancel");
                }

                eu.scrollToView(cp.summaryAddNondrug);
                eu.clickWhenReady(cp.summaryAddNondrug, 6);
                sa.assertTrue(cp.getTextContent(cp.addDrugNondrugDialogH2).contains("Add Non-Drug"));
                if (cp.getTextContent(cp.addDrugNondrugDialogH2).contains("Add Non-Drug")) {
                    cp.clickButton("Cancel");
                }
            } else {
                sa.fail();
                AllureReportListener.saveLogs("No Records exists in the table");
            }
        } catch (InterruptedException | StaleElementReferenceException e) {
            AllureReportListener.saveLogs("Test method Failed");
        }
        sa.assertAll();
    }
    @Test(priority = 5, groups = "smoke",description = "Patient Analysis - Patient Information - Validation of Primary Insurance - CoInsurance, BSA, Insurace Fee Schedule, " +
            "Out Of Pocket, Deductible, CoPays - Amount and cycles are working with different values selection." + "\r\n" +
            "Validation of Secondary Insurance - Secondary Fee Schedule, Coinsurnace, Out Of Pocket, Deductible and Secondary pays Primary Deductible are " +
            "working with different values.")
    @Severity(SeverityLevel.NORMAL)
    @Description("Patient Analysis - Patient Information - Validation of Primary Insurance - CoInsurance, BSA, Insurace Fee Schedule," +
            "Out Of Pocket, Deductible, CoPays - Amount and cycles are working with different values selection." + "\r\n" +
            "Validation of Secondary Insurance - Secondary Fee Schedule, Coinsurnace, Out Of Pocket, Deductible and Secondary pays Primary Deductible are " +
            "working with different values")
    public void patAnalysisPatientInfoFilters() throws NoSuchElementException {
        SoftAssert sa = new SoftAssert();
        try {
            cp.clickByLinkText("Patient Analysis");
            sa.assertEquals(cp.getGridHeading(), "Patient Information", "incorrect Page heading");

            eu.doClick(cp.useDefaults);
            cp.clickSubmit("Calculate");
            cp.srhRegDrugDiagAndEnter(srhDrug);

            if (cp.getNumOfGridResults() > 0) {
                AllureReportListener.saveLogs("Number of rows returned for the search - " + srhDrug + " : " + eu.getGridRowCount(cp.patTable));

                String beforeSrhRowValues = cp.getGridRowData(cp.patTable, 1);

                //Changing the Fee Schedule and validating
                cp.clickImg(cp.patientInformationCollapse);
                cp.analysisCriAntieGfactorSelectItem("primaryInsurance.feeSchedule", 1);
                cp.clickSubmit("Calculate");
                String afterFeeScheValues = cp.getGridRowData(cp.patTable, 1);
                sa.assertNotEquals(beforeSrhRowValues, afterFeeScheValues, "Primary Insurance Fee Schedule - Not working as expected");

                //Changing CoInsurance
                cp.clickImg(cp.patientInformationCollapse);

                cp.sendKeysByAction("primaryInsurance.coinsurance", String.valueOf(new Random().nextInt(40)));
                cp.clickSubmit("Calculate");
                String afterCoInsValues = cp.getGridRowData(cp.patTable, 1);
                sa.assertNotEquals(afterFeeScheValues, afterCoInsValues, "Primary Insurance CoInsurance - NOt working as expected");

                //Validating with providing Deductible amount
                cp.clickImg(cp.patientInformationCollapse);
                cp.sendKeysByAction("primaryInsurance.remainingDeductible.remaining", String.valueOf(new Random().nextInt(500)));
                cp.clickSubmit("Calculate");
                String afterDeductibleValues = cp.getGridRowData(cp.patTable, 1);
                sa.assertNotEquals(afterCoInsValues, afterDeductibleValues, "Primary Insurance Deductible - Not working as expected ");

                //Validating with providing OutOfPocket amount
                cp.clickImg(cp.patientInformationCollapse);
                cp.sendKeysByAction("primaryInsurance.remainingDeductible.remaining", "");
                cp.sendKeysByAction("primaryInsurance.remainingOutOfPocket.remaining", String.valueOf(new Random().nextInt(400)));
                cp.clickSubmit("Calculate");
                String afterOutOfPocValues = cp.getGridRowData(cp.patTable, 1);
                sa.assertNotEquals(afterDeductibleValues, afterOutOfPocValues, "Primary Insurance Out of Pocket - Not working as expected");

                //Validating with providing CoPay and Cycles values
                cp.clickImg(cp.patientInformationCollapse);
                cp.sendKeysByAction("primaryInsurance.remainingOutOfPocket.remaining", "");
                cp.sendKeysByAction("primaryInsurance.copay", String.valueOf(new Random().nextInt(200)));
                cp.sendKeysByAction("primaryInsurance.copaysPerCycle", String.valueOf(new Random().nextInt(10)));
                cp.clickSubmit("Calculate");
                String afterCoPayCycValues = cp.getGridRowData(cp.patTable, 1);
                sa.assertNotEquals(afterOutOfPocValues, afterCoPayCycValues, "Primary Insurance Copay and Cycles - Not working as expected");

                cp.clickImg(cp.patientInformationCollapse);
                cp.clickButton("Secondary insurance");
                eu.scrollToView(cp.getBtnTypeSubmit("Calculate"));
                cp.analysisCriAntieGfactorSelectItem("secondaryInsurance.feeSchedule", 1);
                cp.sendKeysByAction("secondaryInsurance.coinsurance", String.valueOf(new Random().nextInt(30)));
                cp.clickSubmit("Calculate");
                String afterSecondaryFeeScheCoInsValues = cp.getGridRowData(cp.patTable, 1);
                sa.assertNotEquals(afterCoPayCycValues, afterSecondaryFeeScheCoInsValues, "Secondary Insurance CoInsurance - NOt working as expected");

                //Validating with providing Deductible amount
                cp.clickImg(cp.patientInformationCollapse);
                eu.scrollToView(cp.getBtnTypeSubmit("Calculate"));
                cp.sendKeysByAction("secondaryInsurance.remainingDeductible.remaining", String.valueOf(new Random().nextInt(350)));
                cp.clickSubmit("Calculate");
                String afterSecondaryDeductibleValues = cp.getGridRowData(cp.patTable, 1);
                sa.assertNotEquals(afterSecondaryFeeScheCoInsValues, afterSecondaryDeductibleValues, "Secodary Insurance Deductible - Not working as expected");

                //Validating with providing OutOfPocket amount
                cp.clickImg(cp.patientInformationCollapse);
                eu.scrollToView(cp.getBtnTypeSubmit("Calculate"));
                cp.sendKeysByAction("secondaryInsurance.remainingDeductible.remaining", "");
                cp.sendKeysByAction("secondaryInsurance.remainingOutOfPocket.remaining", String.valueOf(new Random().nextInt(200)));
                cp.clickSubmit("Calculate");
                String afterSecondaryOutOfPocValues = cp.getGridRowData(cp.patTable, 1);
                sa.assertNotEquals(afterSecondaryDeductibleValues, afterSecondaryOutOfPocValues, "Secondary Insurance Out of Pocket - Not working as expected");

                cp.clickImg(cp.patientInformationCollapse);
                eu.scrollToView(cp.getBtnTypeSubmit("Calculate"));
                //eu.scrollByPixel(180);
                cp.sendKeysByAction("secondaryInsurance.remainingOutOfPocket.remaining", "");
                cp.sendKeysByAction("primaryInsurance.remainingDeductible.remaining", String.valueOf(new Random().nextInt(400)));
                cp.sendKeysByAction("secondaryInsurance.remainingDeductible.remaining", String.valueOf(new Random().nextInt(250)));
                eu.doClick(cp.secondaryPaysPrimaryDeductible);
                cp.clickSubmit("Calculate");
                String afterSecondaryPayPrimaryDed = cp.getGridRowData(cp.patTable, 1);
                sa.assertNotEquals(afterSecondaryOutOfPocValues, afterSecondaryPayPrimaryDed, "Secondary Pay Primary Deductible - Not working as expected");

            } else {
                sa.fail();
                AllureReportListener.saveLogs("No Records exists in the table");
            }
        } catch (NoSuchElementException e) {
            e.printStackTrace();
            AllureReportListener.saveLogs("Test Method Failed");
        }
        sa.assertAll();
    }

}



