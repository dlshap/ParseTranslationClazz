package libraryquestions

import useful.Args
import useful.Config

class LibraryArgs {
    String basePath
    String libraryFilePath
    String spreadsheetPath
    String language
    def fileNameForTestingSingleFile

    LibraryArgs(args) {
        getValuesFromCommandLineArgs(args)
        setDefaultValuesIfArgsNull()
        getConfigValues()
    }

    def getValuesFromCommandLineArgs(args) {
        def argsMap = new Args(args)
        basePath = argsMap.get("path")
        language = argsMap.get("language")
        fileNameForTestingSingleFile = argsMap.get("file")
    }

    def setDefaultValuesIfArgsNull() {
        if (basePath == null) basePath = "C:\\\\Users\\\\s0041664\\\\Documents\\\\Projects\\\\DMT-DE\\\\Project Work\\\\translations\\\\"
        if (language == null) language = "all"
    }

    private getConfigValues() {
        Config config = new Config(basePath)
        spreadsheetPath = basePath + config.get("library.question.spreadsheet.relative.path")
        libraryFilePath = basePath + config.get("library.question.files.relative.path")
    }
}
