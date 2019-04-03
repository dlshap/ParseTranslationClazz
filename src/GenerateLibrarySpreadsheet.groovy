import filemanagement.BaseFile
import i18n.LanguageLabels
import i18n.Messages
import libraryquestions.LibraryArgs
import libraryquestions.LibrarySpreadsheetBuilder
import org.apache.poi.ss.usermodel.Sheet
import properties.ExcelPropertyFile

class GenerateLibrarySpreadsheet {
    static final MODEL_SPREADSHEET_PROMPT = "prompt.for.translation.spreadsheet.for"

    LibraryArgs libraryArgs
    String path, language
    String languageLibraryFileName

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
        languageLibraryFileName = path + "QuestionAnswerLibrary (${libraryArgs.languageName}).xlsx"
    }

    def generateSpreadsheet() {
        ExcelPropertyFile modelLibraryExcelFile = getModelFile()
        ExcelPropertyFile languageLibraryExcelFile = getLibraryFile()
        if (languageLibraryExcelFile == null)
            createNewLanguageLibraryExcelFileUsingModel(modelLibraryExcelFile)
        else
            updateLanguageLibraryExcelFileFromModel(modelLibraryExcelFile, languageLibraryExcelFile)
    }

   ExcelPropertyFile getModelFile() {
        def prompt = Messages.getString(MODEL_SPREADSHEET_PROMPT, "master", language)
        ExcelPropertyFile.openFileUsingChooser(prompt, libraryArgs.spreadsheetPath)
    }

    ExcelPropertyFile getLibraryFile() {
        ExcelPropertyFile.openFileUsingFileName(languageLibraryFileName)
    }

    def createNewLanguageLibraryExcelFileUsingModel(ExcelPropertyFile modelLibraryExcelFile) {
        LibrarySpreadsheetBuilder librarySpreadsheetBuilder = new LibrarySpreadsheetBuilder(languageLibraryFileName)
        librarySpreadsheetBuilder.buildNewSpreadsheetFromModel(modelLibraryExcelFile)
    }

    private updateLanguageLibraryExcelFileFromModel(modelLibraryExcelFile, languageLibraryExcelFile) {
        /*
         loop through english spreadsheet
         if matching row, update the english and mark date changed
         if no match, create row with english
         end loop
         loop through language spreadsheet
         if no matching english row, delete it and log it
         end loop
         */

    }


}
