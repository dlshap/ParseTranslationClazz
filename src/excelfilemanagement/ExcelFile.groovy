package excelfilemanagement

import filemanagement.BaseFile
import filemanagement.FileChooser

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
        buildSheetIterator()
    }

    private createOutputWorkbookFromBaseFile() {
        excelWorkbook = new ExcelWorkbookForOutput(file)
    }

    def buildSheetIterator() {
        if (this.fileName != null)
            this.sheetIterator = workbook.sheetIterator()
    }

    def hasNextExcelPropertySheet() {
        sheetIterator.hasNext()
    }

    def openExcelFileUsingChooser(String prompt, String path) {
        this.chooseFile(prompt, path)
        this.setFileName()
        this.setInputWorkbook()
        this.buildSheetIterator()
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

    def writeAndClose() {
        excelWorkbook.write()
        excelWorkbook.close()
    }
}
