import org.apache.poi.ss.usermodel.Cell
import org.apache.poi.ss.usermodel.CellStyle
import org.apache.poi.ss.usermodel.Row
import properties.ExcelPropertyFile
import properties.ExcelPropertyRow
import properties.ExcelPropertySheet
import useful.Args

import static filemanagement.BaseFile.CreateFlag.*

class PlayWithCreatingSpreadsheets {

    static final SPREADSHEET_PROMPT = "prompt.for.master.spreadsheet.for.component"

    Args propertyArgs

    PlayWithCreatingSpreadsheets(args) {
        start(args)
    }

    static main(args) {
        new PlayWithCreatingSpreadsheets(args)
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

        def fileName = "test.xlsx"

        ExcelPropertyFile inSpreadsheet = ExcelPropertyFile.openUsingChooser("Pick a file", filePath)
        ExcelPropertyFile outSpreadsheet = ExcelPropertyFile.createNewSpreadsheetFromFileName(filePath + fileName, CREATE)

        while (inSpreadsheet.hasNextExcelPropertySheet()) {
            ExcelPropertySheet inSheet = inSpreadsheet.nextExcelPropertySheet()
            def headerRowNum = inSheet.headerRowNum
            def keyList = inSheet.keyList

            ExcelPropertySheet outSheet = ExcelPropertySheet.createPropertySheetInExcelPropertyFile(outSpreadsheet, inSheet.sheetName)
            outSheet.addHeaderRow(headerRowNum, keyList)
            while (inSheet.hasNextExcelPropertyRow()) {
                ExcelPropertyRow inPropertyRow = inSheet.nextExcelPropertyRow()
                def valueMap = inPropertyRow.getPropertyMap()
                def rowNum = inPropertyRow.rowNum
                outSheet.addRow(rowNum,valueMap)
            }
        }
        outSpreadsheet.writeAndClose()
    }

    def applyStyleToRowOne(ExcelPropertySheet inSheet, ExcelPropertySheet outSheet, int cellNumber) {
        CellStyle inCellStyle = inSheet.sheet.getRow(0).getCell(cellNumber).getCellStyle()
        Cell outCell = outSheet.sheet.getRow(0).getCell(cellNumber)
        CellStyle outCellStyle = outSheet.sheet.getWorkbook().createCellStyle()
        outCellStyle.cloneStyleFrom(inCellStyle)
        outCell.setCellStyle(outCellStyle)
    }

    def setWidthToRowOne(ExcelPropertySheet inSheet, ExcelPropertySheet outSheet, int columnNumber) {
        int columnWidth = inSheet.sheet.getColumnWidth(columnNumber)
        outSheet.sheet.setColumnWidth(columnNumber, columnWidth)
    }

    def applyStylesToRow(int rowNum, ExcelPropertySheet inSheet, ExcelPropertySheet outSheet) {
        Row inRow = inSheet.sheet.getRow(rowNum)
        Row outRow = outSheet.sheet.getRow(rowNum)
        Iterator<Cell> cellIterator = inRow.cellIterator()
        while (cellIterator.hasNext()) {
            Cell inCell = cellIterator.next()
            Cell outCell = outRow.getCell(inCell.columnIndex)
            CellStyle inCellStyle = inCell.getCellStyle()
            CellStyle outCellStyle = convertStyleForOutSheet(inCellStyle, outSheet)
            outCell.setCellStyle(outCellStyle)
        }
    }

    def convertStyleForOutSheet(inCellStyle, ExcelPropertySheet outSheet) {
        CellStyle outCellStyle = outSheet.sheet.getWorkbook().createCellStyle();
        outCellStyle.cloneStyleFrom(inCellStyle);
        outCellStyle
    }
}
