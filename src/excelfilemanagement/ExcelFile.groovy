package excelfilemanagement

import filemanagement.FileChooser
import org.apache.poi.ss.usermodel.Workbook
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


class ExcelFile {

    def filePath
    def fileName
    Workbook workbook

    ExcelFile(filePath) {
        this.setFilePath(filePath)
    }

    def setFilePath(filePath) {
        this.filePath = filePath
        if (this.filePath[-1] != "\\")
            this.filePath += "\\"
    }

    static getPropertiesExcelFile(filePath, fileName) {
        def propertiesExcelFile = new ExcelFile(filePath)
        propertiesExcelFile.fileName = fileName
        propertiesExcelFile.openExcelFile()
        propertiesExcelFile
    }

    static getPropertiesExcelFileUsingChooser(filePath, componentName) {
        def propertiesExcelFile = new ExcelFile(filePath)
        propertiesExcelFile.openExcelFileUsingChooser(componentName)
        propertiesExcelFile
    }

    def openExcelFileUsingChooser(componentName) {
        def chooseFileName = FileChooser.chooseFile("Select translation spreadsheet for $componentName", this.filePath)
        if (chooseFileName != null) {
            def excelFile = new File(chooseFileName)
            this.filePath = excelFile.getParent() + "\\"
            this.fileName = excelFile.name
            openExcelFile()
        }
    }

    def openExcelFile() {
        def file = new File(filePath + fileName)
        def inputStream = new FileInputStream(file)
        this.workbook = new XSSFWorkbook(inputStream)
    }

    def isNull() {
        workbook == null
    }
}
