import excelfilemanagement.ExcelFile
import filemanagement.BaseFile
import properties.ExcelPropertyFile
import useful.Args

import static excelfilemanagement.ExcelFile.createNewSpreadsheetFromFileName
import static filemanagement.BaseFile.CreateFlag.*

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

        def filePath = propertyArgs.get("path")

        def fileName = "test.xls"

        ExcelPropertyFile inSpreadsheet = ExcelPropertyFile.openExcelPropertyFileUsingChooser("Pick a file", filePath)
        ExcelPropertyFile outSpreadsheet = ExcelPropertyFile.createNewSpreadsheetFromFileName(filePath+fileName, CREATE) // create


//
//        Workbook inWorkbook = inSpreadsheet.excelWorkbook
//        Workbook outWorkbook = outSpreadsheet.excelWorkbook
//        Iterator inSheetIterator = inWorkbook.sheetIterator()
//        inSheetIterator.each { Sheet inSheet ->
//            Sheet outSheet = outWorkbook.createSheet(inSheet.sheetName)
//            Iterator rowIterator = inSheet.rowIterator()
//            rowIterator.eachWithIndex { Row inRow, int rowNum ->
//                Row outRow = outSheet.createRow(rowNum)
//                Iterator inCellIterator = inRow.cellIterator()
//                inCellIterator.eachWithIndex { Cell inCell, int cellNum ->
//                    Cell outCell = outRow.createCell(cellNum)
//                    outCell.setCellValue(inCell.toString())
//                }
//            }
//        }
//        outSpreadsheet.writeAndClose()
    }
}
