import i18n.LanguageLabels
import i18n.Messages
import libraryquestions.LibraryExcelPropertyFile
import properties.ExcelPropertyFile
import useful.Args

class GenerateLibrarySpreadsheet {
    static final MODEL_SPREADSHEET_PROMPT = "prompt.for.translation.spreadsheet.for"

    Args propertyArgs
    String language, path       // args

    GenerateLibrarySpreadsheet(args) {
        start(args)
    }

    static main(args) {
        new GenerateLibrarySpreadsheet(args)
    }

    def start(args) {
        propertyArgs = new Args(args)
        setDefaultArgs()
        if (!(LanguageLabels.isLanguageInList(language)))
            println "ERROR: \"$language\" is not in language list"
        else
            generateSpreadsheet()
    }

    def setDefaultArgs() {
        language = propertyArgs.get("language")
        if (language == null)
            language = "All"
        path = propertyArgs.get("path")
        if (path == null)
            path = "C:\\Users\\s0041664\\Documents\\Projects\\DMT-DE\\Project Work\\Translations\\"
    }

    def generateSpreadsheet() {
        /*
        Open English spreadsheet
        Try to open language spreadsheet
        if language spreadsheet exists
            loop through english spreadsheet
                if matching row, update the english and mark date changed
                if no match, create row with english
            end loop
            loop through language spreadsheet
                if no matching english row, delete it and log it
            end loop
        else (new language spreadsheet)
            loop through english spreadsheet
                create row in language spreadsheet, creating new columns modeled after english column
            end loop
         */

        LibraryExcelPropertyFile modelLibraryExcelFile = getModelFile()
        LibraryExcelPropertyFile languageLibraryExcelFile = getLibraryFile()
        if (languageLibraryExcelFile == null)
            languageLibraryExcelFile = createNewLanguageLibraryExcelFile(modelLibraryExcelFile)
        else
            updateLanguageLibraryExcelFileFromModel(modelLibraryExcelFile, languageLibraryExcelFile)
        languageLibraryExcelFile.writeAndClose()
    }

    LibraryExcelPropertyFile getModelFile() {
        def prompt = Messages.getString(MODEL_SPREADSHEET_PROMPT, "Master Properties", language)
        LibraryExcelPropertyFile.openLibraryPropertyFileUsingChooser(prompt, path + "\\Spreadsheets\\DMTQuestionLibrarySpreadsheets\\")
    }

    LibraryExcelPropertyFile getLibraryFile() {

    }

    LibraryExcelPropertyFile createNewLanguageLibraryExcelFile(LibraryExcelPropertyFile modelLibraryExcelFile) {

    }

    private updateLanguageLibraryExcelFileFromModel(modelLibraryExcelFile, languageLibraryExcelFile) {

    }




}
