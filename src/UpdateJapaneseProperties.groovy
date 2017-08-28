import FileManagement.FileChooser
import FileManagement.KeyFileMgr
import FileManagement.LineFileMgr
import Logging.Dates
import Logging.Log
import Translations.Properties
import Translations.Translations

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

    static updatePropertyFile(KeyFileMgr translationFile, LineFileMgr propertyFile) {
        Properties properties = new Properties(propertyFile)
        // loop through translationFile
        while (translationFile.hasNext()) {
            def nextTranslation = translationFile.next()
            def nextTranslationKey = nextTranslation["Message Key"]
            if (!(nextTranslationKey == "" || nextTranslationKey[0] == "#")) {
                def nextPropertyValue = properties.getPropertyValue(nextTranslationKey)
                if (nextPropertyValue == null) {
                    Log.writeLine("exceptions", "Property '$nextTranslationKey' not found in translations file")
                } else {
                    def nextTranslationValue = nextTranslation["Japanese"].trim()
                    if (nextTranslationValue != "") {
                        if (!nextPropertyValue.equals(nextTranslationValue)) {
                            properties.setPropertyValue(nextTranslationKey, nextTranslationValue)
                            Log.writeLine("Property $nextTranslationKey: '$nextPropertyValue' replaced by '$nextTranslationValue'")
                        }
                    }
                }
            }
        }
        //loop through translation file
        //  log any translations missing in properties file
        //end loop

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
                updatePropertyFile(translationFile, propertyFile)
            }
        }
    }

}
