import filemanagement.BaseFile
import i18n.Messages
import properties.ExcelPropertyFile
import properties.ExcelPropertyRow
import properties.ExcelPropertySheet
import properties.PropertyFile
import translations.TranslationProperties
import useful.Args

class GeneratePropertySpreadsheet {


    static final PROPERTIES_FILE_PROMPT = "prompt.for.message.properties.file.for"
    static final MODEL_SPREADSHEET_PROMPT = "prompt.for.master.spreadsheet.for.language"

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
//            movePropertiesFromPropertyFileIntoSpreadsheet(outputExcelPropertyFile)
            outputExcelPropertyFile.writeAndClose()
        }
    }

    def chooseModelPropertySpreadsheet() {
        def modelSpreadsheetPath = propertyArgs.get("path") + "Spreadsheets\\PropertySpreadsheets\\DMTDE\\"
        def language = propertyArgs.get("language")
        def prompt = Messages.getString(MODEL_SPREADSHEET_PROMPT, language)
        ExcelPropertyFile modelExcelPropertyFile = ExcelPropertyFile.openUsingChooser(prompt, modelSpreadsheetPath)
        modelExcelPropertyFile
    }

    def createOutputExcelPropertyFileInModelDirectory(ExcelPropertyFile modelExcelPropertyFile) {
        String outputFileName = buildOutputFileName(modelExcelPropertyFile)
        ExcelPropertyFile outputExcelPropertyFile = ExcelPropertyFile.createNewSpreadsheetFromFileName(outputFileName, BaseFile.CreateFlag.CREATE)
        outputExcelPropertyFile
    }

    def buildOutputFileName(ExcelPropertyFile modelFile) {
        def outputPath = modelFile.getDirPath()
        def outputFileName = outputPath + "\\new\\DMT-DE Properties Translations ($language)_new.xlsx"
        outputFileName
    }

    def generateOutputSheetsFromModelSheets(ExcelPropertyFile outputExcelPropertyFile, ExcelPropertyFile modelExcelPropertyFile) {
        if (modelExcelPropertyFile != null) {
            while (modelExcelPropertyFile.hasNextExcelPropertySheet()) {
                ExcelPropertySheet modelPropertySheet = modelExcelPropertyFile.nextExcelPropertySheet()
                ExcelPropertySheet outputPropertySheet = outputExcelPropertyFile.createNewExcelPropertySheetFromModel(modelPropertySheet)
            }
        }
    }

    def movePropertiesFromPropertyFileIntoSpreadsheet(ExcelPropertyFile outputExcelPropertyFile) {
        TranslationProperties translationProperties = getPropertiesFromPropertyFileForPropertySheet(modelPropertySheet)
    }

    def getPropertiesFromPropertyFileForPropertySheet(ExcelPropertySheet modelExcelPropertySheet) {
        TranslationProperties properties
        def sheetName = modelExcelPropertySheet.sheetName
        def propertyFilePath = path + sheetName + "\\"
        PropertyFile propertyFile = PropertyFile.openPropertyFileForComponentUsingChooser(sheetName, propertyFilePath)
        properties = propertyFile.getTranslationProperties()
    }

    def generateOutputSheetFromPropertiesAndModel(TranslationProperties translationProperties, ExcelPropertySheet modelPropertySheet) {
        /*
        Get model spreadsheet language
        Get model spreadsheet styles
        Setup header row
        Apply header style (later)

        For each property
        If model has property
            If same language, write the line, including translation, otherwise leave translation cell blank
        else
            Create line with blank translation
        Apply data line styling (later)
         */

        def modelLanguage = modelPropertySheet.language
        while (translationProperties.hasNext()) {
            def translationProperty = translationProperties.next()
            def propertyKey = translationProperty.getKey()
            def propertyValue = translationProperty.getValue()
            while (modelPropertySheet.hasNextExcelPropertyRow()) {
                ExcelPropertyRow row = modelPropertySheet.nextExcelPropertyRow()
            }
        }
    }
}