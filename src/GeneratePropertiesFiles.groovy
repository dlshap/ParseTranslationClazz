import i18n.LanguageLabels
import properties.ExcelPropertyFile
import properties.ExcelPropertyRow
import properties.ExcelPropertySheet
import properties.PropertyFile
import useful.Args
import i18n.Messages

/**
 * Created by s0041664 on 8/25/2017.
 */

class GeneratePropertiesFiles {

    def startFilePath        // "root" filepath
    def languageName         // language for this translation

    static final SPREADSHEET_PROMPT = "prompt.for.translation.spreadsheet.for"
//    static final PROP_DIR_PROMPT = "prompt.for.directory.for"

    static main(args) {
        new GeneratePropertiesFiles(args)
    }

    GeneratePropertiesFiles(args) {
        start(args)
    }

    def start(args) {
        buildArgsAndParameters(args)
        generateTranslationsFromSpreadsheetToPropertiesFiles()
    }

    def buildArgsAndParameters(args) {
        getArgValues(args)
        getDefaultValuesIfArgsNull()
    }

    def getArgValues(args) {
        def argsMap = new Args(args)
        languageName = argsMap.get("language")
        startFilePath = argsMap.get("path")
    }

    def getDefaultValuesIfArgsNull() {
        if (startFilePath == null) startFilePath = "C:\\\\Users\\\\s0041664\\\\Documents\\\\Projects\\\\DMT-DE\\\\Project Work\\\\translations\\\\"
        if (languageName == null) languageName = "Japanese"
    }

    def generateTranslationsFromSpreadsheetToPropertiesFiles() {
        ExcelPropertyFile excelPropertyFile = choosePropertiesSpreadsheet()
        if (excelPropertyFile != null)
            movePropertiesFromSpreadsheetsToPropertiesFiles(excelPropertyFile)
    }

    def choosePropertiesSpreadsheet() {
        def prompt = Messages.getString(SPREADSHEET_PROMPT, languageName)
        def excelPath = startFilePath + "\\Spreadsheets\\PropertySpreadsheets\\DMTDE\\"
        ExcelPropertyFile excelPropertyFile = ExcelPropertyFile.openUsingChooser(prompt, excelPath)
        excelPropertyFile
    }

    def movePropertiesFromSpreadsheetsToPropertiesFiles(ExcelPropertyFile excelPropertyFile) {
        while (excelPropertyFile.hasNextExcelPropertySheet()) {
            ExcelPropertySheet excelPropertySheet = excelPropertyFile.nextExcelPropertySheet()
            movePropertiesFromSpreadsheetToPropertiesFile(excelPropertySheet)
        }
    }

    def movePropertiesFromSpreadsheetToPropertiesFile(ExcelPropertySheet excelPropertySheet) {
        PropertyFile propertyFile = openPropertyFileForSheetName(excelPropertySheet.sheetName)
        while (excelPropertySheet.hasNextExcelPropertyRow()) {
            writePropertyRowToPropertyFile(excelPropertySheet.nextExcelPropertyRow(), propertyFile)
        }
    }

    def openPropertyFileForSheetName(String sheetName) {
        def propFilePath = startFilePath + "\\${sheetName}\\"
        def languageLabel = LanguageLabels.getPropertiesLabel(languageName)
        def fileName = "messages_${languageLabel}.properties"
        PropertyFile propertyFile = PropertyFile.createNewTranslationPropertyFileFromPathAndFile(propFilePath, fileName)
        propertyFile
    }

    def writePropertyRowToPropertyFile(ExcelPropertyRow excelPropertyRow, PropertyFile propertyFile) {
        String outLine
        def propertyValueMap = excelPropertyRow.propertyMap
        def propertyId = propertyValueMap.get("Message Key")
        def propertyValue = propertyValueMap.get(languageName)

        if ((propertyId != null) && (propertyId.trim() != "") && (propertyId[0] != "#"))
            outLine = "$propertyId=${propertyValue == null ? '' : propertyValue}"
        else
            outLine = "${propertyId == null ? '' : propertyId}"

        propertyFile.writeLine(outLine)
    }
}