import filemanagement.PropertyFile
import filemanagement.TranslationFile
import logging.Dates
import logging.Log
import translations.Properties
import translations.Translations
import translations.Translation


/**
 * Created by s0041664 on 8/25/2017.
 */
class UpdateDmtDeJapaneseProperties {

    static openLogs(comp, fp) {
        def logFp = fp + "logs\\\\"
        Log.open(logFp + "$comp log-property-translations.txt")
        Log.writeLine "Running on " + Dates.currentDateAndTime() + ":\r\n"
        Log.open("exceptions", logFp + "$comp log-property-exceptions.txt")
        Log.writeLine"exceptions", "Running on " + Dates.currentDateAndTime() + ":\r\n"
    }

    static updatePropertyFile(Translations translations, Properties properties) {
        // loop through translationFile
        while (translations.hasNext()) {
            def nextTranslation = translations.next()
            def nextTranslationValue = nextTranslation["Japanese"].trim()
            if ((nextTranslationValue != null) && (nextTranslationValue.trim() != "")) {
                def nextTranslationKey = nextTranslation["Message Key"]
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

    static logPropertiesWithNoTranslations(translations, properties) {
        properties.allProperties.each { propKey, propValue ->
            if (propKey[0] != "*") {
                Translation matchingTranslation = translations.getTranslation("Message Key", propKey)
                if (matchingTranslation == null)
                    Log.writeLine("exceptions", "Property '$propKey' does not have corresponding 'Message Key' in translation spreadsheet.")
                else if (matchingTranslation.get("Japanese") == null)
                    Log.writeLine("exceptions", "Property '$propKey' in property file, but no Japanese translation in translation spreadsheet.")
            }
        }
    }

    static logTranslationKeysWithNoValues(Translations translations, Properties properties) {
        Log.writeLine("exceptions", "\r\n******* No Japanese translation in spreadsheet:")
        def noJapaneseList = translations.getTranslations("Japanese", "")
        if (noJapaneseList != null) {
            noJapaneseList.each {
                def tKey = it.get("Message Key")
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
        def initFp = getFilePath(args)
        def components = ["DMT", "DE"]
        components.each { comp ->
            def fp = initFp + comp + "\\\\"
            openLogs(comp, fp)
            // open translation file
            TranslationFile translationFile = new TranslationFile(comp, fp)
            if (translationFile.exists()) {
                Translations translations = new Translations(translationFile)
                // open property file
                PropertyFile propertyFile = new PropertyFile(comp, fp)
                if (propertyFile.exists()) {
                    //get property list
                    Properties properties = new Properties(propertyFile)
                    updatePropertyFile(translations, properties)
                    logMissingTranslations(translations, properties)
                    properties.writeToTranslatedFile()
                }
            }
        }
    }
}
