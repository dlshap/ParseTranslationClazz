package excelfilemanagement

import filemanagement.BaseFile
import filemanagement.FileChooser
import org.apache.poi.hssf.usermodel.HSSFWorkbook
import org.apache.poi.ss.usermodel.Workbook
import org.apache.poi.xssf.usermodel.XSSFWorkbook

class ExcelPropertyFile extends BaseFile {

    private filePath
    Workbook workbook

    ExcelPropertyFile(filePath) {
        super()
        this.setFilePath(filePath)
    }

    private setFilePath(filePath) {
        this.filePath = filePath
        if (this.filePath[-1] != "\\")
            this.filePath += "\\"
    }

    static getPropertiesExcelFileUsingChooser(filePath, prompt, componentName) {
        def excelPropertyFile = new ExcelPropertyFile(filePath)
        excelPropertyFile.openExcelFileUsingChooser(prompt, componentName)
        excelPropertyFile
    }

    def openExcelFileUsingChooser(String prompt, componentName) {
        file = FileChooser.chooseFile(prompt, this.filePath)
        workbook = getWorkbookFromFile()
    }

    private getWorkbookFromFile() {
        if (file != null) {
            def inputStream = new FileInputStream(file)
            def fileName = file.name
            def fileExtension = fileName.substring(fileName.indexOf("."))
            if (fileExtension == ".xlsx")
                new XSSFWorkbook(inputStream)
            else
                new HSSFWorkbook(inputStream)
        }
    }

    static createPropertiesExcelFileFromFileNameAndPathName(filePath, fileName, BaseFile.createFlag createFlag) {
        def excelPropertyFile = new ExcelPropertyFile(filePath)
        excelPropertyFile.createExcelFileFromFileName(fileName, createFlag)
    }

    def createExcelFileFromFileName(fileName, BaseFile.createFlag createFlag) {
        try {
            openFile(filePath + fileName, createFlag)
        } catch (Exception e) {
            println e
        }
        workbook = getWorkbookFromFile()
    }

    def isNull() {
        workbook == null
    }

    def exists() {
        !(isNull())
    }

    def getPropertySheetWithHeaderLabelsInHeaderRowNum(String sheetName, int headerRowNum) {
        new ExcelPropertySheet(this, sheetName, headerRowNum)
    }

    def getPropertySheet(String sheetName) {
        this.getPropertySheetWithHeaderLabelsInHeaderRowNum(sheetName, 0)     // usually header in row 0
    }
}


//    static getPropertiesExcelFileFromFileAndPathNames(filePath, fileName) {
//        def excelPropertyFile = new ExcelPropertyFile(filePath)
//        excelPropertyFile.openExcelFileFromFileName(fileName)
//        excelPropertyFile
//    }
//
//
//    def openExcelFileFromFileName(fileName) {
//        openFile(this.filePath + fileName)
//        workbook = getWorkbookFromFile()
//    }

