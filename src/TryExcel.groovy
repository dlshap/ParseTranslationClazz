import excelfilemanagement.ExcelUtil
import org.apache.poi.ss.usermodel.Cell
import org.apache.poi.ss.usermodel.Row
import org.apache.poi.ss.usermodel.Sheet
import org.apache.poi.ss.usermodel.Workbook
import properties.ExcelPropertyFile

import static filemanagement.BaseFile.CreateFlag.CREATE


class TryExcel {

    TryExcel() {
        start();
    }

    static main(args) {
        new TryExcel();
    }

    def start() {

        def filePath = "C:\\Users\\s0041664\\Documents\\Projects\\DMT-DE\\Project Work\\Translations\\DMT\\doofus\\"

        def fileName = "test.xls"

        ExcelPropertyFile inSpreadsheet = ExcelPropertyFile.openExcelPropertyFileUsingChooser("Pick a file", filePath)
        ExcelPropertyFile outSpreadsheet = ExcelPropertyFile.createNewSpreadsheetFromFileName(filePath+fileName, CREATE) // create

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
                    outCell.setCellValue(ExcelUtil.toStringWithOnlyIntegerNumerics(inCell))
                }
            }
        }
        outSpreadsheet.writeAndClose()
    }
}


