package excelfilemanagement

import org.apache.poi.ss.usermodel.CellStyle
import org.apache.poi.ss.usermodel.Row

class ExcelRow {

    Row row
    Iterator cellIterator
    CellStyle style

    def reset() {
        cellIterator = row.cellIterator()
    }

    def hasNextCell() {
        cellIterator.hasNext()
    }

    def nextCell() {
        cellIterator.next()
    }
}
