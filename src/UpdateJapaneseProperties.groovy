import FileManagement.FileMgr
import FileManagement.KeyFileMgr
import FileManagement.LineFileMgr
import FileManagement.PropertyFile
import FileManagement.TranslationFile
import Logging.Dates
import Logging.Log
import Translations.Properties
import Translations.Translations
import Translations.Translation


/**
 * Created by s0041664 on 8/25/2017.
 */
class UpdateJapaneseProperties {

    static openLogs(fp) {
        Log.open(fp + "log-property-translations.txt")
        Log.writeLine("Running on: " + Dates.currentDateAndTime())
        Log.open("exceptions", fp + "log-property-exceptions.txt")
        Log.writeLine("exceptions", "Running on: " + Dates.currentDateAndTime())
    }

    static updatePropertyFile(TranslationFile translationFile, Properties properties) {
        // loop through translationFile
        while (translationFile.hasNext()) {
            def nextTranslation = translationFile.next()
            def nextTranslationValue = nextTranslation["Japanese"].trim()
            if (nextTranslationValue != null) {
                def nextTranslationKey = nextTranslation["Message Key"]
                if (!(nextTranslationKey == "" || nextTranslationKey[0] == "#")) {
                    def matchingProperty = properties.getPropertyValue(nextTranslationKey)
                    if (matchingProperty == null) {
                        Log.writeLine("exceptions", "Translated property '$nextTranslationKey' not found in properties file")
                    } else {
                        if (!matchingProperty.equals(nextTranslationValue)) {
                            properties.setPropertyValue(nextTranslationKey, nextTranslationValue)
                            Log.writeLine("Property $nextTranslationKey: '$matchingProperty' replaced by '$nextTranslationValue'")
                        }
                    }
                }
            }
        }
    }

    static logMissingTranslations(KeyFileMgr translationFile, Properties properties) {
        Log.writeLine("exceptions", "\r\n******* Missing translation keys or values:")
        Translations translations = new Translations(translationFile)
        //TODO: Encapsulate propKeyMap
        properties.propKeyMap.each { propKey, propValue ->
            if (propKey[0] != "*") {
                Translation matchingTranslation = translations.getTranslation("Message Key", propKey)
                if (matchingTranslation == null)
                    Log.writeLine("exceptions", "Property '$propKey' does not have corresponding 'Message Key' in translation spreadsheet.")
                else if (matchingTranslation.getTranslationValue("Japanese") == null)
                    Log.writeLine("exceptions", "Property '$propKey' has row, but no Japanese translation in translation spreadsheet.")
            }
        }
    }

    static main(args) {
        //open log files
        def fp = "C:\\\\Users\\\\s0041664\\\\Documents\\\\Projects\\\\DMT-DE\\\\Translations\\\\"

        openLogs(fp)
        // open translation file
        TranslationFile translationFile = new TranslationFile(fp)
        if (translationFile.exists()) {
            // open property file
            PropertyFile propertyFile = new PropertyFile(fp)
            if (propertyFile.exists()) {
                //get property list
                Properties properties = new Properties(propertyFile)
                updatePropertyFile(translationFile, properties)
                logMissingTranslations(translationFile, properties)
                properties.writeToTranslatedFile()
            }
        }
    }
}
