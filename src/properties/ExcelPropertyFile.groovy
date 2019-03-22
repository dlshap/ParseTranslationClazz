package properties

import excelfilemanagement.ExcelFile
import org.apache.poi.ss.usermodel.Sheet

class ExcelPropertyFile extends ExcelFile {

    ExcelPropertyFile() {
    }

    ExcelPropertyFile(String fileName, CreateFlag createFlag) {
        super(fileName, createFlag)
    }

    static openExcelPropertyFileUsingChooser(prompt, filePath) {
        ExcelPropertyFile excelPropertyFile = new ExcelPropertyFile()
        excelPropertyFile.openExcelFileUsingChooser(prompt, filePath)
        excelPropertyFile.buildSheetIterator()
        excelPropertyFile.fileName == null ? null : excelPropertyFile
    }

    static createNewSpreadsheetFromFileName(String fileName, CreateFlag createFlag) {
        ExcelPropertyFile excelPropertyFile = new ExcelPropertyFile(fileName, createFlag)
        excelPropertyFile.buildSheetIterator()
        excelPropertyFile
    }

    ExcelPropertySheet createNewExcelPropertySheetFromModel(ExcelPropertySheet modelPropertySheet) {
        ExcelPropertySheet.createExcelPropertySheetInWorkbookFromModelSheet(this.workbook, modelPropertySheet)
    }

    def getPropertySheetWithHeaderLabelsInHeaderRow(String sheetName, int headerRowNum) {
        Sheet sheet = this.workbook.getSheet(sheetName)
        ExcelPropertySheet.createExcelPropertySheetFromAWorkbookSheet(this.workbook, sheet, headerRowNum)
    }

    def nextExcelPropertySheet() {
        Sheet sheet = sheetIterator.next()
        ExcelPropertySheet.createExcelPropertySheetFromAWorkbookSheet(this.workbook, sheet, 0)
    }
}