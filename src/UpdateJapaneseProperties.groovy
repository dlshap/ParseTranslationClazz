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
            def nextPropertyKey = nextTranslation["Message Key"]
            if (!(nextPropertyKey == "" || nextPropertyKey[0] == "#")) {
                def nextPropertyValue = properties.getPropertyValue(nextPropertyKey).trim()
                if (nextPropertyValue == null) {
                    Log.writeLine("exceptions", "Property $nextPropertyKey not found")
                } else {
                    def nextTranslationValue = nextTranslation["Japanese"].trim()
                    if (!nextPropertyValue.equals(nextTranslationValue)) {
                        properties.setPropertyValue(nextPropertyKey, nextTranslationValue)
                        Log.writeLine("Property $nextPropertyKey: '$nextPropertyValue' replaced by $nextTranslationValue")
                    }
                }
            }
        }
        //   if matching property
        //      update translation value if different
        //  else
        //      log missing property
        //end loop
        //loop through property file
        //  log missing translations
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
