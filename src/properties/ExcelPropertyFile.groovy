package properties

import excelfilemanagement.ExcelFile
import org.apache.poi.ss.usermodel.Sheet

class ExcelPropertyFile extends ExcelFile {

    Iterator sheetIterator

    ExcelPropertyFile() {
    }

    ExcelPropertyFile(String fileName, CreateFlag createFlag) {
        super(fileName, createFlag)
    }

    static openUsingChooser(prompt, filePath) {
        ExcelPropertyFile excelPropertyFile = new ExcelPropertyFile()
        excelPropertyFile.chooseFile(prompt, filePath)
        excelPropertyFile.buildSheetIterator()
        excelPropertyFile.fileName == null ? null : excelPropertyFile
    }

    private buildSheetIterator() {
        if (this.fileName != null)
            this.sheetIterator = workbook.sheetIterator()
    }

    static createNewSpreadsheetFromFileName(String fileName, CreateFlag createFlag) {
        ExcelPropertyFile excelPropertyFile = new ExcelPropertyFile(fileName, createFlag)
        excelPropertyFile.initializeNewSpreadsheet()
        excelPropertyFile
    }

    def initializeNewSpreadsheet() {
        super.initializeNewSpreadsheet()
        buildSheetIterator()
    }

    def createNewExcelPropertySheetFromModel(ExcelPropertySheet modelPropertySheet) {
        ExcelPropertySheet.createExcelPropertySheetInWorkbookFromModelSheet(this.workbook, modelPropertySheet)
    }

    def getPropertySheet(String sheetName) {
        this.getPropertySheetWithHeaderLabelsInHeaderRowNum(sheetName, 0)     // usually header in row 0
    }

    def getPropertySheetWithHeaderLabelsInHeaderRowNum(String sheetName, int headerRowNum) {
        ExcelPropertySheet.createExcelPropertySheetFromSheetInExcelPropertyFile(this, sheetName, headerRowNum)
    }

    def hasNextExcelPropertySheet() {
        sheetIterator.hasNext()
    }

    def nextExcelPropertySheet() {
        Sheet sheet = sheetIterator.next()
        ExcelPropertySheet.createExcelPropertySheetFromExcelSheet(sheet)
    }
}