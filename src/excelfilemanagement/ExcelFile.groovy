package excelfilemanagement

import filemanagement.BaseFile

class ExcelFile extends BaseFile {

    ExcelWorkbook workbook

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
        workbook = new ExcelWorkbookForOutput(file)
    }

    def setInputWorkbookFromBaseFile() {
        if (file != null)
            workbook = new ExcelWorkbookForInput(file)
    }

    def getWorkbook() {
        workbook.workbook
    }

    def writeAndClose() {
        workbook.write()
        workbook.close()
    }
//
//    def getSheet(sheetName) {
//        workbook.getSheet(sheetName)
//    }
}