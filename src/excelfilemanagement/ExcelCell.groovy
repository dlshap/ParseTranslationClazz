package excelfilemanagement

import org.apache.poi.ss.usermodel.Cell

class ExcelCell {

    Cell cell

    ExcelCell(Cell cell) {
        this.cell = cell
    }

    String toString() {
        switch (cell.getCellType()) {
            case "NUMERIC":
                cell.getNumericCellValue().toString()
                break
            case "STRING":
            case "BLANK":
                cell.getStringCellValue()
                break
            case "BOOLEAN":
                cell.getBooleanCellValue().toString()
                break
            default:
                ""
        }
    }
}
