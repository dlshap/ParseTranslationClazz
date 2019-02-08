import excelfilemanagement.ExcelPropertyFile
import excelfilemanagement.ExcelPropertyRow
import excelfilemanagement.ExcelPropertySheet
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

//        ExcelPropertyFile excelFile = ExcelPropertyFile.getPropertiesExcelFileFromFileAndPathNames(filePath, "test.xlsx")

        def chooserPrompt = Messages.getString("prompt.for.spreadsheet", "DMT")

        ExcelPropertyFile excelPropertyFile = ExcelPropertyFile.getPropertiesExcelFileUsingChooser(filePath, chooserPrompt, "DMT")

        ExcelPropertySheet sheet = excelPropertyFile.getPropertySheet("DMT")

        while (sheet.hasNextRow()) {
            ExcelPropertyRow excelPropertyRow = sheet.nextRow()
            def keyMap = excelPropertyRow.getPropertyMap()
            println keyMap
        }
    }
}