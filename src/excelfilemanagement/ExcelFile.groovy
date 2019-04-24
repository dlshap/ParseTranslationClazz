package excelfilemanagement

import filemanagement.BaseFile
import org.apache.poi.ss.usermodel.Sheet

class ExcelFile extends BaseFile {

    ExcelWorkbook excelWorkbook
    Iterator sheetIterator

    ExcelFile() {
    }

    ExcelFile(fileName, createFlag) {
        super(fileName, createFlag)
        this.initializeNewSpreadsheet()
    }

    def initializeNewSpreadsheet() {
        createOutputWorkbookFromBaseFile()
        resetSheetIterator()
    }

    private createOutputWorkbookFromBaseFile() {
        excelWorkbook = new ExcelWorkbookForOutput(file)
    }

    def resetSheetIterator() {
        if (this.fileName != null)
            this.sheetIterator = workbook.sheetIterator()
    }

    Sheet getSheet(String sheetName) {
        excelWorkbook.workbook.getSheet(sheetName)
    }

    def hasNextExcelPropertySheet() {
        sheetIterator.hasNext()
    }

    def openExcelFileUsingChooser(String prompt, String path) {
        this.chooseFile(prompt, path)
        this.setupFile()
    }

    def setupFile() {
        this.setFileName()
        this.setInputWorkbook()
        this.resetSheetIterator()
    }

    def openExcelFileUsingFileName(String fileName) {
        super.openFile(fileName)
        if (this.file.exists())
            this.setupFile()
    }

    private setFileName() {
        if (file != null)
            fileName = file.getName()
    }

    private setInputWorkbook() {
        if (file != null)
            excelWorkbook = new ExcelWorkbookForInput(file)
    }

    def getWorkbook() {
        excelWorkbook.workbook
    }

    def close() {
        excelWorkbook.close()
    }

    def write() {
        excelWorkbook.write()
    }

    def writeAndClose() {
        excelWorkbook.write()
        excelWorkbook.close()
    }
}
