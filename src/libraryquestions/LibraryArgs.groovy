package libraryquestions

import useful.Args

class LibraryArgs {
    def startFilePath
    def languageName
    def fileNameForTestingSingleFile

    def args = [:]

    LibraryArgs(args) {
        getValuesFromCommandLineArgs(args)
        getDefaultValuesIfArgsNull()
        appendSlashToStartFilePath()
    }

    def getValuesFromCommandLineArgs(args) {
        def argsMap = new Args(args)
        startFilePath = argsMap.get("path")
        languageName = argsMap.get("language")
        fileNameForTestingSingleFile = argsMap.get("file")
    }

    def getDefaultValuesIfArgsNull() {
        if (startFilePath == null) startFilePath = "C:\\\\Users\\\\s0041664\\\\Documents\\\\Projects\\\\DMT-DE\\\\Project Work\\\\translations\\\\"
        if (languageName == null) languageName = "Japanese"
    }

    def appendSlashToStartFilePath() {
        if (startFilePath[-1] != "\\")
            startFilePath += "\\"
    }
}
