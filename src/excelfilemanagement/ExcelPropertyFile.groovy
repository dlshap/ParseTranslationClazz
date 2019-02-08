package excelfilemanagement

import filemanagement.BaseFile
import filemanagement.FileChooser
import org.apache.poi.hssf.usermodel.HSSFWorkbook
import org.apache.poi.ss.usermodel.Workbook
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import useful.Messages;


class ExcelPropertyFile extends BaseFile {

    private filePath
    Workbook workbook


    ExcelPropertyFile(filePath) {
        super()
        this.setFilePath(filePath)
    }

    def setFilePath(filePath) {
        this.filePath = filePath
        if (this.filePath[-1] != "\\")
            this.filePath += "\\"
    }

    static getPropertiesExcelFileFromFileAndPathNames(filePath, fileName) {
        def excelPropertyFile = new ExcelPropertyFile(filePath)
        excelPropertyFile.openExcelFileFromFileName(fileName)
        excelPropertyFile
    }

    static getPropertiesExcelFileUsingChooser(filePath, prompt, componentName) {
        def excelPropertyFile = new ExcelPropertyFile(filePath)
        excelPropertyFile.openExcelFileUsingChooser(prompt, componentName)
        excelPropertyFile
    }

    static createPropertiesExcelFileFromFileAndPathNames(filePath, fileName) {
        def excelPropertyFile = new ExcelPropertyFile(filePath)
        try {
            excelPropertyFile.openFile(filePath + fileName, BaseFile.createFlag.CREATE_ONLY_IF_NO_EXISTING_FILE)
        } catch (Exception e) {
            println e
        }
    }

    def openExcelFileFromFileName(fileName) {
        openFile(this.filePath + fileName)
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

    def openExcelFileUsingChooser(String prompt, componentName) {
        file = FileChooser.chooseFile(prompt, this.filePath)
        workbook = getWorkbookFromFile()
    }

    def isNull() {
        workbook == null
    }

    def exists() {
        !(isNull())
    }

    def getPropertySheet(String sheetName, int headerRowNum) {
        new ExcelPropertySheet(this, sheetName, headerRowNum)
    }

    def getPropertySheet(String sheetName) {
        this.getPropertySheet(sheetName, 0)     // usually header in row 0
    }
}
