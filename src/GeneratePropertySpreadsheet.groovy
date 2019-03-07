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
            ExcelPropertyFile outputExcelPropertyFile = createOutputExcelPropertyFile(modelExcelPropertyFile)
            while (modelExcelPropertyFile.hasNextExcelPropertySheet()) {
                ExcelPropertySheet modelPropertySheet = modelExcelPropertyFile.nextExcelPropertySheet()
                ExcelPropertySheet newPropertySheet = createPropertySheetFromPropertiesFileUsingModel(outputExcelPropertyFile, modelPropertySheet)
                movePropertiesIntoPropertySheetUsingModelSheet(newPropertySheet, modelPropertySheet)
            }
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

    def createOutputExcelPropertyFile(ExcelPropertyFile modelExcelPropertyFile) {
        String outputFileName = buildOutputFileName(modelExcelPropertyFile)
        ExcelPropertyFile outputExcelPropertyFile = ExcelPropertyFile.createNewSpreadsheetFromFileName(outputFileName, BaseFile.CreateFlag.CREATE)
        outputExcelPropertyFile
    }

    def createPropertySheetFromPropertiesFileUsingModel(ExcelPropertyFile outputExcelPropertyFile, ExcelPropertySheet modelPropertySheet) {
        ExcelPropertySheet outputPropertySheet = outputExcelPropertyFile.createNewExcelPropertySheetFromModel(modelPropertySheet)
        outputPropertySheet
    }

    def movePropertiesIntoPropertySheetUsingModelSheet(ExcelPropertySheet newPropertySheet, ExcelPropertySheet modelPropertySheet) {
        def propertyFileName = buildPropertyFileName(modelPropertySheet)
        PropertyFile propertyFile = PropertyFile.openPropertyFileFromFileName(propertyFileName)
        TranslationProperties translationProperties = propertyFile.translationProperties
        updateNewSheetFromPropertiesFileAndModel(newPropertySheet, translationProperties, modelPropertySheet)
    }

    def buildPropertyFileName(ExcelPropertySheet excelPropertySheet) {
        def component = excelPropertySheet.sheetName
        def fileName = path + "\\$component\\PropertyFiles\\messages.properties"
    }

    def buildOutputFileName(ExcelPropertyFile modelFile) {
        def outputPath = modelFile.getDirPath()
        def outputFileName = outputPath + "\\new\\DMT-DE Properties Translations ($language)_new.xlsx"
        outputFileName
    }

    def updateNewSheetFromPropertiesFileAndModel(ExcelPropertySheet newPropertySheet, TranslationProperties translationProperties, ExcelPropertySheet modelPropertySheet) {

        while (translationProperties.hasNext()) {
            def property = translationProperties.next()
            def propertyKey = property.getKey()
            def row = modelPropertySheet.getKeyMaps()
            def modelRow = row.find {it.get("Message Key") == propertyKey}

        }
    }

//    def movePropertiesFromPropertyFileIntoSpreadsheet(ExcelPropertyFile modelExcelPropertyFile, ExcelPropertyFile outputExcelPropertyFile) {
//        TranslationProperties properties
//        def sheetName = modelExcelPropertySheet.sheetName
//        def propertyFilePath = path + sheetName + "\\"
//        PropertyFile propertyFile = PropertyFile.openPropertyFileForComponentUsingChooser(sheetName, propertyFilePath)
//        TranslationProperties translationProperties = propertyFile.getTranslationProperties()
//    }

//    def generateOutputSheetFromPropertiesAndModel(TranslationProperties translationProperties, ExcelPropertySheet modelPropertySheet) {
//        /*
//        Get model spreadsheet language
//        Get model spreadsheet styles
//        Setup header row
//        Apply header style (later)
//
//        For each property
//        If model has property
//            If same language, write the line, including translation, otherwise leave translation cell blank
//        else
//            Create line with blank translation
//        Apply data line styling (later)
//         */
//
//        def modelLanguage = modelPropertySheet.language
//        while (translationProperties.hasNext()) {
//            def translationProperty = translationProperties.next()
//            def propertyKey = translationProperty.getKey()
//            def propertyValue = translationProperty.getValue()
//            while (modelPropertySheet.hasNextExcelPropertyRow()) {
//                ExcelPropertyRow row = modelPropertySheet.nextExcelPropertyRow()
//            }
//        }
//    }
}