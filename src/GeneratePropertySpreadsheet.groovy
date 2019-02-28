import excelfilemanagement.ExcelFile
import org.apache.poi.ss.usermodel.Cell
import org.apache.poi.ss.usermodel.Row
import org.apache.poi.ss.usermodel.Sheet
import org.apache.poi.ss.usermodel.Workbook
import filemanagement.BaseFile
import useful.Args

class GeneratePropertySpreadsheet {

    static final SPREADSHEET_PROMPT = "prompt.for.master.spreadsheet.for.component"

    Args propertyArgs

    GeneratePropertySpreadsheet(args) {
        start(args)
    }

    static main(args) {
        new GeneratePropertySpreadsheet(args)
    }

    def start(args) {
        propertyArgs = new Args(args)
        setDefaultArgs()
        generateSpreadsheet()
    }

    def setDefaultArgs() {
        if (propertyArgs.get("language") == null)
            propertyArgs.set("language", "Japanese")

        if (propertyArgs.get("path") == null)
            propertyArgs.set("path", "C:\\Users\\s0041664\\Documents\\Projects\\DMT-DE\\Project\\Work\\translations\\Spreadsheets\\PropertySpreadsheets\\DMTDE\\")
    }

    def generateSpreadsheet() {
        def componentName = "DMT"

        def filePath = "C:\\Users\\s0041664\\Documents\\Projects\\DMT-DE\\Project Work\\Translations\\Spreadsheets\\PropertySpreadsheets\\DMTDE\\"

        def fileName = "test.xls"

        ExcelFile inSpreadsheet = ExcelFile.openSpreadsheetUsingChooser("Pick a file", filePath)
        ExcelFile outSpreadsheet = ExcelFile.createNewSpreadsheetFromFileName(filePath+fileName, BaseFile.CreateFlag.CREATE) // create

        Workbook inWorkbook = inSpreadsheet.excelWorkbook
        Workbook outWorkbook = outSpreadsheet.excelWorkbook
        Iterator inSheetIterator = inWorkbook.sheetIterator()
        inSheetIterator.each { Sheet inSheet ->
            Sheet outSheet = outWorkbook.createSheet(inSheet.sheetName)
            Iterator rowIterator = inSheet.rowIterator()
            rowIterator.eachWithIndex { Row inRow, int rowNum ->
                Row outRow = outSheet.createRow(rowNum)
                Iterator inCellIterator = inRow.cellIterator()
                inCellIterator.eachWithIndex { Cell inCell, int cellNum ->
                    Cell outCell = outRow.createCell(cellNum)
                    outCell.setCellValue(inCell.toString())
                }
            }
        }
        outSpreadsheet.writeAndClose()
    }
}
