package libraryquestions

import useful.ArgsParser

class LibraryArgs {
    def startFilePath
    def languageName
    def fileNameForTestingSingleFile

    LibraryArgs(args) {
        getValuesFromCommandLineArgs(args)
        getDefaultValuesIfArgsNull()
    }

    def getValuesFromCommandLineArgs(args) {
        def argsMap = new ArgsParser(args)
        startFilePath = argsMap.get("path")
        languageName = argsMap.get("language")
        fileNameForTestingSingleFile = argsMap.get("file")
    }

    def getDefaultValuesIfArgsNull() {
        if (startFilePath == null) startFilePath = "C:\\\\Users\\\\s0041664\\\\Documents\\\\Projects\\\\DMT-DE\\\\Project Work\\\\translations\\\\DMT\\\\"
        if (languageName == null) languageName = "Japanese"
    }


}
