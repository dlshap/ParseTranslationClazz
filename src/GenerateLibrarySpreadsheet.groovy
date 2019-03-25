import i18n.LanguageLabels
import i18n.Messages
import libraryquestions.LibraryExcelPropertyFile
import useful.Args
import useful.Config

class GenerateLibrarySpreadsheet {
    static final MODEL_SPREADSHEET_PROMPT = "prompt.for.translation.spreadsheet.for"

    Args propertyArgs
    String language, configPath, spreadsheetPath
    Config config

    GenerateLibrarySpreadsheet(args) {
        start(args)
    }

    static main(args) {
        new GenerateLibrarySpreadsheet(args)
    }

    def start(args) {
        propertyArgs = new Args(args)
        setLanguageAndConfigPath()
        getConfig()
        if (!(LanguageLabels.isLanguageInList(language)))
            println "ERROR: \"$language\" is not in language list"
        else
            generateSpreadsheet()
    }

    def setLanguageAndConfigPath() {
        language = propertyArgs.get("language")
        if (language == null)
            language = "All"
        configPath = propertyArgs.get("path")
        if (configPath == null)
            configPath = "C:\\Users\\s0041664\\Documents\\Projects\\DMT-DE\\Project Work\\Translations\\"
    }

    def getConfig() {
        config = new Config(configPath)
        spreadsheetPath = configPath + config.get("library.question.spreadsheet.relative.path")
    }

    def generateSpreadsheet() {
        LibraryExcelPropertyFile modelLibraryExcelFile = getModelFile()
        LibraryExcelPropertyFile languageLibraryExcelFile = getLibraryFile()
        if (languageLibraryExcelFile == null)
            languageLibraryExcelFile = createNewLanguageLibraryExcelFile(modelLibraryExcelFile)
        else
            updateLanguageLibraryExcelFileFromModel(modelLibraryExcelFile, languageLibraryExcelFile)
        if (languageLibraryExcelFile != null)
            languageLibraryExcelFile.writeAndClose()
    }

    LibraryExcelPropertyFile getModelFile() {
        def prompt = Messages.getString(MODEL_SPREADSHEET_PROMPT, "Master Properties", language)
        LibraryExcelPropertyFile.openLibraryPropertyFileUsingChooser(prompt, spreadsheetPath)
    }

    LibraryExcelPropertyFile getLibraryFile() {
        buildLibraryFileNameForLanguage()
    }

    LibraryExcelPropertyFile createNewLanguageLibraryExcelFile(LibraryExcelPropertyFile modelLibraryExcelFile) {
        /*
            loop through english spreadsheet
                create row in language spreadsheet, creating new columns modeled after english column
            end loop
         */


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
