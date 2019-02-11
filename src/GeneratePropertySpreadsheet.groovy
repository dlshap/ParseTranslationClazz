import excelfilemanagement.ExcelPropertyFile
import exceptions.OverwriteFileException
import filemanagement.BaseFile
import libraryquestions.LibraryArgs
import useful.Args
import useful.Messages

class GeneratePropertySpreadsheet {

    static final SPREADSHEET_PROMPT = "prompt.for.master.spreadsheet.for.component"

    Args propertyArgs

    GeneratePropertySpreadsheet(args) {
        start(args)
    }

    static main(args) {
        new GeneratePropertySpreadsheet(args)
    }

    def start(args) {
        propertyArgs = new Args(args)
        setDefaultArgs()
        println propertyArgs.get("language")
//        generateSpreadsheet()
    }

    def setDefaultArgs() {
        if (propertyArgs.get("language") == null)
            propertyArgs.set("language","Japanese")
    }

    def generateSpreadsheet() {
        ExcelPropertyFile excelMasterPropertySpreadsheet = getMasterSpreadsheet()
        if (excelMasterPropertySpreadsheet.exists())
            generateNewSpreadsheetFromMasterSpreadsheet(excelMasterPropertySpreadsheet)
    }

    def getMasterSpreadsheet() {
        def chooserPrompt = Messages.getString(SPREADSHEET_PROMPT, "DMT", propertyArgs.get("language"))
        ExcelPropertyFile.getPropertiesExcelFileUsingChooser(filePath, chooserPrompt, "DMT")
    }

    def generateNewSpreadsheetFromMasterSpreadsheet(ExcelPropertyFile excelPropertyFile) {
        def filePath = excelPropertyFile.filePath
        def fileName = "DMT-DE Properties Translations($newLanguage).txt"
//        ExcelPropertyFile newExcelPropertyFile = ExcelPropertyFile.createPropertiesExcelFileFromFileAndPathNames(filePath, fileName)
        try {
            def createFlag = args.get("overwrite") == "yes" ? BaseFile.createFlag.CREATE : BaseFile.createFlag.CREATE_ONLY_IF_NO_EXISTING_FILE
            def newExcelPropertyFile = new BaseFile(filePath + fileName, createFlag)
            newExcelPropertyFile.file << "test"
        } catch (OverwriteFileException e) {
            println "! ${e.information} !"
        }

    }

}
