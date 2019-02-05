import excelfilemanagement.ExcelPropertySheet
import org.apache.poi.hssf.usermodel.HSSFRow
import org.apache.poi.hssf.usermodel.HSSFSheet
import org.apache.poi.hssf.usermodel.HSSFWorkbook
import org.apache.poi.ss.usermodel.Cell
import org.apache.poi.ss.usermodel.Row
import org.apache.poi.ss.usermodel.Sheet
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
//        def testFile = ExcelFile.getPropertiesExcelFileUsingChooser(filePath, "DMT")
        ExcelFile testFile = ExcelFile.getPropertiesExcelFile(filePath, "DMT message translations-Eng-Spa-Jap.xlsx")
        ExcelPropertySheet excelPropertySheet = new ExcelPropertySheet(testFile, "DMT")
//        println excelPropertySheet.keyList
//        Row testRow = excelPropertySheet.sheet.getRow(1)
//        def keyMap = excelPropertySheet.getPropertyMapFromRow(testRow)
//        println keyMap
        while (excelPropertySheet.hasNextRow()) {
            def keyMap = excelPropertySheet.getPropertyMapFromRow(excelPropertySheet.nextRow())
            println keyMap
        }
    }


    def getKeyMaps(testFile) {
        def translationList = []
        Workbook workbook = testFile.workbook
        Sheet sheet = workbook.getSheet("DMT")
        Row firstRow = sheet.getRow(0)
        def keyValues = firstRow.cellIterator().collect() { it.getStringCellValue() }
        sheet.rowIterator().eachWithIndex { Row row, int rowNum ->
            def keyMap = [:]
            if (rowNum != 0) {
                row.cellIterator().eachWithIndex { Cell cell, int colNum ->
                    if (colNum < keyValues.size()) {
                        switch (cell.getCellType()) {
                            case "NUMERIC":
                                keyMap.put(keyValues[colNum], cell.getNumericCellValue().toInteger())
                                break;
                            case "STRING":
                                keyMap.put(keyValues[colNum], cell.getStringCellValue())
                                break;
                            default:
                                keyMap.put(keyValues[colNum], "")
                        }
                    }
                }
                translationList << keyMap
            }
        }
        println translationList
    }
}
