package utilities

import filemanagement.FileChooser
import logging.Dates
import logging.Log

class StripTranslatedFileNames {

    static boolean test = false

    static main(args) {
        def pathName = getFilePath(args)
        // pick library folder (translated files)
        def changeLibrary = FileChooser.chooseDirectoryAndReturnDirName("Select Directory Folder for Name Changes", pathName)
        if (changeLibrary != null) {
            def logLibrary = ((changeLibrary =~ /(.*\\)(new).*/)[0][1]) + "logs\\"
            openLogs(logLibrary)
            stripTranslatedFromFileNames(changeLibrary)
        }
    }

    static getFilePath(args) {
        String fp //filepath
        if (args.size() == 0)
            fp = "C:\\Users\\s0041664\\Documents\\Projects\\DMT-DE\\Project Work\\Translations\\Spreadsheets\\DMTQuestionLibrarySpreadsheets\\new"
        else {
            fp = args[0]
            if (fp[-1] != "\\") fp += "\\"
        }
        fp
    }

    static openLogs(fp) {
        Log.open(fp + "log-rename-translated-files.txt")
        Log.writeLine("Running on: " + Dates.currentDateAndTime())
        Log.open("exceptions", fp + "log-rename-exceptions.txt")
        Log.writeLine("exceptions", "Running on: " + Dates.currentDateAndTime())
    }

    static stripTranslatedFromFileNames(changeLibrary) {
        def list = new File(changeLibrary)
        list.eachFileMatch(~/.*_new.*/) {
            def newName = it.getPath().replaceAll(~/_new/, "")
            if (test)
                println newName
            else {
                def result = it.renameTo(new File(newName))
                if (result)
                    Log.writeLine("changed name of $it \r\nto: $newName")
                else
                    Log.writeLine("exceptions", "could not change name of $it \r\nto: $newName")
            }
        }
    }
}
