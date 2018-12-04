import filemanagement.PropertyFile
import filemanagement.TranslationsExcelExportFile
import logging.Dates
import logging.Log
import translations.IgnorePropertyList
import translations.Properties
import translations.Translations
import translations.Translation
import useful.ArgsParser

/**
 * Created by s0041664 on 8/25/2017.
 */
class UpdateDmtDeTranslatedProperties {

    static startFilePath        // "root" filepath
    static languageName         // language for this translation

    static componentList = ["DMT", "DE"]       // list for looping through components
    static componentFilePath                    // filepath for individual component
    static componentName                        // name of component from component list for translations

    static TranslationsExcelExportFile translationsExcelExportFile
    static Translations translationsFromExcelExport
    static PropertyFile propertyFile
    static Properties propertiesFromPropertyFile
    static IgnorePropertyList ignorePropertyList

    static main(args) {
        buildArgsAndParameters(args)
        moveTranslationsFromExcelExportToPropertiesFiles()
    }

    static buildArgsAndParameters(args) {
        getArgValues(args)
        getDefaultValues()
        println languageName
        println startFilePath
    }

    static getArgValues(args) {
        def argsMap = new ArgsParser(args)
        languageName = argsMap.get("language")
        startFilePath = argsMap.get("path")
    }

    static getDefaultValues() {
        if (startFilePath == null) startFilePath = "C:\\\\Users\\\\s0041664\\\\Documents\\\\Projects\\\\DMT-DE\\\\Project Work\\\\translations\\\\"
        if (languageName == null) languageName = "Japanese"
    }

    static moveTranslationsFromExcelExportToPropertiesFiles() {
        componentList.each { comp ->
            componentName = comp
            doTranslationsForComponent()
        }
    }

    static doTranslationsForComponent() {
        buildFilePathForComponent()
        buildIgnorePropertyList()
        openTranslationLogsForComponent()
        moveTranslationsToPropertiesForComponent()
    }

    static buildFilePathForComponent() {
        componentFilePath = startFilePath + componentName + "\\\\"
    }

    static buildIgnorePropertyList() {
        ignorePropertyList = new IgnorePropertyList(componentFilePath + "logs\\\\ignoreMessages.txt")
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
        // first build translations object...if that's ok
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
        translationsFromExcelExport = new Translations(translationsExcelExportFile)
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
        // loop through property value translations from Excel export
        while (translationsFromExcelExport.hasNext()) {
            updateAPropertyFromAnExcelExportRow()
        }
    }

    static updateAPropertyFromAnExcelExportRow() {
        def nextExcelExportRow = translationsFromExcelExport.next()
        def nextTranslationValue = nextExcelExportRow[languageName].trim()
        def nextTranslationKey = nextExcelExportRow["Message Key"].trim()
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
        Log.writeLine("exceptions", "\r\n******* No $languageName translation in Excel export:")
        def noTranslationList = translationsFromExcelExport.getTranslations("$languageName", "")
        if (noTranslationList != null) {
            noTranslationList.each {
                def translationKey = it.get("Message Key")
                if ((translationKey != "") && (translationKey.charAt(0) != "#") && (!(ignorePropertyList.contains(translationKey)))) {
                    Log.writeLine("exceptions", "Property '$translationKey' has no $languageName translation in Excel export.")
                }
            }
        }
    }

    static logPropertiesWithNoTranslations() {
//      loop through all properties (iterator), find matching translation from Excel export if it exists; otherwise log missing translation
        propertiesFromPropertyFile.rewind()
        while (propertiesFromPropertyFile.hasNext()) {
            logIfNoMatchingExcelExportTranslation()
        }
    }

    static logIfNoMatchingExcelExportTranslation() {
        def nextProperty = propertiesFromPropertyFile.next()
        String nextPropertyKey = nextProperty.key
        if ((nextPropertyKey.charAt(0) != "*") && (!(ignorePropertyList.contains(nextPropertyKey)))) {
            //pseudo-properties (comments) have '*' in first character (maybe should trim left?)
            Translation matchingExcelExportTranslation = translationsFromExcelExport.getTranslation("Message Key", nextPropertyKey)
            if (matchingExcelExportTranslation == null)
                Log.writeLine("exceptions", "Property '$nextPropertyKey' does not have corresponding 'Message Key' in translation Excel export.")
            else if (matchingExcelExportTranslation.get(languageName) == null)
                Log.writeLine("exceptions", "Property '$nextPropertyKey' in property file, but no $languageName translation in translation Excel export.")
        }
    }
}
