import excelfilemanagement.ExcelPropertyFile
import exceptions.OverwriteFileException
import filemanagement.BaseFile
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
        generateSpreadsheet()
    }

    def setDefaultArgs() {
        if (propertyArgs.get("language") == null)
            propertyArgs.set("language", "Japanese")

        if (propertyArgs.get("path") == null)
            propertyArgs.set("path", "C:\\Users\\s0041664\\Documents\\Projects\\DMT-DE\\Project\\Work\\translations\\Spreadsheets\\PropertySpreadsheets\\DMTDE\\")

    }

    def generateSpreadsheet() {
        ExcelPropertyFile masterPropertySpreadsheet = getMasterSpreadsheet()
        ExcelPropertyFile outputPropertySpreadsheet = buildOutputSpreadsheetFromMaster(masterPropertySpreadsheet)
    }

    def getMasterSpreadsheet() {
        def chooserPrompt = Messages.getString(SPREADSHEET_PROMPT, "DMT", propertyArgs.get("language"))
        ExcelPropertyFile.getExcelPropertyFileUsingChooser(propertyArgs.get("path"), chooserPrompt)
    }

    def buildOutputSpreadsheetFromMaster(ExcelPropertyFile masterPropertySpreadsheet) {
        ExcelPropertyFile outputPropertySpreadsheet
        def outputPath = masterPropertySpreadsheet.filePath
        def outputFileName = "DMT-DE Properties Translations(${propertyArgs.get("language")}).xlsx"
        try {
            def createFlag = propertyArgs.get("overwrite") == "yes" ? BaseFile.createFlag.CREATE : BaseFile.createFlag.CREATE_ONLY_IF_NO_EXISTING_FILE
            outputPropertySpreadsheet = ExcelPropertyFile.createPropertiesExcelFileFromFileNameAndPathName(outputPath, outputFileName, createFlag)
        } catch (OverwriteFileException e) {
            println "! ${e.information} !"
        }
    outputPropertySpreadsheet
    }
}
