import translations.UnicodeUtil

import properties.PropertyFile
import properties.PropertiesExcelExportFile
import logging.Dates
import logging.Log
import translations.Properties
import translations.Translations
import translations.Translation


/**
 * Created by s0041664 on 11/17/2017.
 */
class UpdateRMTJapaneseProperties {

    static openLogs(fp) {
        Log.open(fp + "log-rmt-property-translations.txt")
        Log.writeLine "Running on " + Dates.currentDateAndTime() + ":\r\n"
        Log.open("exceptions", fp + "log-rmt-property-exceptions.txt")
        Log.writeLine"exceptions", "Running on " + Dates.currentDateAndTime() + ":\r\n"
    }

    static updatePropertyFile(Translations translations, Properties properties) {
        // loop through translationFile
        while (translations.hasNext()) {
            def nextTranslation = translations.next()
            def nextTranslationValue = nextTranslation["Japanese"].trim()
            if ((nextTranslationValue != null) && (nextTranslationValue != "")) {
                def nextTranslationKey = (nextTranslation["English"]).trim()
                nextTranslationKey = UnicodeUtil.encodeSpaces(nextTranslationKey)
                if (!(nextTranslationKey == "" || nextTranslationKey[0] == "#")) {
                    def matchingProperty = properties.get(nextTranslationKey)
                    if (matchingProperty == null) {
                        Log.writeLine("exceptions", "Translated property '$nextTranslationKey' not found in properties file")
                    } else {
                        def matchingPropertyMixedEncoding = matchingProperty.trim()
                        def matchingPropertyDecoded = UnicodeUtil.unicodeDecode(matchingPropertyMixedEncoding)
                        matchingProperty = UnicodeUtil.unicodeEncode(matchingPropertyDecoded)
                        nextTranslationValue = UnicodeUtil.unicodeEncode(nextTranslationValue)
                        if (!matchingProperty.equals(nextTranslationValue)) {
                            properties.set(nextTranslationKey, nextTranslationValue)
                            Log.writeLine("Property $nextTranslationKey: '$matchingProperty' replaced by '$nextTranslationValue'")
                        }
                    }
                }
            }
        }
    }

    static logPropertiesWithNoTranslations(translations, properties) {
        properties.allProperties.each { propKey, propValue ->
            if (propKey[0] != "*") {
                Translation matchingTranslation = translations.getTranslation("English", propKey)
                if (matchingTranslation == null)
                    Log.writeLine("exceptions", "Property '$propKey' does not have corresponding 'English' key in translation spreadsheet.")
                else if (matchingTranslation.get("Japanese") == null)
                    Log.writeLine("exceptions", "Property '$propKey' in property file, but no Japanese translation in translation spreadsheet.")
            }
        }
    }

    static logTranslationKeysWithNoValues(Translations translations, Properties properties) {
        Log.writeLine("exceptions", "\r\n******* No Japanese translation in spreadsheet:")
        def noJapaneseList = translations.getTranslationsMapFromKeyFields("Japanese", "")
        if (noJapaneseList != null) {
            noJapaneseList.each {
                def tKey = it.get("English")
                def x = (tKey != "")
                if ((tKey != "") && (tKey[0] != "#")) {
                    Log.writeLine("exceptions", "Property $tKey has no Japanese translation in spreadsheet.")
                }
            }
        }
    }

    static logMissingTranslations(Translations translations, Properties properties) {
        Log.writeLine("exceptions", "\r\n******* Missing translation keys or values:")
        logPropertiesWithNoTranslations(translations, properties)
        logTranslationKeysWithNoValues(translations, properties)
    }

    static getFilePath(args) {
        String fp //filepath
        def lastChar
        if (args.size() == 0)
            fp = "C:\\\\Users\\\\s0041664\\\\Documents\\\\Projects\\\\DMT-DE\\\\Project Work\\\\translations\\\\"
        else {
            fp = args[0]
            if (fp[-1] != "\\") fp += "\\"
        }
        fp
    }

    static main(args) {
        def fp = getFilePath(args)
        openLogs(fp)
        // open translation file
        PropertiesExcelExportFile translationFile = new PropertiesExcelExportFile(fp)
        if (translationFile.exists()) {
            Translations translations = new Translations(translationFile)
            // open property file
            PropertyFile propertyFile = new PropertyFile(fp)
            if (propertyFile.exists()) {
                //get property list
                Properties properties = new Properties(propertyFile)
                updatePropertyFile(translations, properties)
                logMissingTranslations(translations, properties)
                properties.writePropertiesToTranslatedOutputFile()
            }
        }
    }
}
