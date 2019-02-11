import excelfilemanagement.ExcelPropertyFile
import excelfilemanagement.ExcelPropertyRow
import excelfilemanagement.ExcelPropertySheet
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

        def fileName = "test.xlsx"

        def excelFile = new File(filePath + fileName)
        def outputStream = new FileOutputStream(excelFile)
        Workbook workbook = getWorkBookFromFile(excelFile)
        workbook.createSheet("Test")
        workbook.write(outputStream)
        outputStream.close()
    }

    def getWorkBookFromFile(file) {
        if (file != null) {
            def fileName = file.name
            def fileExtension = fileName.substring(fileName.indexOf("."))
            if (fileExtension == ".xlsx")
                new XSSFWorkbook()
            else
                new HSSFWorkbook()
        }
    }

//        ExcelPropertyFile excelFile = ExcelPropertyFile.getPropertiesExcelFileFromFileAndPathNames(filePath, "test.xlsx")

//        def chooserPrompt = Messages.getString("prompt.for.spreadsheet", "DMT")
//        ExcelPropertyFile excelPropertyFile = ExcelPropertyFile.getPropertiesExcelFileUsingChooser(filePath, chooserPrompt, "DMT")

//        ExcelPropertySheet sheet = excelPropertyFile.getPropertySheet("DMT")
//
//        while (sheet.hasNextRow()) {
//            ExcelPropertyRow excelPropertyRow = sheet.nextRow()
//            def keyMap = excelPropertyRow.getPropertyMap()
//            println keyMap
//        }
}

