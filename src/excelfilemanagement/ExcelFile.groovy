package excelfilemanagement

import filemanagement.FileChooser
import org.apache.poi.ss.usermodel.Workbook
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


class ExcelFile {

    private filePath
    File file
    Workbook workbook

    ExcelFile(filePath) {
        this.setFilePath(filePath)
    }

    def setFilePath(filePath) {
        this.filePath = filePath
        if (this.filePath[-1] != "\\")
            this.filePath += "\\"
    }

    static getPropertiesExcelFileFromFileAndPathNames(filePath, fileName) {
        def propertiesExcelFile = new ExcelFile(filePath)
        propertiesExcelFile.openExcelFileFromFileName(fileName)
        propertiesExcelFile
    }

    static getPropertiesExcelFileUsingChooser(filePath, componentName) {
        def propertiesExcelFile = new ExcelFile(filePath)
        propertiesExcelFile.openExcelFileUsingChooser(componentName)
        propertiesExcelFile
    }

    def openExcelFileFromFileName(fileName) {
        file = new File(this.filePath + fileName)
        workbook = getWorkbookFromExcelFile()
    }

    def getWorkbookFromExcelFile() {
        if (file != null) {
            def inputStream = new FileInputStream(file)
            new XSSFWorkbook(inputStream)
        }
    }

    def openExcelFileUsingChooser(componentName) {
        file = FileChooser.chooseFile("Select translation spreadsheet for $componentName", this.filePath)
        workbook = getWorkbookFromExcelFile()
    }

    def isNull() {
        workbook == null
    }

}
