import excelfilemanagement.ExcelPropertyFile
import excelfilemanagement.ExcelPropertyRow
import excelfilemanagement.ExcelPropertySheet
import exceptions.OverwriteFileException
import filemanagement.BaseFile
import org.apache.poi.hssf.usermodel.HSSFWorkbook
import org.apache.poi.ss.usermodel.Workbook
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import useful.Messages

class TryExcel {

    TryExcel() {
        start();
    }

    static main(args) {
        def tryExcel = new TryExcel();
    }

    def start() {

        def componentName = "DMT"

        def filePath = "C:\\Users\\s0041664\\Documents\\Projects\\DMT-DE\\Project Work\\Translations\\Spreadsheets\\PropertySpreadsheets\\DMTDE\\"

        def fileName = "test.xls"

        ExcelPropertyFile excelPropertyFile = ExcelPropertyFile.createExcelPropertyFileFromFileName(filePath + fileName, BaseFile.createFlag.CREATE)

        Workbook workbook = excelPropertyFile.workbook

        workbook.createSheet("Test")
        workbook.createSheet("Test2")
        excelPropertyFile.writeAndClose()
    }

//    def useExcelFile() {
//        def outputFileName = "DMT-DE Properties Translations(${propertyArgs.get("language")}).xlsx"
//        try {
//            def createFlag = propertyArgs.get("overwrite") == "yes" ? BaseFile.createFlag.CREATE : BaseFile.createFlag.CREATE_ONLY_IF_NO_EXISTING_FILE
//            outputPropertySpreadsheet = ExcelPropertyFile.createExcelPropertyFileFromFileName(outputPath, outputFileName, createFlag)
//        } catch (OverwriteFileException e) {
//            println "! ${e.toString()} !"
//        }
//    }
//
//    def getWorkBookFromFile(file) {
//        if (file != null) {
//            def fileName = file.name
//            def fileExtension = fileName.substring(fileName.indexOf("."))
//            if (fileExtension == ".xlsx")
//                new XSSFWorkbook()
//            else
//                new HSSFWorkbook()
//        }
//    }

//        ExcelPropertyFile excelFile = ExcelPropertyFile.getPropertiesExcelFileFromFileAndPathNames(filePath, "test.xlsx")

//        def chooserPrompt = Messages.getString("prompt.for.spreadsheet", "DMT")
//        ExcelPropertyFile excelPropertyFile = ExcelPropertyFile.getExcelPropertyFileUsingChooser(filePath, chooserPrompt, "DMT")

//        ExcelPropertySheet sheet = excelPropertyFile.getPropertySheet("DMT")
//
//        while (sheet.hasNextRow()) {
//            ExcelPropertyRow excelPropertyRow = sheet.nextRow()
//            def keyMap = excelPropertyRow.getPropertyMap()
//            println keyMap
//        }
}

