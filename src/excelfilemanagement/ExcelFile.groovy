package excelfilemanagement

import filemanagement.BaseFile

class ExcelFile extends BaseFile {

    ExcelWorkbook excelWorkbook

    ExcelFile() {
    }

    ExcelFile(fileName, createFlag) {
        super(fileName, createFlag)
        this.createOutputWorkbookFromBaseFile()
    }

    static openSpreadsheetUsingChooser(prompt, filePath) {
        ExcelFile excelFile = new ExcelFile()
        excelFile.chooseFile(prompt, filePath)
    }

    static createNewSpreadsheetFromFileName(fileName, createFlag) {
        ExcelFile excelFile = new ExcelFile(fileName, createFlag)
        excelFile.createOutputWorkbookFromBaseFile()
        excelFile
    }

    def chooseFile(prompt, filePath) {
        setFileUsingChooser(prompt, filePath)
        setInputWorkbookFromBaseFile()
        file == null ? null : this
    }

    private createOutputWorkbookFromBaseFile() {
        excelWorkbook = new ExcelWorkbookForOutput(file)
    }

    def setInputWorkbookFromBaseFile() {
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
//
//    def getSheet(sheetName) {
//        workbook.getSheet(sheetName)
//    }
}