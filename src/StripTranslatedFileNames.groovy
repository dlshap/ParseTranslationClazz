import FileManagement.FileChooser
import Logging.Dates
import Logging.Log

class StripTranslatedFileNames {

    static stripTranslatedFromFileNames(changeLibrary) {
        def list = new File(changeLibrary)
        list.eachFileMatch(~/.*.translated/) {
            def newName = it.getPath().replaceAll(~/.translated/, "")
            def result = it.renameTo(new File(newName))
            if (result)
                Log.writeLine("changed name of $it \r\nto: $newName")
            else
                Log.writeLine("exceptions", "could not change name of $it \r\nto: $newName")
        }
    }

    static openLogs(fp) {
        Log.open(fp + "log-rename-translated-files.txt")
        Log.writeLine("Running on: " + Dates.currentDateAndTime())
        Log.open("exceptions", fp + "log-rename-exceptions.txt")
        Log.writeLine("exceptions", "Running on: " + Dates.currentDateAndTime())
    }

    static main(args) {
        def fp = "C:\\\\Users\\\\s0041664\\\\Documents\\\\Projects\\\\DMT-DE\\\\Translations\\\\"
        // pick library folder (translated files)
        openLogs(fp)
        def changeLibrary = FileChooser.chooseFile("Select Directory Folder for Name Changes", fp, FileChooser.selectMode.DIRECTORIES)
        if (changeLibrary != null) {
            stripTranslatedFromFileNames(changeLibrary)
        }
    }
}
