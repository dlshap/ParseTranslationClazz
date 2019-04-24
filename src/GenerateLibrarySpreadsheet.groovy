import i18n.LanguageLabels
import i18n.Messages
import libraryquestions.LibraryArgs
import libraryquestions.LibrarySpreadsheetUpdater
import logging.Dates
import logging.Log
import properties.ExcelPropertyFile
import properties.ExcelPropertyRow
import properties.ExcelPropertySheet

class GenerateLibrarySpreadsheet {
    static final MODEL_SPREADSHEET_PROMPT = "prompt.for.translation.spreadsheet.for"

    LibraryArgs libraryArgs
    String path, language
    String foreignLangFileName

    GenerateLibrarySpreadsheet(args) {
        start(args)
    }

    static main(args) {
        new GenerateLibrarySpreadsheet(args)
    }

    def start(args) {
        libraryArgs = new LibraryArgs(args)
        setupPathsAndNames()
        openTranslationLogs()
        if (!(LanguageLabels.isLanguageInList(language)))
        //todo: change to exception
            println "ERROR: \"$language\" is not in language list"
        else
            generateSpreadsheet()
    }

    def setupPathsAndNames() {
        language = libraryArgs.languageName
        path = libraryArgs.spreadsheetPath
        foreignLangFileName = "QuestionAnswerLibrary (${libraryArgs.languageName}).xlsx"
    }

    def openTranslationLogs() {
        def logsFilePath = path + "\\logs\\"
        Log.open("adds", logsFilePath + "log-property-adds.txt")
        Log.writeLine "adds", "Running on " + Dates.currentDateAndTime() + ":\r\n"
        Log.open("updates", logsFilePath + "log-property-changes.txt")
        Log.writeLine "updates", "Running on " + Dates.currentDateAndTime() + ":\r\n"
        Log.open("deletes", logsFilePath + "log-property-deletes.txt")
        Log.writeLine "deletes", "Running on " + Dates.currentDateAndTime() + ":\r\n"
    }

    def generateSpreadsheet() {
        ExcelPropertyFile modelLibraryExcelFile = getModelFile()
        if (modelLibraryExcelFile != null) {
            ExcelPropertyFile newLibraryExcelFile = createNewLanguageLibraryExcelFileUsingModel(modelLibraryExcelFile)
            ExcelPropertyFile oldLibraryExcelFile = getLibraryFile()
            if (oldLibraryExcelFile != null)
                updateNewLanguageLibraryExcelFileFromOldFile(newLibraryExcelFile, oldLibraryExcelFile)
            newLibraryExcelFile.writeAndClose()
        }
    }

    ExcelPropertyFile getModelFile() {
        def prompt = Messages.getString(MODEL_SPREADSHEET_PROMPT, "master", language)
        ExcelPropertyFile.openFileUsingChooser(prompt, libraryArgs.spreadsheetPath)
    }

    ExcelPropertyFile getLibraryFile() {
        ExcelPropertyFile.openFileUsingFileName(path + foreignLangFileName)
    }

    def createNewLanguageLibraryExcelFileUsingModel(ExcelPropertyFile modelLibraryExcelFile) {
        String newLibraryFileName = path + "\\new\\" + foreignLangFileName
        LibrarySpreadsheetUpdater.buildNewSpreadsheetFromModel(newLibraryFileName, modelLibraryExcelFile)
    }

    def updateNewLanguageLibraryExcelFileFromOldFile(ExcelPropertyFile newLibraryExcelFile, ExcelPropertyFile oldLibraryExcelFile) {
        newLibraryExcelFile.resetSheetIterator()
        while (newLibraryExcelFile.hasNextExcelPropertySheet()) {
            ExcelPropertySheet newSheet = newLibraryExcelFile.nextExcelPropertySheet()
            ExcelPropertySheet oldSheet = oldLibraryExcelFile.getExcelPropertySheet(newSheet.sheetName)
            updateNewSheetFromOldSheet(newSheet, oldSheet)
            logAddedRowsInNewSheetNotInOldSheet(newSheet, oldSheet)
        }
    }

    def updateNewSheetFromOldSheet(ExcelPropertySheet newSheet, ExcelPropertySheet oldSheet) {
        while (oldSheet.hasNextExcelPropertyRow()) {
            ExcelPropertyRow oldRow = oldSheet.nextExcelPropertyRow()
            ExcelPropertyRow newRow = LibrarySpreadsheetUpdater.getRowMatchingKeysFromModelRow(newSheet, oldRow)
            if (newRow != null) {
                LibrarySpreadsheetUpdater.updateNewRowTranslationsFromOldRowForSheet(newRow, oldRow, newSheet.sheetName)
            } else {
                Log.writeLine("deletes", "Deleted old row for translation in ${newSheet.sheetName}: ${LibrarySpreadsheetUpdater.getRowKeys(oldRow)}")
            }
        }
    }

    def logAddedRowsInNewSheetNotInOldSheet(ExcelPropertySheet newSheet, ExcelPropertySheet oldSheet) {
        while (newSheet.hasNextExcelPropertyRow()) {
            ExcelPropertyRow newRow = newSheet.nextExcelPropertyRow()
            if (LibrarySpreadsheetUpdater.getRowMatchingKeysFromModelRow(oldSheet, newRow) == null) {
                Log.writeLine("adds", "Added new row for translation in ${newSheet.sheetName}: ${LibrarySpreadsheetUpdater.getRowKeys(newRow)}")
            }
        }
    }
}
