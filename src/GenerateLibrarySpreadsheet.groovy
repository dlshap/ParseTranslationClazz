import i18n.LanguageLabels
import i18n.Messages
import libraryquestions.LibraryArgs
import libraryquestions.LibraryExcelPropertyFile

class GenerateLibrarySpreadsheet {
    static final MODEL_SPREADSHEET_PROMPT = "prompt.for.translation.spreadsheet"

    LibraryArgs libraryArgs

    GenerateLibrarySpreadsheet(args) {
        start(args)
    }

    static main(args) {
        new GenerateLibrarySpreadsheet(args)
    }

    def start(args) {
        libraryArgs = new LibraryArgs(args)
        def language = libraryArgs.languageName
        if (!(LanguageLabels.isLanguageInList(language)))
            println "ERROR: \"$language\" is not in language list"
        else
            generateSpreadsheet()
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
        def prompt = Messages.getString(MODEL_SPREADSHEET_PROMPT, "master", libraryArgs.languageName)
        LibraryExcelPropertyFile.openLibraryPropertyFileUsingChooser(prompt, libraryArgs.spreadsheetPath)
    }

    LibraryExcelPropertyFile getLibraryFile() {
        String libraryFileName = getLibraryFileNameForLanguage()
        openLibrarySpreadsheet(libraryArgs.spreadsheetPath + libraryFileName)
    }

    String getLibraryFileNameForLanguage() {
        "QuestionAnswerLibrary (${libraryArgs.languageName}).xlsx"
    }

    LibraryExcelPropertyFile openLibrarySpreadsheet(fileName) {
//        LibraryExcelPropertyFile.
        null
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
