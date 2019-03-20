import excelfilemanagement.ExcelFile
import org.apache.poi.ss.usermodel.Cell
import org.apache.poi.ss.usermodel.CellStyle
import org.apache.poi.ss.usermodel.Row
import org.apache.poi.ss.usermodel.Sheet
import org.apache.poi.ss.usermodel.Workbook
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
        ExcelPropertyFile fromFile = ExcelPropertyFile.openExcelPropertyFileUsingChooser("Pick a spreadsheet", propertyArgs.get("path"))

        getMyStyles(fromFile)
    }
    
    def getMyStyles(ExcelPropertyFile fromFile) {
        
        Row fromRow = fromFile.workbook.getSheetAt(0).getRow(0)
        ArrayList<CellStyle> styles = [], styles2 = []
        for (int i = fromRow.getFirstCellNum(); i < fromRow.getLastCellNum(); i++) {
            CellStyle cellStyle = fromRow.getCell(i).getCellStyle()
            styles << cellStyle
        }
        for (int i = fromRow.getFirstCellNum(); i < fromRow.getLastCellNum(); i++) {
            CellStyle cellStyle = fromRow.getCell(i).getCellStyle()
            styles2 << cellStyle
        }

        println styles
//        if (fromFile != null) {
//            ExcelPropertyFile toFile = ExcelPropertyFile.createNewSpreadsheetFromFileName(path + "test.xlsx", CREATE)

//            copyHeaderRow(fromFile, toFile)
//            copyHeaderStyles(fromFile, toFile)
//        }
    }

//    def copyHeaderRow(ExcelPropertyFile fromFile, ExcelPropertyFile toFile) {
//        ExcelPropertySheet fromExcelPropertySheet = fromFile.nextExcelPropertySheet()
//        Sheet toSheet = toFile.workbook.createSheet("Test")
//        Row toRow = toSheet.createRow(0)
//        Row fromRow = fromExcelPropertySheet.getRow(0)
//        fromRow.eachWithIndex { Cell cell, int i ->
//            Cell toCell = toRow.createCell(i)
//        }
//    }
}

//            Cell fromCell = fromExcelPropertyFile.workbook.sheetIterator().next().getRow(0).getCell(0)
//            String fromValue = fromCell.getStringCellValue()
//            CellStyle fromStyle = fromCell.getCellStyle()

//            ExcelPropertyFile toExcelFile = ExcelPropertyFile.createNewSpreadsheetFromFileName(propertyArgs.get("path") + "test.xlsx", CREATE)
//            Sheet toSheet = toExcelFile.workbook.createSheet("Test")
//            Row toRow = toSheet.createRow(0)
//            Cell toCell = toRow.createCell(0)
//            toCell.setCellValue(fromValue)
//            CellStyle toStyle = toSheet.workbook.createCellStyle()
//            toStyle.cloneStyleFrom(fromStyle)
//            toCell.setCellStyle(toStyle)
//            toExcelFile.writeAndClose()

