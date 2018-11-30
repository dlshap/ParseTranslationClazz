import filemanagement.PropertyFile
import filemanagement.TranslationsExcelExportFile
import logging.Dates
import logging.Log
import translations.Properties
import translations.Translations
import translations.Translation

import static useful.ArgsParser.buildArgsMap


/**
 * Created by s0041664 on 8/25/2017.
 */
class UpdateDmtDeTranslatedProperties {

    static argsMap = [:]        // parsed map of command line args (key=value -> [key:value])
    static startFilePath        // "root" filepath
    static languageName         // language for this translation

    static componentList        // list for looping through components
    static componentFilePath    // filepath for individual component
    static componentName        // name of component from component list for translations

    static TranslationsExcelExportFile translationsExcelExportFile
    static Translations translationsFromSpreadsheet
    static PropertyFile propertyFile
    static Properties propertiesFromPropertyFile

    static main(args) {
        buildArgsAndParameters(args)
        moveTranslationsFromExcelExportToPropertiesFiles()
    }

    static buildArgsAndParameters(args) {
        argsMap = buildArgsMap(args)
        startFilePath = buildStartFilePath()
        languageName = argsMap["language"]
        componentList = ["DMT", "DE"]
    }

    static buildStartFilePath() {
        def thePath = argsMap.get("path")
        if (thePath == null) thePath = "C:\\\\Users\\\\s0041664\\\\Documents\\\\Projects\\\\DMT-DE\\\\Project Work\\\\translations\\\\"
        thePath
    }

    static moveTranslationsFromExcelExportToPropertiesFiles() {
        componentList.each { comp ->
            componentName = comp
            doTranslationsForComponent()
        }
    }

    static doTranslationsForComponent() {
        buildFilePathForComponent()
        openTranslationLogsForComponent()
        moveTranslationsToPropertiesForComponent()
    }

    static buildFilePathForComponent() {
        componentFilePath = startFilePath + componentName + "\\\\"
    }

    static openTranslationLogsForComponent() {
        def logsFilePath = componentFilePath + "logs\\\\"
        //default log: property translations (successful) log
        Log.open(logsFilePath + "$componentName log-property-translations.txt")
        Log.writeLine "Running on " + Dates.currentDateAndTime() + ":\r\n"
        //exceptions log: property exceptions log
        Log.open("exceptions", logsFilePath + "$componentName log-property-exceptions.txt")
        Log.writeLine "exceptions", "Running on " + Dates.currentDateAndTime() + ":\r\n"
    }

    static moveTranslationsToPropertiesForComponent() {
        if (buildPropertiesAndTranslationsObjects()) {
            copyTranslations()
            logMissingTranslations()
        }
    }

    static buildPropertiesAndTranslationsObjects() {
        if (buildTranslationsObject())
            buildPropertiesObject()
    }

    static buildTranslationsObject() {
        if (openTranslationsExcelExportFile())
            buildTranslationsFromExport()
    }

    static buildPropertiesObject() {
        if (openPropertyFile())
            buildPropertiesFromPropertyFile()
    }

    static openTranslationsExcelExportFile() {
        // open property file
        translationsExcelExportFile = new TranslationsExcelExportFile(componentName, componentFilePath)
        (translationsExcelExportFile.theFile != null)       // return true if there is a file (open was successful)
    }

    static buildTranslationsFromExport() {
        translationsFromSpreadsheet = new Translations(translationsExcelExportFile)
    }

    static openPropertyFile() {
        propertyFile = new PropertyFile(componentName, componentFilePath)
        (propertyFile.theFile != null)                      // return true if there is a file (open was successful)
    }

    static buildPropertiesFromPropertyFile() {
        //get property list
        propertiesFromPropertyFile = new Properties(propertyFile)
    }

    static copyTranslations() {
        updatePropertiesFromTranslations()
        propertiesFromPropertyFile.writePropertiesToTranslatedOutputFile()
    }

    static updatePropertiesFromTranslations() {
        // loop through property value translations from Excel spreadsheet
        while (translationsFromSpreadsheet.hasNext()) {
            updateAPropertyFromASpreadsheetRow()
        }
    }

    static updateAPropertyFromASpreadsheetRow() {
        def nextSpreadsheetRow = translationsFromSpreadsheet.next()
        def nextTranslationValue = nextSpreadsheetRow[languageName].trim()
        def nextTranslationKey = nextSpreadsheetRow["Message Key"].trim()
        if ((nextTranslationValue != null) && (nextTranslationValue != "") && (!(nextTranslationKey == "" || nextTranslationKey[0] == "#")))
            replaceOriginalValueWithNewValue(nextTranslationKey, nextTranslationValue)
    }

    static replaceOriginalValueWithNewValue(nextTranslationKey, nextTranslationValue) {
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

    static logMissingTranslations() {
        Log.writeLine("exceptions", "\r\n******* Missing translation keys or values:")
        logTranslationKeysWithNoValues()
        logPropertiesWithNoTranslations()
    }

    static logTranslationKeysWithNoValues() {
        // write exceptions accumulated while moving translations into properties file
        Log.writeLine("exceptions", "\r\n******* No $languageName translation in spreadsheet:")
        def noTranslationList = translationsFromSpreadsheet.getTranslations("$languageName", "")
        if (noTranslationList != null) {
            noTranslationList.each {
                def translationKey = it.get("Message Key")
                if ((translationKey != "") && (translationKey[0] != "#")) {
                    Log.writeLine("exceptions", "Property $translationKey has no $languageName translation in spreadsheet.")
                }
            }
        }
    }

    static logPropertiesWithNoTranslations() {
//      loop through all properties (iterator), find matching translation from spreadsheet if it exists; otherwise log missing translation
        propertiesFromPropertyFile.rewind()
        while (propertiesFromPropertyFile.hasNext()) {
            logIfNoMatchingSpreadsheetTranslation()
        }
    }

    static logIfNoMatchingSpreadsheetTranslation() {
        def nextProperty = propertiesFromPropertyFile.next()
        def nextPropertyKey = nextProperty.key
        if (nextPropertyKey[0] != "*") {
            //pseudo-properties (comments) have '*' in first character (maybe should trim left?)
            Translation matchingSpreadsheetTranslation = translationsFromSpreadsheet.getTranslation("Message Key", nextPropertyKey)
            if (matchingSpreadsheetTranslation == null)
                Log.writeLine("exceptions", "Property '$nextPropertyKey' does not have corresponding 'Message Key' in translation spreadsheet.")
            else if (matchingSpreadsheetTranslation.get(languageName) == null)
                Log.writeLine("exceptions", "Property '$nextPropertyKey' in property file, but no $languageName translation in translation spreadsheet.")
        }
    }
}
