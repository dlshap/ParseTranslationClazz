package properties

import excelfilemanagement.ExcelFile
import org.apache.poi.openxml4j.util.ZipSecureFile
import org.apache.poi.ss.usermodel.Sheet

class ExcelPropertyFile extends ExcelFile {

    ExcelPropertyFile() {
    }

    ExcelPropertyFile(String fileName, CreateFlag createFlag) {
        super(fileName, createFlag)
    }

    static openFileUsingChooser(prompt, filePath) {
        ExcelPropertyFile excelPropertyFile = new ExcelPropertyFile()
        excelPropertyFile.openExcelFileUsingChooser(prompt, filePath)
        excelPropertyFile.fileName == null ? null : excelPropertyFile
    }

    static ExcelPropertyFile createNewFileFromFileName(String fileName, CreateFlag createFlag) {
        ExcelPropertyFile excelPropertyFile = new ExcelPropertyFile(fileName, createFlag)
        excelPropertyFile
    }

    static openFileUsingFileName(String fileName) {
        ZipSecureFile.setMinInflateRatio(0);                    // avoid ZipBomb problem
        ExcelPropertyFile excelPropertyFile = new ExcelPropertyFile()
        excelPropertyFile.openExcelFileUsingFileName(fileName)
        excelPropertyFile.file.exists() ? excelPropertyFile : null
    }

    ExcelPropertySheet createNewExcelPropertySheetFromModel(ExcelPropertySheet modelPropertySheet) {
        ExcelPropertySheet.createExcelPropertySheetInWorkbookFromModelSheet(this.workbook, modelPropertySheet)
    }

    def getPropertySheetWithHeaderLabelsInHeaderRow(String sheetName, int headerRowNum) {
        Sheet sheet = this.workbook.getSheet(sheetName)
        ExcelPropertySheet.createExcelPropertySheetFromAWorkbookSheet(this.workbook, sheet, headerRowNum)
    }

    ExcelPropertySheet nextExcelPropertySheet() {
        Sheet sheet = sheetIterator.next()
        ExcelPropertySheet.createExcelPropertySheetFromAWorkbookSheet(this.workbook, sheet, 0)
    }

    ExcelPropertySheet getExcelPropertySheet(String sheetName) {
        ExcelPropertySheet excelPropertySheet
        Sheet sheet = getSheet(sheetName)
        if (sheet != null) {
            excelPropertySheet = ExcelPropertySheet.createExcelPropertySheetFromAWorkbookSheet(this.workbook, sheet, 0)
        }
        excelPropertySheet
    }
}