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
            propertyArgs.set("path", "C:\\Users\\s0041664\\Documents\\Projects\\DMT-DE\\Project Work\\translations\\Spreadsheets\\DMTQuestionLibrarySpreadsheets\\")
    }

    def generateSpreadsheet() {
        String path = propertyArgs.get("path")
        ExcelPropertyFile fromFile = ExcelPropertyFile.openExcelPropertyFileUsingChooser("Pick a spreadsheet", propertyArgs.get("path"))
        ExcelPropertyFile toFile = ExcelPropertyFile.createNewSpreadsheetFromFileName(path + "\\new\\QuestionAnswerLibrary(${propertyArgs.get("language")}).xlsx", CREATE)
        while (fromFile.hasNextExcelPropertySheet()) {
            ExcelPropertySheet modelPropertySheet = fromFile.nextExcelPropertySheet()
            ExcelPropertySheet toPropertySheet = toFile.createNewExcelPropertySheetFromModel(modelPropertySheet)
            duplicateModelInNewFile(modelPropertySheet, toPropertySheet)
        }
        toFile.writeAndClose()
    }

    def duplicateModelInNewFile(ExcelPropertySheet modelPropertySheet, ExcelPropertySheet toPropertySheet) {
        modelPropertySheet.resetRows()
        while (modelPropertySheet.hasNextExcelPropertyRow()) {
            print "."
            ExcelPropertyRow fromRow = modelPropertySheet.nextExcelPropertyRow()
            ExcelPropertyRow toRow = cloneModelRowAddingTranslatedColumns(fromRow, toPropertySheet)
        }
    }

    ExcelPropertyRow cloneModelRowAddingTranslatedColumns(ExcelPropertyRow fromRow, ExcelPropertySheet toPropertySheet) {
        Iterator cellIterator = fromRow.row.cellIterator()
        while (cellIterator.hasNext()) {
            cellIterator.next()
        }
    }
}