import excelfilemanagement.ExcelPropertyFile
import exceptions.OverwriteFileException
import filemanagement.BaseFile
import libraryquestions.LibraryArgs
import useful.Messages

class GeneratePropertySpreadsheet {

    static final SPREADSHEET_PROMPT = "prompt.for.master.spreadsheet.for.component"
    def newLanguage

    GeneratePropertySpreadsheet(args) {
        start(args)
    }

    static main(args) {
        new GeneratePropertySpreadsheet(args)
    }

    def start(args) {
        def libraryArgs = new LibraryArgs(args)
        newLanguage = libraryArgs.languageName
        generateSpreadsheet(libraryArgs)
    }

    def generateSpreadsheet(libraryArgs) {
        ExcelPropertyFile excelPropertyFile = getMasterSpreadsheet(libraryArgs)
        generateNewSpreadsheetFromMasterSpreadsheet(excelPropertyFile)
    }

    def getMasterSpreadsheet(LibraryArgs libraryArgs) {
        def filePath = libraryArgs.startFilePath
        def chooserPrompt = Messages.getString(SPREADSHEET_PROMPT, "DMT", newLanguage)
        ExcelPropertyFile.getPropertiesExcelFileUsingChooser(filePath, chooserPrompt, "DMT")
    }

    def generateNewSpreadsheetFromMasterSpreadsheet(ExcelPropertyFile excelPropertyFile) {
        def filePath = excelPropertyFile.filePath
        def fileName = "DMT-DE Properties Translations($newLanguage).xlsx"
//        ExcelPropertyFile newExcelPropertyFile = ExcelPropertyFile.createPropertiesExcelFileFromFileAndPathNames(filePath, fileName)
        try {
            def newExcelPropertyFile = new BaseFile(filePath + fileName, BaseFile.createFlag.CREATE_ONLY_IF_NO_EXISTING_FILE)
            newExcelPropertyFile.file << "test"
        } catch (OverwriteFileException e) {
            println "${e.information}"
        }

    }

}
