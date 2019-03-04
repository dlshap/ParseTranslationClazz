import filemanagement.BaseFile
import i18n.Messages
import properties.ExcelPropertyFile
import properties.ExcelPropertySheet
import properties.PropertyFile
import translations.TranslationProperties
import useful.Args

import static properties.ExcelPropertyFile.createNewSpreadsheetFromFileName

class GeneratePropertySpreadsheet {


    static final PROPERTIES_FILE_PROMPT = "prompt.for.message.properties.file.for"
    static final MODEL_SPREADSHEET_PROMPT = "prompt.for.master.spreadsheet.for.component.into"

    Args propertyArgs
    String language, path       // args

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
        language = propertyArgs.get("language")
        if (language == null)
            language = "Japanese"
        path = propertyArgs.get("path")
        if (path == null)
            path = "C:\\Users\\s0041664\\Documents\\Projects\\DMT-DE\\Project Work\\Translations\\"
    }

    def generateSpreadsheet() {
        ExcelPropertyFile modelExcelPropertyFile = chooseModelPropertySpreadsheet()
        if (modelExcelPropertyFile != null) {
            ExcelPropertyFile outputExcelPropertyFile = createOutputExcelPropertyFileInModelDirectory(modelExcelPropertyFile)
            generateOutputSheetsFromModelSheets(outputExcelPropertyFile, modelExcelPropertyFile)
            outputExcelPropertyFile.writeAndClose()
        }
    }

    def chooseModelPropertySpreadsheet() {
        def modelSpreadsheetPath = propertyArgs.get("path") + "Spreadsheets\\PropertySpreadsheets\\DMTDE\\"
        def language = propertyArgs.get("language")
        def prompt = Messages.getString(MODEL_SPREADSHEET_PROMPT, language)
        ExcelPropertyFile modelExcelPropertyFile = ExcelPropertyFile.openUsingChooser(MODEL_SPREADSHEET_PROMPT, modelSpreadsheetPath)
        modelExcelPropertyFile
    }

    def createOutputExcelPropertyFileInModelDirectory(ExcelPropertyFile modelExcelPropertyFile) {
        String outputFileName = buildOutputFileName(modelExcelPropertyFile)
        ExcelPropertyFile outputExcelPropertyFile = createNewSpreadsheetFromFileName(outputFileName, BaseFile.CreateFlag.CREATE)
        outputExcelPropertyFile
    }

    def buildOutputFileName(ExcelPropertyFile modelFile) {
        def outputPath = modelFile.getDirPath()
        def outputFileName = outputPath + "\\new\\DMT-DE Properties Translations ($language).new.xlsx"
        outputFileName
    }

    def generateOutputSheetsFromModelSheets(ExcelPropertyFile outputExcelPropertyFile, ExcelPropertyFile modelExcelPropertyFile) {
        if (modelExcelPropertyFile != null) {
            while (modelExcelPropertyFile.hasNextExcelPropertySheet()) {
                ExcelPropertySheet modelPropertySheet = modelExcelPropertyFile.nextExcelPropertySheet()
                TranslationProperties translationProperties = getPropertiesFromPropertyFileForPropertySheet(modelPropertySheet)
                generateOutputSheetFromPropertiesAndModel(translationProperties, modelPropertySheet)
            }
        }
    }

    def getPropertiesFromPropertyFileForPropertySheet(ExcelPropertySheet modelExcelPropertySheet) {
        TranslationProperties properties
        def sheetName = modelExcelPropertySheet.sheetName
        def propertyFilePath = path + sheetName + "\\"
        PropertyFile propertyFile = PropertyFile.openPropertyFileForComponentUsingChooser(sheetName, propertyFilePath)
        properties = propertyFile.getTranslationProperties()
    }

    def generateOutputSheetFromPropertiesAndModel(TranslationProperties translationProperties, ExcelPropertySheet modelPropertySheet) {
        while (translationProperties.hasNext()) {
            // use getKey() and getValue() and populate the spreadsheet
        }
    }
}