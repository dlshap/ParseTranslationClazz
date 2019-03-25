package libraryquestions

import useful.Args
import useful.Config

class LibraryArgs {
    String startFilePath
    String spreadsheetPath
    String languageName
    def fileNameForTestingSingleFile

    LibraryArgs(args) {
        getValuesFromCommandLineArgs(args)
        setDefaultValuesIfArgsNull()
        appendSlashToStartFilePath()
        getConfigValues()
    }

    def getValuesFromCommandLineArgs(args) {
        def argsMap = new Args(args)
        startFilePath = argsMap.get("path")
        languageName = argsMap.get("language")
        fileNameForTestingSingleFile = argsMap.get("file")
    }

    def setDefaultValuesIfArgsNull() {
        if (startFilePath == null) startFilePath = "C:\\\\Users\\\\s0041664\\\\Documents\\\\Projects\\\\DMT-DE\\\\Project Work\\\\translations\\\\"
        if (languageName == null) languageName = "Japanese"
    }

    def appendSlashToStartFilePath() {
        if (startFilePath[-1] != "\\")
            startFilePath += "\\"
    }

    private getConfigValues() {
        Config config = new Config(startFilePath)
        spreadsheetPath = config.get(startFilePath + "library.question.spreadsheet.relative.path")
    }
}
