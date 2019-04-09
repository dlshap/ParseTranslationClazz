package properties

import excelfilemanagement.ExcelUtil
import org.apache.poi.ss.usermodel.Cell
import org.apache.poi.ss.usermodel.CellStyle
import org.apache.poi.ss.usermodel.Row

class ExcelPropertyRow {

    ArrayList headerRowNames
    Row row

    ExcelPropertyRow(Row row, headerRowNames) {
        this.row = row
        this.headerRowNames = headerRowNames
    }

    Boolean keysMatch(Map<String, String> matchKeyList) {
        def propertyMap = this.getPropertyMap()
        def match = true
        matchKeyList.each {
            if (propertyMap.get(it.getKey()) != it.getValue())
                match = false
        }
        match
    }

    Map<String, String> getPropertyMap() {
        Map<String, String> propertyMap = ["row" : row.rowNum+1]
        headerRowNames.eachWithIndex{ String colName, int colNum ->
            Cell cell = row.getCell(colNum)
            String cellValue = ExcelUtil.toStringWithOnlyIntegerNumerics(cell)
            propertyMap.put(colName, cellValue)
        }
        propertyMap
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

    Integer getColumnNumber(String key) {
        headerRowNames.indexOf(key)
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

    String getValue(String key) {
        def colNum = getColumnNumber(key)
        Cell cell = row.getCell(colNum)
        ExcelUtil.toStringWithOnlyIntegerNumerics(cell)
    }
}
