package excelfilemanagement

import filemanagement.BaseFile
import filemanagement.FileChooser

class ExcelFile extends BaseFile {

    ExcelWorkbook excelWorkbook

    ExcelFile() {
    }

    ExcelFile(fileName, createFlag) {
        super(fileName, createFlag)
        this.initializeNewSpreadsheet()
    }

    static getExcelFileUsingChooser(prompt, path) {
        ExcelFile excelFile = new ExcelFile()
        excelFile.openExcelFileUsingChooser(prompt, path)
        excelFile.file == null ? null : excelFile
    }

    def openExcelFileUsingChooser(String prompt, String path) {
        this.chooseFile(prompt, path)
        this.setFileName()
        this.setInputWorkbook()
    }

    private setFileName() {
        if (file != null)
            fileName = file.getName()
    }

    private setInputWorkbook() {
        if (file != null)
            excelWorkbook = new ExcelWorkbookForInput(file)
    }

    static ExcelFile createFile(fileName, createFlag) {
        ExcelFile excelFile = new ExcelFile(fileName, createFlag)
        excelFile
    }

    def initializeNewSpreadsheet() {
        createOutputWorkbookFromBaseFile()
    }

    private createOutputWorkbookFromBaseFile() {
        excelWorkbook = new ExcelWorkbookForOutput(file)
    }

    def getWorkbook() {
        excelWorkbook.workbook
    }

    def writeAndClose() {
        excelWorkbook.write()
        excelWorkbook.close()
    }
}