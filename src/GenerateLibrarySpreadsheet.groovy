import filemanagement.BaseFile
import i18n.LanguageLabels
import i18n.Messages
import libraryquestions.LibraryArgs
import libraryquestions.LibraryExcelPropertyFile
import org.apache.poi.ss.usermodel.Sheet

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
        LibraryExcelPropertyFile modelLibraryExcelFile = getModelFile()
        LibraryExcelPropertyFile languageLibraryExcelFile = getLibraryFile()
        if (languageLibraryExcelFile == null)
            languageLibraryExcelFile = createNewLanguageLibraryExcelFileUsingModel(modelLibraryExcelFile)
        else
            updateLanguageLibraryExcelFileFromModel(modelLibraryExcelFile, languageLibraryExcelFile)
        if (languageLibraryExcelFile != null)
            languageLibraryExcelFile.writeAndClose()
    }

   LibraryExcelPropertyFile getModelFile() {
        def prompt = Messages.getString(MODEL_SPREADSHEET_PROMPT, "master", language)
        LibraryExcelPropertyFile.openLibraryPropertyFileUsingChooser(prompt, libraryArgs.spreadsheetPath)
    }

    LibraryExcelPropertyFile getLibraryFile() {
        LibraryExcelPropertyFile.openLibraryPropertyFileUsingFileName(languageLibraryFileName)
    }

    LibraryExcelPropertyFile createNewLanguageLibraryExcelFileUsingModel(LibraryExcelPropertyFile modelLibraryExcelFile) {
        LibraryExcelPropertyFile libraryExcelPropertyFile = LibraryExcelPropertyFile.createNewFileFromFileName(languageLibraryFileName, BaseFile.CreateFlag.CREATE)
        Sheet sheet = libraryExcelPropertyFile.workbook.createSheet("test")
        sheet.createRow(0)
        libraryExcelPropertyFile
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
