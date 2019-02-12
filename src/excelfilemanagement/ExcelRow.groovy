package excelfilemanagement

import org.apache.poi.ss.usermodel.CellStyle
import org.apache.poi.ss.usermodel.Row

class ExcelRow {

    Row row
    Iterator cellIterator
    CellStyle style

    ExcelRow(Row row) {
        this.row = row
        this.cellIterator = row.cellIterator()
    }

    def reset() {
        cellIterator = row.cellIterator()
    }

    def hasNextCell() {
        cellIterator.hasNext()
    }

    def nextCell() {
        new ExcelCell(cellIterator.next())
    }

    def collectCellsAsStrings() {
        row.cellIterator().collect() { cell -> new ExcelCell(cell).toString()}
    }
}
