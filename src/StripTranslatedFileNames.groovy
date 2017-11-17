import filemanagement.FileChooser
import logging.Dates
import logging.Log

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
        // pick library folder (translated files)
        openLogs(fp)
        def changeLibrary = FileChooser.chooseFile("Select Directory Folder for Name Changes", fp, FileChooser.selectChoices.DIRECTORIES)
        if (changeLibrary != null) {
            stripTranslatedFromFileNames(changeLibrary)
        }
    }
}
