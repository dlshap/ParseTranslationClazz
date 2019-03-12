import filemanagement.BaseFile
import i18n.Messages
import org.apache.poi.ss.usermodel.CellStyle
import org.apache.poi.ss.usermodel.Workbook
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
            if (outputExcelPropertyFile != null) {
                while (modelExcelPropertyFile.hasNextExcelPropertySheet()) {
                    ExcelPropertySheet modelPropertySheet = modelExcelPropertyFile.nextExcelPropertySheet()
                    ExcelPropertySheet newPropertySheet = createPropertySheetFromPropertiesFileUsingModel(outputExcelPropertyFile, modelPropertySheet)
                    movePropertiesIntoPropertySheetUsingModelSheet(newPropertySheet, modelPropertySheet)
                }
                outputExcelPropertyFile.writeAndClose()
            }
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
        int propIndex = 1
        while (translationProperties.hasNext()) {
            def property = translationProperties.next()
            def propertyKey = property.getKey()
            ExcelPropertyRow modelPropertyRow = modelPropertySheet.getFirstExcelPropertyRowMatchingKeys(["Message Key": propertyKey])
            if (modelPropertyRow != null)
                updateTranslationInRow(newPropertySheet, property, modelPropertyRow, propIndex)
            else
                populateNewRowFromTranslation(newPropertySheet, property, propIndex)
            def sheetName = newPropertySheet.sheetName
            propIndex++
        }
    }

    def updateTranslationInRow(ExcelPropertySheet newPropertySheet, property, ExcelPropertyRow modelPropertyRow, int propIndex) {
        println "updating property for ${newPropertySheet.sheetName} = ${property.getKey()} : value = ${property.getValue()}"
        ExcelPropertyRow newPropertyRow = newPropertySheet.cloneExcelPropertyRow(propIndex, modelPropertyRow)
        def oldEnglishValue = modelPropertyRow.getValue("English")
        def newEnglishValue = property.getValue()
        newPropertyRow.setValue("Index", propIndex)
        if (oldEnglishValue != newEnglishValue) {
            newPropertyRow.setValue("English", newEnglishValue)
            def today = Calendar.getInstance().time
            newPropertyRow.setValue("Date Changed", today)
            newPropertyRow.setStyle("Date Changed", getDateStyle(newPropertySheet))
        }
    }

    def getDateStyle(ExcelPropertySheet excelPropertySheet) {
        Workbook wb = excelPropertySheet.workbook
        CellStyle dateCellStyle = wb.createCellStyle()
        short dateFormat = wb.createDataFormat().getFormat("mm/dd/yyyy")
        dateCellStyle.setDataFormat(dateFormat)
        dateCellStyle
    }

    def populateNewRowFromTranslation(ExcelPropertySheet newPropertySheet, property, int propIndex) {
        println "new property for ${newPropertySheet.sheetName} = ${property.getKey()} : value = ${property.getValue()}"
        def propertyMap = [:]
        propertyMap.put("Index", propIndex)
        def propertyId = (property.getKey())[0] == "*" ? "" : property.getKey()
        propertyMap.put("Message Key", propertyId)
        propertyMap.put("English", property.getValue())
        propertyMap.put(newPropertySheet.getLanguage(), "")
        newPropertySheet.addRow(propIndex, propertyMap)
    }
}