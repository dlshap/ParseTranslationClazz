package excelfilemanagement

import filemanagement.BaseFile

class ExcelFile extends BaseFile {

    ExcelWorkbook excelWorkbook

    ExcelFile() {
    }

    ExcelFile(fileName, createFlag) {
        super(fileName, createFlag)
        this.createNewWorkbook()
    }

    static createFileUsingChooser(prompt, filePath) {
        ExcelFile excelFile = new ExcelFile()
        excelFile.chooseFile(prompt, filePath)
    }

    def chooseFile(prompt, filePath) {
        setFileUsingChooser(prompt, filePath)
        setInputWorkbookFromFile()
        file == null ? null : this
    }

    static createNewSpreadsheetFromFileName(fileName, createFlag) {
        ExcelFile excelFile = new ExcelFile(fileName, createFlag)
        excelFile.createNewWorkbook()
        excelFile
    }

    private createNewWorkbook() {
        this.excelWorkbook = new ExcelWorkbookForOutput(file)
    }

    def setInputWorkbookFromFile() {
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

    def getExcelSheet(sheetName) {
        excelWorkbook.getExcelSheet(sheetName)
    }

}