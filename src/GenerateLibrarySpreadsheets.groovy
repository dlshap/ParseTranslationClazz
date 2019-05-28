import i18n.LanguageLabels
import libraryquestions.LibraryArgs
import libraryquestions.LibrarySpreadsheetUpdater
import logging.Dates
import logging.Log
import logging.LogUtils
import properties.ExcelPropertyFile
import properties.ExcelPropertyRow
import properties.ExcelPropertySheet

class GenerateLibrarySpreadsheets {

    LibraryArgs libraryArgs
    String path, language
    String foreignLangFileName, masterLangFileName

    GenerateLibrarySpreadsheets(args) {
        start(args)
    }

    static main(args) {
        if (allLanguages(args))
            runForAllLanguages(args)
        else
            runForOneLanguage(args)
    }

    static boolean allLanguages(args) {
        def checkForLanguage = new LibraryArgs(args)
        checkForLanguage.languageName.toLowerCase() == "all"
    }

    static runForAllLanguages(args) {
        ArrayList<String> languageList = LanguageLabels.getLanguageList()
        languageList.each {
            if (it != "English") {
                println "Processing $it library spreadsheet"
                new GenerateLibrarySpreadsheets(["language=$it"])
            }
        }
    }

    static runForOneLanguage(args) {
        new GenerateLibrarySpreadsheets(args)
    }

    def start(args) {
        libraryArgs = new LibraryArgs(args)
        setupPathsAndNames()
        if (!(LanguageLabels.isLanguageInList(language)))
        //todo: change to exception
            println "ERROR: \"$language\" is not in language list"
        else {
            println "Building $language spreadsheet from '${masterLangFileName}.xlsx' and '${foreignLangFileName}.xlsx'"
            LogUtils.OpenLogs(path + "\\logs\\", language)
            generateSpreadsheet()
        }
    }

    def setupPathsAndNames() {
        language = libraryArgs.languageName
        path = libraryArgs.spreadsheetPath
        foreignLangFileName = "QuestionAnswerLibrary (${libraryArgs.languageName})"
        masterLangFileName = "QuestionAnswerLibrary (English)"
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
        ExcelPropertyFile.openFileUsingFileName(path + masterLangFileName + ".xlsx")
    }

    ExcelPropertyFile getLibraryFile() {
        ExcelPropertyFile.openFileUsingFileName(path + foreignLangFileName + ".xlsx")
    }

    def createNewLanguageLibraryExcelFileUsingModel(ExcelPropertyFile modelLibraryExcelFile) {
        String newLibraryFileName = path + "\\new\\" + foreignLangFileName + "_new.xlsx"
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
