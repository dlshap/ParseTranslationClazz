package properties

import excelfilemanagement.ExcelFile
import excelfilemanagement.ExcelWorkbookForOutput
import filemanagement.BaseFile
import filemanagement.FileChooser

class ExcelPropertyFile extends ExcelFile {

    ExcelPropertyFile() {
    }

    static createFileUsingChooser(prompt, filePath) {
        ExcelPropertyFile excelPropertyFile = new ExcelPropertyFile()
        excelPropertyFile.chooseFile(prompt, filePath)
    }

    ExcelPropertyFile(file) {
        this.file = file
        this.setInputWorkbookFromFile()
    }

    static createNewExcelPropertyFileFromFileName(fileName, BaseFile.createFlag createFlag) {
        ExcelPropertyFile excelPropertyFile = new ExcelPropertyFile(fileName, createFlag)
        excelPropertyFile
    }

    ExcelPropertyFile(fileName, createFlag) {
        super(fileName, createFlag)
        this.buildPropertyFile()
    }

    private buildPropertyFile() {
        this.excelWorkbook = new ExcelWorkbookForOutput(file)
    }

    /*************** public methods ****************/

    def getPropertySheetWithHeaderLabelsInHeaderRowNum(String sheetName, int headerRowNum) {
        new ExcelPropertySheet(this, sheetName, headerRowNum)
    }

    def getPropertySheet(String sheetName) {
        this.getPropertySheetWithHeaderLabelsInHeaderRowNum(sheetName, 0)     // usually header in row 0
    }
}