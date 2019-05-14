import filemanagement.KeyFile
import i18n.LanguageLabels
import logging.Dates
import logging.Log
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
        def prompt = Messages.getString(SPREADSHEET_PROMPT, "message properties", languageName)
        def excelPath = startFilePath + "\\Spreadsheets\\PropertySpreadsheets\\DMTDE\\"
        ExcelPropertyFile excelPropertyFile = ExcelPropertyFile.openFileUsingChooser(prompt, excelPath)
        excelPropertyFile
    }

    def movePropertiesFromSpreadsheetsToPropertiesFiles(ExcelPropertyFile excelPropertyFile) {
        while (excelPropertyFile.hasNextExcelPropertySheet()) {
            ExcelPropertySheet excelPropertySheet = excelPropertyFile.nextExcelPropertySheet()
            openTranslationLogsForSheet(excelPropertySheet.sheetName)
            movePropertiesFromSpreadsheetToPropertiesFile(excelPropertySheet)
        }
    }

    def openTranslationLogsForSheet(String sheetName) {
        def logsFilePath = startFilePath + "\\$sheetName\\logs\\"
        Log.open("adds", logsFilePath + "$sheetName log-property-adds.txt")
        Log.writeLine "adds", "Running on " + Dates.currentDateAndTime() + ":\r\n"
        Log.open("updates", logsFilePath + "$sheetName log-property-changes.txt")
        Log.writeLine "updates", "Running on " + Dates.currentDateAndTime() + ":\r\n"
        Log.open("deletes", logsFilePath + "$sheetName log-property-deletes.txt")
        Log.writeLine "deletes", "Running on " + Dates.currentDateAndTime() + ":\r\n"
    }

    def movePropertiesFromSpreadsheetToPropertiesFile(ExcelPropertySheet excelPropertySheet) {
        PropertyFile newPropertyFile = createNewPropertyFileForSheetName(excelPropertySheet.sheetName)
        KeyFile oldPropertyFile = openOldPropertyFileForSheetName(excelPropertySheet.sheetName)
        while (excelPropertySheet.hasNextExcelPropertyRow()) {
            ExcelPropertyRow excelPropertyRow = excelPropertySheet.nextExcelPropertyRow()
            writePropertyRowToPropertyFile(excelPropertyRow, newPropertyFile)
            logPropertyAddOrChange(excelPropertyRow, oldPropertyFile)
        }
        logOldFilePropertiesDeletedFromNewFile(oldPropertyFile, newPropertyFile)
    }

    PropertyFile createNewPropertyFileForSheetName(String sheetName) {
        def propFilePath = startFilePath + "\\${sheetName}\\"
        def languageLabel = LanguageLabels.getPropertiesLabel(languageName)
        def fileName = "messages_${languageLabel}.properties"
        PropertyFile propertyFile = PropertyFile.createNewTranslationPropertyFileFromPathAndFile(propFilePath, fileName)
        propertyFile
    }

    KeyFile openOldPropertyFileForSheetName(String sheetName) {
        def propFilePath = startFilePath + "\\${sheetName}\\PropertyFiles\\"
        def languageLabel = LanguageLabels.getPropertiesLabel(languageName)
        def fileName = "messages_${languageLabel}.properties"
        KeyFile propertyFile = new KeyFile(propFilePath + fileName)
        propertyFile
    }

    def writePropertyRowToPropertyFile(ExcelPropertyRow excelPropertyRow, PropertyFile propertyFile) {
        def propertyId = getRowId(excelPropertyRow)
        def propertyValue = getRowValue(excelPropertyRow)
        String outLine
        if ((propertyId != null) && (propertyId.trim() != "") && (propertyId[0] != "#"))
            outLine = "$propertyId=${propertyValue == null ? '' : propertyValue}"
        else
            outLine = "${propertyId == null ? '' : propertyId}"
        propertyFile.writeLine(outLine)
    }

    String getRowId(ExcelPropertyRow excelPropertyRow) {
        def propertyValueMap = excelPropertyRow.propertyMap
        propertyValueMap.get("Message Key")
    }

    String getRowValue(ExcelPropertyRow excelPropertyRow) {
        def propertyValueMap = excelPropertyRow.propertyMap
        propertyValueMap.get(languageName)
    }

    def logPropertyAddOrChange(ExcelPropertyRow excelPropertyRow, KeyFile oldPropertyFile) {
        def propertyId = getRowId(excelPropertyRow)
        def oldPropertyValue = (oldPropertyFile.get(propertyId))
        def newPropertyValue = getRowValue(excelPropertyRow)
        if (oldPropertyValue == null && newPropertyValue.trim() != "")
            Log.writeLine "adds", "Adding $propertyId=$newPropertyValue"
        else if (!(oldPropertyValue.equals(newPropertyValue)) && newPropertyValue.trim() != "")
            Log.writeLine("updates", "Changing $propertyId from $oldPropertyValue to $newPropertyValue")
    }

    def logOldFilePropertiesDeletedFromNewFile(KeyFile oldPropertyFile, PropertyFile newUnkeyedPropertyFile) {
        def newPropertyFileName = newUnkeyedPropertyFile.fullName
        KeyFile newPropertyFile = new KeyFile(newPropertyFileName)
        oldPropertyFile.keyMap.each { oldKey, oldValue ->
            if (newPropertyFile.keyMap.get(oldKey) == null)
                Log.writeLine("deletes", "Deleted from old property file: $oldKey=$oldValue")
        }
    }
}