import i18n.LanguageLabels
import i18n.Messages
import libraryquestions.LibraryArgs
import libraryquestions.LibrarySpreadsheetBuilder
import libraryquestions.LibrarySpreadsheetUpdater
import properties.ExcelPropertyFile
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
        LibrarySpreadsheetBuilder.buildNewSpreadsheetFromModel(newLibraryFileName, modelLibraryExcelFile)

    }

    private updateNewLanguageLibraryExcelFileFromOldFile(ExcelPropertyFile newLibraryExcelFile, ExcelPropertyFile oldLibraryExcelFile) {
        newLibraryExcelFile.resetSheetIterator()
        while (newLibraryExcelFile.hasNextExcelPropertySheet()) {
            ExcelPropertySheet newSheet = newLibraryExcelFile.nextExcelPropertySheet()
            ExcelPropertySheet oldSheet = oldLibraryExcelFile.getExcelPropertySheet(newSheet.sheetName)
            updateNewSheetFromOldSheet(newSheet, oldSheet)
        }
    }

    private updateNewSheetFromOldSheet(ExcelPropertySheet newSheet, ExcelPropertySheet oldSheet) {
        println newSheet.sheetName
    }


}
