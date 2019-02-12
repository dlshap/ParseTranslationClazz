package excelfilemanagement

import filemanagement.BaseFile
import filemanagement.FileChooser
import org.apache.poi.hssf.usermodel.HSSFWorkbook
import org.apache.poi.ss.usermodel.Workbook
import org.apache.poi.xssf.usermodel.XSSFWorkbook

class ExcelPropertyFile extends BaseFile {

    //inherited: File file
    ExcelWorkbook excelWorkbook
    def workbookStream

    // use static factory, not constructor
    static getExcelPropertyFileUsingChooser(filePath, prompt) {
        ExcelPropertyFile excelPropertyFile = null
        def excelFile = FileChooser.chooseFile(prompt, filePath)
        if (excelFile != null) {
            excelPropertyFile = new ExcelPropertyFile(excelFile)
        }
        excelPropertyFile
    }

    ExcelPropertyFile(file) {
        this.loadExcelPropertyFile(file)
    }

    private loadExcelPropertyFile(file) {
        this.file = file
        this.excelWorkbook = new ExcelWorkbookForInput(file)
    }

    static createExcelPropertyFileFromFileName(fileName, BaseFile.createFlag createFlag) {
        ExcelPropertyFile excelPropertyFile = new ExcelPropertyFile(fileName, createFlag)
    }

    ExcelPropertyFile(fileName, createFlag) {
        super(fileName, createFlag)
        this.buildPropertyFile()
    }

    private buildPropertyFile() {
        this.excelWorkbook = new ExcelWorkbookForOutput(file)
    }

     /*************** public methods ****************/

    def getWorkbook() {
        this.excelWorkbook.workbook
    }

    def getPropertySheetWithHeaderLabelsInHeaderRowNum(String sheetName, int headerRowNum) {
        new ExcelPropertySheet(this, sheetName, headerRowNum)
    }

    def getPropertySheet(String sheetName) {
        this.getPropertySheetWithHeaderLabelsInHeaderRowNum(sheetName, 0)     // usually header in row 0
    }

    def writeAndClose() {
        excelWorkbook.write()
        excelWorkbook.close()
    }
}