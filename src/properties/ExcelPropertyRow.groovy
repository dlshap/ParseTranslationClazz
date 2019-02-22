package properties

import excelfilemanagement.ExcelUtil
import org.apache.poi.ss.usermodel.Cell
import org.apache.poi.ss.usermodel.CellStyle
import org.apache.poi.ss.usermodel.Row

class ExcelPropertyRow {

    def keyList = [:]

    Row row
    Iterator cellIterator
    CellStyle style


    ExcelPropertyRow(Row row, keyList) {
        this.row = row
        this.keyList = keyList
    }

    def getPropertyValueMap() {
        def keyMap = ["row": row.rowNum + 1]
        row.cellIterator().each { Cell cell ->
            def colNum = cell.getColumnIndex()
            if (colNum < keyList.size()) {
                if (keyList[colNum] != "")
                    keyMap.put(keyList[colNum], ExcelUtil.toStringWithOnlyIntegerNumerics(cell))
            }
        }
        keyMap
    }
}
