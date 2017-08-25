import FileManagement.FileChooser
import FileManagement.KeyFileMgr
import Logging.Dates
import Logging.Log
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

    static openTranslationFile(fileName) {
        def transFile = new KeyFileMgr(fileName)
        if (!transFile.exists()) {
            Log.writeLine("exceptions", "Translation file: $fileName doesn't exist.")
        }
        transFile
    }

    static doStuff(fp, translationFile) {
        //open translations file
        Translations translations = new Translations(translationFile)
//        Translations.Translation keymap = translations.getTranslation("Message Key","default.doesnt.match.message")
//        println keymap.getTranslationValue("Japanese")

        //loop through translations
        def tli = translations.getTransKeyListIterator()
        while (tli.hasNext()) {
            println tli.next()
        }
        //   if matching property
        //      update translation if different
        //      save last matching property
        //  else
        //      log missing property
        //      add to file after last matching property
        //      save last matching property
        //end loop
        //loop through property file
        //  log missing translations
        //end loop
    }

    static main(args) {
        //open log files
        def fp = "C:\\\\Users\\\\s0041664\\\\Documents\\\\Projects\\\\DMT-DE\\\\Translations\\\\"
        openLogs(fp)

        //open property file
//        def fileName = new FileChooser().chooseFile(fp + "PropertyExports\\\\")
        def fileName = fp + "PropertyExports\\\\DMT-export.txt"         //testing

        if (fileName != null) {
            def translationFile = openTranslationFile(fileName)
            if (translationFile.exists()) {
                doStuff(fp, translationFile)
            }
        }
    }
}
