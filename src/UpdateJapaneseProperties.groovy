import FileManagement.FileChooser
import FileManagement.FileMgr
import FileManagement.KeyFileMgr
import FileManagement.LineFileMgr
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

    static openTranslationFile(transFileName) {
        def transFile = new KeyFileMgr(transFileName)
        if (!transFile.exists()) {
            Log.writeLine("exceptions", "Translation file: $transFileName doesn't exist.")
        }
        transFile
    }

    static openPropertyFile(propFileName) {
        def propFile = new LineFileMgr(propFileName)
        if (!propFile.exists()) {
            Log.writeLine("exceptions", "Property file $propFileName doesn't exist.")
        }
        propFile
    }

    static updatePropertyFile(KeyFileMgr translationFile, Properties properties) {
        // loop through translationFile
        while (translationFile.hasNext()) {
            Translation nextTranslation = translationFile.next()
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
//        def transFileName = new FileChooser().chooseFile(fp + "PropertyExports\\\\")
        def transFileName = fp + "PropertyExports\\\\DMT-export.txt"         //testing
        KeyFileMgr translationFile
        if (transFileName == null) {
            Log.writeLine("exceptions", "No translation file found.")
        } else {
            translationFile = openTranslationFile(transFileName)
        }

        LineFileMgr propertyFile
        if (translationFile != null) {
            // open property file
//        def propFileName = new FileChooser().chooseFile(fp + "PropertyFiles\\\\")
            def propFileName = fp + "PropertyFiles\\\\messages_ja.properties"
            if (propFileName == null) {
                Log.writeLine("exceptions", "No properties file found.")
            } else {
                propertyFile = openPropertyFile(propFileName)
            }
            if (propertyFile != null) {
                //open property output file
                def propertyOutFileName = fp + "PropertyFilesTranslated\\\\" + propertyFile.getFileName() + ".translated"
                LineFileMgr propertyOutFile = new LineFileMgr(propertyOutFileName, FileMgr.createFlag.CREATE)
                Properties properties = new Properties(propertyFile)
                updatePropertyFile(translationFile, properties)
                logMissingTranslations(translationFile, properties)
                properties.writeToFile(propertyOutFile)
            }
        }
    }

}
