import org.apache.poi.ss.usermodel.Workbook
import excelfilemanagement.ExcelFile;

class TryExcel {

    TryExcel() {
        start();
    }

    static main(args) {
        def tryExcel = new TryExcel();
    }

    def start() {
        def filePath = "C:\\Users\\s0041664\\Documents\\Projects\\DMT-DE\\Project Work\\Translations\\TranslationSpreadsheets\\PropertySpreadsheets\\DMTDE"
        def testFile = ExcelFile.getPropertiesExcelFileUsingChooser(filePath, "DMT")
//        def testFile = ExcelFile.getPropertiesExcelFile(filePath, "DMT message translations-Eng-Spa-Jap.xlsx")

        Workbook workbook = testFile.workbook
        println "${testFile.filePath + testFile.fileName}"
        println "${workbook.numberOfSheets} sheets:"
        0.upto(workbook.numberOfSheets-1) {
            println workbook.getSheetAt(it).sheetName
        }
    }
}
