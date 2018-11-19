import filemanagement.PropertyFile
import filemanagement.TranslationsExcelExportFile
import logging.Dates
import logging.Log
import translations.Properties
import translations.Translations
import translations.Translation


/**
 * Created by s0041664 on 8/25/2017.
 */
class UpdateDmtDeTranslatedProperties {

    static openLogs(component, rootFilePath) {
        def logsFilePath = rootFilePath + "logs\\\\"
        //default log: property translations (successful) log
        Log.open(logsFilePath + "$component log-property-translations.txt")
        Log.writeLine "Running on " + Dates.currentDateAndTime() + ":\r\n"
        //exceptions log: property exceptions log
        Log.open("exceptions", logsFilePath + "$component log-property-exceptions.txt")
        Log.writeLine "exceptions", "Running on " + Dates.currentDateAndTime() + ":\r\n"
    }

    static updatePropertiesFromTranslations(Translations translationsFromSpreadsheet, Properties properties, languageName) {
        // loop through property value translations from Excel spreadsheet
        while (translationsFromSpreadsheet.hasNext()) {
            def nextSpreadsheetRow = translationsFromSpreadsheet.next()
            def nextTranslatedValue = nextSpreadsheetRow[languageName].trim()
            if ((nextTranslatedValue != null) && (nextTranslatedValue.trim() != "")) {
                def nextSpreadsheetTranslationKey = nextSpreadsheetRow["Message Key"]
                if (!(nextSpreadsheetTranslationKey == "" || nextSpreadsheetTranslationKey[0] == "#")) {
                    def originalPropertyValue = properties.get(nextSpreadsheetTranslationKey)
                    if (originalPropertyValue != null) {
                        // if property value found for key
                        if (!originalPropertyValue.equals(nextTranslatedValue)) {
                            // if property value differs from value from spreadsheet, replace it and log it; otherwise, do nothing
                            properties.set(nextSpreadsheetTranslationKey, nextTranslatedValue)
                            Log.writeLine("Property $nextSpreadsheetTranslationKey: '$originalPropertyValue' replaced by '$nextTranslatedValue'")
                        }
                    } else {
                        Log.writeLine("exceptions", "Translated property '$nextSpreadsheetTranslationKey' not found in properties file")
                    }
                }
            }
        }
    }

    static logPropertiesWithNoTranslations(translations, Properties properties, languageName) {
        //loop through all properties (iterator), find matching translation from spreadsheet if it exists; otherwise log missing translation
//        properties.allProperties.each { propKey, propValue ->
        properties.rewind()
        while (properties.hasNext()) {
            def nextProperty = properties.next()
            def nextPropertyKey = nextProperty.key
            def nextPropertyValue = nextProperty.value
            if (nextPropertyKey[0] != "*") {
                //pseudo-properties (comments) have '*' in first character (maybe should trim left?)
                Translation matchingSpreadsheetTranslation = translations.getTranslation("Message Key", nextPropertyKey)
                if (matchingSpreadsheetTranslation == null)
                    Log.writeLine("exceptions", "Property '$nextPropertyKey' does not have corresponding 'Message Key' in translation spreadsheet.")
                else if (matchingSpreadsheetTranslation.get(languageName) == null)
                    Log.writeLine("exceptions", "Property '$nextPropertyKey' in property file, but no $languageName translation in translation spreadsheet.")
            }
        }
    }

    static logTranslationKeysWithNoValues(Translations translations, Properties properties, languageName) {
        Log.writeLine("exceptions", "\r\n******* No $languageName translation in spreadsheet:")
        def noTranslationList = translations.getTranslations("$languageName", "")
        if (noTranslationList != null) {
            noTranslationList.each {
                def tKey = it.get("Message Key")
                def x = (tKey != "")
                if ((tKey != "") && (tKey[0] != "#")) {
                    Log.writeLine("exceptions", "Property $tKey has no $languageName translation in spreadsheet.")
                }
            }
        }
    }

    static logMissingTranslations(Translations translations, Properties properties, languageName) {
        Log.writeLine("exceptions", "\r\n******* Missing translation keys or values:")
        logPropertiesWithNoTranslations(translations, properties, languageName)
        logTranslationKeysWithNoValues(translations, properties, languageName)
    }

    static buildArgsMap(args) {
        def key, value
        def argsMap = [:]
        def argCount = args.size()
        args.each {
            def match = it =~ /(.*?)=(.*)/
            if (match[0].size() > 0) {
                key = match[0][1]
                value = match[0][2]
                argsMap.put(key, value)
            }
        }
        argsMap
    }

        static main(args) {
            def argsMap = buildArgsMap(args)
            def startFilePath = argsMap.get("path")
            def languageName = argsMap["language"]
            def component_list = ["DMT", "DE"]
            component_list.each { componentName ->
                def componentFilePath = startFilePath + componentName + "\\\\"
                // open log files
                openLogs(componentName, componentFilePath)
                // open translation file
                TranslationsExcelExportFile translationsExcelExportFile = new TranslationsExcelExportFile(componentName, componentFilePath)
                if (translationsExcelExportFile.exists()) {
                    Translations translationsFromSpreadsheet = new Translations(translationsExcelExportFile)
                    // open property file
                    PropertyFile propertyFile = new PropertyFile(componentName, componentFilePath)
                    if (propertyFile.exists()) {
                        //get property list
                        Properties properties = new Properties(propertyFile)            //load initial property values
                        updatePropertiesFromTranslations(translationsFromSpreadsheet, properties, languageName)
                        //update properties from spreadsheet translation values
                        logMissingTranslations(translationsFromSpreadsheet, properties, languageName)
                        //identify missing translations
                        properties.writePropertiesToTranslatedOutputFile()
                        //write new properties out to output (.translated) file
                    }
                }
            }
        }
    }
