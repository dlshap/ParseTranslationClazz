package properties

import excelfilemanagement.ExcelUtil
import org.apache.poi.ss.usermodel.Cell
import org.apache.poi.ss.usermodel.CellStyle
import org.apache.poi.ss.usermodel.Row

class ExcelPropertyRow {

    ArrayList keyList
    Row row

    ExcelPropertyRow(Row row, keyList) {
        this.row = row
        this.keyList = keyList
    }

    def keysMatch(Map<String, String> matchKeyList) {
        def propertyMap = this.getPropertyMap()
        def match = true
        matchKeyList.each {
            if (propertyMap.get(it.getKey()) != it.getValue())
                match = false
        }
        match
    }

    def getPropertyMap() {
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

    def putPropertyMap(Map<String, String> keyMap) {
        keyMap.each { key, value ->
            def colNum = getColumnNumber(key)
            if (colNum >= 0) {
                Cell cell = row.createCell(colNum)
                cell.setCellValue(value)
            }
        }
    }

    def getColumnNumber(String key) {
        keyList.indexOf(key)
    }

    def setValue(String key, value) {
        def colNum = getColumnNumber(key)
        if (colNum >= 0) {
            Cell cell = row.getCell(colNum)
            if (cell == null)
                cell = row.createCell(colNum)
            cell.setCellValue(value)
        }
    }

    def setStyle(String key, CellStyle cellStyle) {
        def colNum = getColumnNumber(key)
        if (colNum >= 0) {
            Cell cell = row.getCell(colNum)
            if (cell == null)
                cell = row.createCell(colNum)
            cell.setCellStyle(cellStyle)
        }
    }

    def getValue(String key) {
        def colNum = getColumnNumber(key)
        Cell cell = row.getCell(colNum)
        cell == null ? null : ExcelUtil.toStringWithOnlyIntegerNumerics(cell)
    }

    def putStyleMapIntoRow(Map<String, CellStyle> styleMap) {
        styleMap.each { key, value ->
            def colNum = getColumnNumber(key)
            if (colNum >= 0) {
                row.getCell(colNum).setCellStyle(value)
            }
        }
    }
}
