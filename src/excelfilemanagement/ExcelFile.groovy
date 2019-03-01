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

//    ToDo: remove
//    static createNewSpreadsheetFromFileName(String fileName, CreateFlag createFlag) {
//        ExcelFile excelFile = new ExcelFile(fileName, createFlag)
//        excelFile.initializeNewSpreadsheet()
//        excelFile
//    }

    def initializeNewSpreadsheet() {
        createOutputWorkbookFromBaseFile()
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
}