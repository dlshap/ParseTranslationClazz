import i18n.LanguageLabels
import i18n.Messages
import libraryquestions.LibraryArgs
import libraryquestions.LibrarySpreadsheetBuilder
import libraryquestions.LibrarySpreadsheetUpdater
import properties.ExcelPropertyFile

class GenerateLibrarySpreadsheet {
    static final MODEL_SPREADSHEET_PROMPT = "prompt.for.translation.spreadsheet.for"

    LibraryArgs libraryArgs
    String path, language
    String oldLibraryFileName

    GenerateLibrarySpreadsheet(args) {
        start(args)
    }

    static main(args) {
        new GenerateLibrarySpreadsheet(args)
    }

    def start(args) {
        libraryArgs = new LibraryArgs(args)
        setupPathsAndNames()
        if (!(LanguageLabels.isLanguageInList(language)))
        //todo: change to exception
            println "ERROR: \"$language\" is not in language list"
        else
            generateSpreadsheet()
    }

    def setupPathsAndNames() {
        language = libraryArgs.languageName
        path = libraryArgs.spreadsheetPath
        oldLibraryFileName = path + "QuestionAnswerLibrary (${libraryArgs.languageName}).xlsx"
    }

    def generateSpreadsheet() {
        ExcelPropertyFile modelLibraryExcelFile = getModelFile()
        if (modelLibraryExcelFile != null) {
            ExcelPropertyFile oldLibraryExcelFile = getLibraryFile()
            if (oldLibraryExcelFile == null)
                createNewLanguageLibraryExcelFileUsingModel(modelLibraryExcelFile)
            else
                updateLanguageLibraryExcelFileFromModel(modelLibraryExcelFile, oldLibraryExcelFile)
        }
    }

   ExcelPropertyFile getModelFile() {
        def prompt = Messages.getString(MODEL_SPREADSHEET_PROMPT, "master", language)
        ExcelPropertyFile.openFileUsingChooser(prompt, libraryArgs.spreadsheetPath)
    }

    ExcelPropertyFile getLibraryFile() {
        ExcelPropertyFile.openFileUsingFileName(oldLibraryFileName)
    }

    def createNewLanguageLibraryExcelFileUsingModel(ExcelPropertyFile modelLibraryExcelFile) {
        LibrarySpreadsheetBuilder librarySpreadsheetBuilder = new LibrarySpreadsheetBuilder(oldLibraryFileName)
        librarySpreadsheetBuilder.buildNewSpreadsheetFromModel(modelLibraryExcelFile)
    }

    private updateLanguageLibraryExcelFileFromModel(ExcelPropertyFile modelLibraryExcelFile, ExcelPropertyFile languageLibraryExcelFile) {
        LibrarySpreadsheetUpdater librarySpreadsheetUpdater = new LibrarySpreadsheetUpdater(languageLibraryExcelFile)
        librarySpreadsheetUpdater.updateSpreadsheetFromModel(modelLibraryExcelFile)
    }


}
