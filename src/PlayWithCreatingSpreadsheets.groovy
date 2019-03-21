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

import javax.print.attribute.standard.SheetCollate

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
        String path = propertyArgs.get("path")
        ExcelPropertyFile fromFile = ExcelPropertyFile.openExcelPropertyFileUsingChooser("Pick a spreadsheet", propertyArgs.get("path"))
        if (fromFile != null) {
            ExcelPropertyFile toFile = ExcelPropertyFile.createNewSpreadsheetFromFileName(path + "test.xlsx", CREATE)
            ExcelPropertySheet fromSheet = fromFile.nextExcelPropertySheet()
            ExcelPropertySheet toSheet = toFile.createNewExcelPropertySheetFromModel(fromSheet)
            toSheet.cloneExcelPropertyRow(1, fromSheet.getExcelPropertyRow(1))
            toFile.writeAndClose()
        }
    }

}

//