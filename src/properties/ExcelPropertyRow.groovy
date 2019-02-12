package properties

import excelfilemanagement.ExcelRow
import org.apache.poi.ss.usermodel.Cell
import org.apache.poi.ss.usermodel.Row

class ExcelPropertyRow extends ExcelRow {

    def keyList = [:]

    ExcelPropertyRow(Row row, keyList) {
        this.row = row
        this.keyList = keyList
    }

    def getPropertyValueMap() {
        def keyMap = ["row": row.rowNum + 1]
        row.cellIterator().each { Cell cell ->
            def colNum = cell.getColumnIndex()
            if (colNum < keyList.size()) {
                switch (cell.getCellType()) {
                    case "NUMERIC":
                        def value = cell.getNumericCellValue()
                        if (value == value.toInteger())
                            value = value.toInteger()
                        keyMap.put(keyList[colNum], value.toString())
                        break;
                    case "STRING":
                    case "BLANK":
                        keyMap.put(keyList[colNum], cell.getStringCellValue().trim())
                        break;
                    default:
                        keyMap.put(keyList[colNum], "")
                }
            }
        }
        keyMap
    }

}
