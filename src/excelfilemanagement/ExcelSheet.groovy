package excelfilemanagement

import org.apache.poi.ss.usermodel.Cell
import org.apache.poi.ss.usermodel.Row
import org.apache.poi.ss.usermodel.Sheet
import org.apache.poi.ss.usermodel.Workbook

class ExcelSheet {

    Sheet sheet
    Workbook workbook
    Iterator rowIterator            // sheet rowIterator
    ExcelSheetProperties sheetProperties

    String getSheetName() {
        sheet.sheetName
    }

    int getHeaderRowNum() {
        sheetProperties.headerRowNum
    }

    ArrayList<Integer> getColumnWidths() {
//        Row row = sheet.getRow(headerRowNum)
        Row row = sheet.getRow(0)
        def columnWidths = row.cellIterator().collect() { Cell it ->
            sheet.getColumnWidth(it.getColumnIndex())
        }
        columnWidths
    }
}
