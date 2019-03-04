import properties.ExcelPropertyFile
import properties.PropertyFile
import logging.Dates
import logging.Log
import translations.IgnorePropertyList
import translations.TranslationProperties_old
import translations.Translations
import translations.Translation
import useful.Args
import i18n.Messages

/**
 * Created by s0041664 on 8/25/2017.
 */
class UpdateDmtDeTranslatedProperties_old {

    def startFilePath        // "root" filepath
    def languageName         // language for this translation

    static final SPREADSHEET_PROMPT = "prompt.for.master.spreadsheet.for.component"


    def componentList = ["DMT", "DE"]       // list for looping through components


    def ExcelPropertyFile translationsExcelFile
    def Translations translationsFromSpreadsheet
    def PropertyFile propertyFile
    def TranslationProperties_old propertiesFromPropertyFile
    def IgnorePropertyList ignorePropertyList

    static main(args) {
        new UpdateDmtDeTranslatedProperties_old(args)
    }

    UpdateDmtDeTranslatedProperties_old(args) {
        start(args)
    }

    def start(args) {
        buildArgsAndParameters(args)
        moveTranslationsFromSpreadsheetToPropertiesFiles()
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

    def moveTranslationsFromSpreadsheetToPropertiesFiles() {
        componentList.each { componentName ->
            doTranslationsForComponent(componentName)
        }
    }

    def doTranslationsForComponent(componentName) {
        def componentFilePath = buildFilePathForComponent(componentName)
        buildIgnorePropertyListForComponentFromIgnoreFile(componentName, componentFilePath)
        openTranslationLogsForComponent(componentName, componentFilePath)
        moveTranslationsToPropertiesForComponent(componentName, componentFilePath)
    }

    def buildFilePathForComponent(componentName) {
        startFilePath + componentName + "\\\\"
    }

    def buildIgnorePropertyListForComponentFromIgnoreFile(componentName, componentFilePath) {
        ignorePropertyList = new IgnorePropertyList(componentFilePath + "logs\\\\ignoreMessages.txt")
    }

    def openTranslationLogsForComponent(componentName, componentFilePath) {
        def logsFilePath = componentFilePath + "logs\\\\"
        //default log: property translations (successful) log
        Log.open(logsFilePath + "$componentName log-property-translations.txt")
        Log.writeLine "Running on " + Dates.currentDateAndTime() + ":\r\n"
        //exceptions log: property exceptions log
        Log.open("exceptions", logsFilePath + "$componentName log-property-exceptions.txt")
        Log.writeLine "exceptions", "Running on " + Dates.currentDateAndTime() + ":\r\n"
    }

    def moveTranslationsToPropertiesForComponent(componentName, componentFilePath) {
        if (buildPropertiesAndTranslationsObjectsForComponent(componentName, componentFilePath)) {
            copyTranslations()
            logMissingTranslations()
        }
    }

    def buildPropertiesAndTranslationsObjectsForComponent(componentName, componentFilePath) {
        // first build translations object...if that's ok
        if (buildTranslationsObject(componentName))
            buildPropertiesObject(componentName, componentFilePath)
    }

    def buildTranslationsObject(componentName) {
        translationsExcelFile = openTranslationsExcelFile(componentName)
        if (translationsExcelFile != null)
            buildTranslationsFromExcelFile(componentName)
    }

    def openTranslationsExcelFile(componentName) {
        def excelFilePath = startFilePath + "Spreadsheets\\PropertySpreadsheets\\DMTDE"
        def chooserPrompt = Messages.getString(SPREADSHEET_PROMPT, componentName, languageName)
//        ExcelPropertyFile.getExcelPropertyFileUsingChooser(chooserPrompt, excelFilePath)
        ExcelPropertyFile.openUsingChooser(chooserPrompt, excelFilePath)
    }

    def buildTranslationsFromExcelFile(componentName) {
        translationsFromSpreadsheet = Translations.createTranslationsFromExcelPropertiesFile(translationsExcelFile, componentName)
    }

    def buildPropertiesObject(componentName, componentFilePath) {
        if (openPropertyFile(componentName, componentFilePath))
            buildPropertiesFromPropertyFile()
    }

    def openPropertyFile(componentName, componentFilePath) {
        propertyFile = PropertyFile.createPropertyFileForComponentUsingChooser(componentName, componentFilePath)
        (propertyFile.file != null)                      // return true if there is a file (open was successful)
    }

    def buildPropertiesFromPropertyFile() {
        //get property list
        propertiesFromPropertyFile = new TranslationProperties_old(propertyFile)
    }

    def copyTranslations() {
        updatePropertiesFromTranslations()
        propertiesFromPropertyFile.writePropertiesToTranslatedOutputFile()
    }

    def updatePropertiesFromTranslations() {
        // loop through property value translations from Spreadsheet
        while (translationsFromSpreadsheet.hasNext()) {
            updateAPropertyFromASpreadsheetRow()
        }
    }

    def updateAPropertyFromASpreadsheetRow() {
        def nextSpreadsheetTranslation = translationsFromSpreadsheet.next()
        def nextTranslationKey = nextSpreadsheetTranslation.get("Message Key")
        def nextTranslationValue = nextSpreadsheetTranslation.get(languageName)
        if ((nextTranslationValue != null)  && (!(nextTranslationKey == null || nextTranslationKey.charAt(0) == "#")))
            replaceOriginalValueWithNewValue(nextTranslationKey, nextTranslationValue)
    }

    def replaceOriginalValueWithNewValue(nextTranslationKey, nextTranslationValue) {
        def originalPropertyValue = propertiesFromPropertyFile.get(nextTranslationKey)
        if (originalPropertyValue != null) {
            if (!originalPropertyValue.equals(nextTranslationValue)) {
                propertiesFromPropertyFile.set(nextTranslationKey, nextTranslationValue)
                Log.writeLine("Property $nextTranslationKey: '$originalPropertyValue' replaced by '$nextTranslationValue'")
            }
        } else {
            Log.writeLine("exceptions", "Translated property '$nextTranslationKey' not found in properties file")
        }
    }

    def logMissingTranslations() {
        Log.writeLine("exceptions", "\r\n******* Missing translation keys or values:")
        logTranslationKeysWithNoValues()
        logPropertiesWithNoTranslations()
    }

    def logTranslationKeysWithNoValues() {
        // write exceptions accumulated while moving translations into properties file
        Log.writeLine("exceptions", "\r\n******* No $languageName translation in Spreadsheet:")
        def noTranslationList = translationsFromSpreadsheet.getTranslations("$languageName", "")
        if (noTranslationList != null) {
            noTranslationList.each { Translation it ->
                def translationKey = it.get("Message Key")
                if ((translationKey != "") && (translationKey.charAt(0) != "#") && (!(ignorePropertyList.contains(translationKey)))) {
                    Log.writeLine("exceptions", "Property '$translationKey' has no $languageName translation in Spreadsheet.")
                }
            }
        }
    }

    def logPropertiesWithNoTranslations() {
//      loop through all properties (iterator), find matching translation from Spreadsheet if it exists; otherwise log missing translation
        propertiesFromPropertyFile.rewind()
        while (propertiesFromPropertyFile.hasNext()) {
            logIfNoMatchingSpreadsheetTranslation()
        }
    }

    def logIfNoMatchingSpreadsheetTranslation() {
        def nextProperty = propertiesFromPropertyFile.next()
        String nextPropertyKey = nextProperty.key
        if ((nextPropertyKey.charAt(0) != "*") && (!(ignorePropertyList.contains(nextPropertyKey)))) {
            //pseudo-properties (comments) have '*' in first character (maybe should trim left?)
            Translation matchingSpreadsheetTranslation = translationsFromSpreadsheet.getTranslation("Message Key", nextPropertyKey)
            if (matchingSpreadsheetTranslation == null)
                Log.writeLine("exceptions", "Property '$nextPropertyKey' does not have corresponding 'Message Key' in translation Spreadsheet.")
            else {
                def test1 = matchingSpreadsheetTranslation.get(languageName)
                if (matchingSpreadsheetTranslation.get(languageName) == null) {
                    println nextPropertyKey
                    Log.writeLine("exceptions", "Property '$nextPropertyKey' in property file, but no $languageName translation in translation Spreadsheet.")
                }
            }
        }
    }
}
