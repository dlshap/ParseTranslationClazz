package excelfilemanagement

import filemanagement.BaseFile
import filemanagement.FileChooser

class ExcelFile extends BaseFile {

    ExcelWorkbook excelWorkbook

    ExcelFile() {
    }

    ExcelFile(fileName, createFlag) {
        super(fileName, createFlag)
    }

    static createNewSpreadsheetFromFileName(fileName, createFlag) {
        ExcelFile excelFile = new ExcelFile(fileName, createFlag)
        excelFile.createOutputWorkbookFromBaseFile()
        excelFile
    }

    private createOutputWorkbookFromBaseFile() {
        excelWorkbook = new ExcelWorkbookForOutput(file)
    }

    def chooseFile(prompt, filePath) {
        setFileUsingChooser(prompt, filePath)
        setInputWorkbookFromBaseFile()
        file == null ? null : this
    }

    private setFileUsingChooser(prompt, filePath) {
        file = FileChooser.chooseFile(prompt, filePath)
    }

    private setInputWorkbookFromBaseFile() {
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

//    Todo: remove
//    static openSpreadsheetUsingChooser(prompt, filePath) {
//        ExcelFile excelFile = new ExcelFile()
//        excelFile.chooseFile(prompt, filePath)
//    }


}