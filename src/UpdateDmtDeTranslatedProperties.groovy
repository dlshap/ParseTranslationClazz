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
        Log.writeLine"exceptions", "Running on " + Dates.currentDateAndTime() + ":\r\n"
    }

    static updatePropertiesFromTranslations(Translations translationsFromSpreadsheet, Properties properties, languageName) {
        // loop through property value translations from Excel spreadsheet
        while (translationsFromSpreadsheet.hasNext()) {
            def nextTranslationFromSpreadsheet = translationsFromSpreadsheet.next()
            def nextTranslationValue = nextTranslationFromSpreadsheet[languageName].trim()
            if ((nextTranslationValue != null) && (nextTranslationValue.trim() != "")) {
                def nextTranslationKey = nextTranslationFromSpreadsheet["Message Key"]
                if (!(nextTranslationKey == "" || nextTranslationKey[0] == "#")) {
                    def matchingProperty = properties.get(nextTranslationKey)
                    if (matchingProperty == null) {
                        Log.writeLine("exceptions", "Translated property '$nextTranslationKey' not found in properties file")
                    } else {
                        if (!matchingProperty.equals(nextTranslationValue)) {
                            properties.set(nextTranslationKey, nextTranslationValue)
                            Log.writeLine("Property $nextTranslationKey: '$matchingProperty' replaced by '$nextTranslationValue'")
                        }
                    }
                }
            }
        }
    }

    static logPropertiesWithNoTranslations(translations, properties, languageName) {
        properties.allProperties.each { propKey, propValue ->
            if (propKey[0] != "*") {
                Translation matchingTranslation = translations.getTranslation("Message Key", propKey)
                if (matchingTranslation == null)
                    Log.writeLine("exceptions", "Property '$propKey' does not have corresponding 'Message Key' in translation spreadsheet.")
                else if (matchingTranslation.get(languageName) == null)
                    Log.writeLine("exceptions", "Property '$propKey' in property file, but no $languageName translation in translation spreadsheet.")
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

    static getFilePath(args) {
        String fp //filepath
        def lastChar
        if (args.size() == 0)
            fp = "C:\\\\Users\\\\s0041664\\\\Documents\\\\Projects\\\\DMT-DE\\\\Project Work\\\\translations\\\\"
        else {
            fp = args[0]
            if (fp[-2] != "\\")
                fp += "\\"
        }
        fp
    }

    static main(args) {
        def initFp = getFilePath(args)
        def languageName = "Japanese"
        def component_list = ["DMT", "DE"]
        component_list.each { componentName ->
            def componentFilePath = initFp + componentName + "\\\\"
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
                    updatePropertiesFromTranslations(translationsFromSpreadsheet, properties, languageName)      //overwrite properties from translations
                    logMissingTranslations(translationsFromSpreadsheet, properties, languageName)                //identify missing translations
                    properties.writePropertiesToTranslatedOutputFile()              //write new properties out to output (.translated) file
                }
            }
        }
    }
}
