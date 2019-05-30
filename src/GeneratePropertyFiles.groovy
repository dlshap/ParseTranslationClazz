import filemanagement.KeyFile
import i18n.LanguageLabels
import logging.Log
import logging.LogUtils
import properties.ExcelPropertyFile
import properties.ExcelPropertyRow
import properties.ExcelPropertySheet
import properties.PropertyArgs
import properties.PropertyFile
import i18n.Messages

/**
 * Created by s0041664 on 8/25/2017.
 */

class GeneratePropertyFiles {

//    private String path        // "root" filepath
//    private String language         // language for this translation
    private PropertyArgs propertyArgs

    static final SPREADSHEET_PROMPT = "prompt.for.translation.spreadsheet.for"

    static main(args) {
        new GeneratePropertyFiles(args)
    }

    GeneratePropertyFiles(args) {
        start(args)
    }

    private start(args) {
        propertyArgs = new PropertyArgs(args)
//        buildArgsAndParameters(args)
        if (!(LanguageLabels.isLanguageInList(this.propertyArgs.language))) {
            Log.writeLine "app", "ERROR: \"${this.propertyArgs.language}\" is not in language list"
        } else {
            generateTranslationsFromSpreadsheetToPropertiesFiles()
        }


    }

//    private buildArgsAndParameters(args) {
//        getArgValues(args)
//        getDefaultValuesIfArgsNull()
//    }
//
//    private getArgValues(args) {
//        def argsMap = new Args(args)
//        language = argsMap.get("language")
//        path = argsMap.get("path")
//    }
//
//    def getDefaultValuesIfArgsNull() {
//        if (path == null) path = "C:\\\\Users\\\\s0041664\\\\Documents\\\\Projects\\\\DMT-DE\\\\Project Work\\\\translations\\\\"
//        if (language == null) language = "Japanese"
//    }

    private generateTranslationsFromSpreadsheetToPropertiesFiles() {
        ExcelPropertyFile excelPropertyFile = choosePropertiesSpreadsheet()
        if (excelPropertyFile != null) {
            movePropertiesFromSpreadsheetsToPropertiesFiles(excelPropertyFile)
        }
    }

    private ExcelPropertyFile choosePropertiesSpreadsheet() {
        def prompt = Messages.getString(SPREADSHEET_PROMPT, "message properties", propertyArgs.language)
        def excelPath = propertyArgs.spreadsheetPath
        ExcelPropertyFile excelPropertyFile = ExcelPropertyFile.openFileUsingChooser(prompt, excelPath)
        excelPropertyFile
    }

    private movePropertiesFromSpreadsheetsToPropertiesFiles(ExcelPropertyFile excelPropertyFile) {
        while (excelPropertyFile.hasNextExcelPropertySheet()) {
            ExcelPropertySheet excelPropertySheet = excelPropertyFile.nextExcelPropertySheet()
            openTranslationLogsForSheet(excelPropertySheet.sheetName)
            movePropertiesFromSpreadsheetToPropertiesFile(excelPropertySheet)
        }
    }

    private openTranslationLogsForSheet(String sheetName) {
        def logsFilePath = propertyArgs.basePath + "\\$sheetName\\logs\\"
        LogUtils.openLogs(logsFilePath, "${propertyArgs.language}-$sheetName-property")
    }

    private movePropertiesFromSpreadsheetToPropertiesFile(ExcelPropertySheet excelPropertySheet) {
        PropertyFile newPropertyFile = createNewPropertyFileForSheetName(excelPropertySheet.sheetName)
        KeyFile oldPropertyFile = openOldPropertyFileForSheetName(excelPropertySheet.sheetName)
        while (excelPropertySheet.hasNextExcelPropertyRow()) {
            ExcelPropertyRow excelPropertyRow = excelPropertySheet.nextExcelPropertyRow()
            writePropertyRowToPropertyFile(excelPropertyRow, newPropertyFile)
            logPropertyAddOrChange(excelPropertyRow, oldPropertyFile)
        }
        logOldFilePropertiesDeletedFromNewFile(oldPropertyFile, newPropertyFile)
    }

    private PropertyFile createNewPropertyFileForSheetName(String sheetName) {
        def propFilePath = propertyArgs.propertyFilePath + "\\${sheetName}\\PropertyFiles\\new\\"
        def languageLabel = LanguageLabels.getPropertiesLabel(propertyArgs.language)
        def fileName = "messages_${languageLabel}_new.properties"
        PropertyFile propertyFile = PropertyFile.createNewTranslationPropertyFileFromFileName(propFilePath + fileName)
        propertyFile
    }

    private KeyFile openOldPropertyFileForSheetName(String sheetName) {
        def propFilePath = propertyArgs.propertyFilePath + "\\${sheetName}\\PropertyFiles\\"
        def languageLabel = LanguageLabels.getPropertiesLabel(propertyArgs.language)
        def fileName = "messages_${languageLabel}.properties"
        KeyFile propertyFile = new KeyFile(propFilePath + fileName)
        propertyFile
    }

    private writePropertyRowToPropertyFile(ExcelPropertyRow excelPropertyRow, PropertyFile propertyFile) {
        def propertyId = getRowId(excelPropertyRow)
        def propertyValue = getRowTranslatedValue(excelPropertyRow)
        String outLine
        if ((propertyId != null) && (propertyId.trim() != "") && (propertyId[0] != "#")) {
            if (propertyValue == null || propertyValue == "") {
                propertyValue = getRowEnglishValue(excelPropertyRow) + " (needs translation)"
                Log.writeLine "adds", "Adding $propertyId=$propertyValue"
            }
            outLine = "$propertyId=$propertyValue"
        } else {
            outLine = "${propertyId == null ? '' : propertyId}"
        }
        propertyFile.writeLine(outLine)
    }

    private String getRowId(ExcelPropertyRow excelPropertyRow) {
        def propertyValueMap = excelPropertyRow.propertyMap
        propertyValueMap.get("Message Key")
    }

    private String getRowTranslatedValue(ExcelPropertyRow excelPropertyRow) {
        def propertyValueMap = excelPropertyRow.propertyMap
        propertyValueMap.get(propertyArgs.language)
    }

    private String getRowEnglishValue(ExcelPropertyRow excelPropertyRow) {
        def propertyValueMap = excelPropertyRow.propertyMap
        propertyValueMap.get("English")
    }

    private logPropertyAddOrChange(ExcelPropertyRow excelPropertyRow, KeyFile oldPropertyFile) {
        def propertyId = getRowId(excelPropertyRow)
        def oldPropertyValue = (oldPropertyFile.get(propertyId))
        def newPropertyValue = getRowTranslatedValue(excelPropertyRow)
        if (oldPropertyValue == null && newPropertyValue.trim() != "")
            Log.writeLine "adds", "Adding $propertyId=$newPropertyValue"
        else if (!(oldPropertyValue == newPropertyValue) && newPropertyValue.trim() != "")
            Log.writeLine("updates", "Changing $propertyId from $oldPropertyValue to $newPropertyValue")
    }

    private logOldFilePropertiesDeletedFromNewFile(KeyFile oldPropertyFile, PropertyFile newUnkeyedPropertyFile) {
        def newPropertyFileName = newUnkeyedPropertyFile.fullName
        KeyFile newPropertyFile = new KeyFile(newPropertyFileName)
        oldPropertyFile.keyMap.each { oldKey, oldValue ->
            if (newPropertyFile.keyMap.get(oldKey) == null) {
                Log.writeLine("deletes", "Deleted from old property file: $oldKey=$oldValue")
            }
        }
    }
}