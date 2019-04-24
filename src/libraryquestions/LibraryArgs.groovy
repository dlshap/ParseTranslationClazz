package libraryquestions

import useful.Args
import useful.Config

class LibraryArgs {
    String configPath
    String libraryFilePath
    String spreadsheetPath
    String languageName
    def fileNameForTestingSingleFile

    LibraryArgs(args) {
        getValuesFromCommandLineArgs(args)
        setDefaultValuesIfArgsNull()
        getConfigValues()
    }

    def getValuesFromCommandLineArgs(args) {
        def argsMap = new Args(args)
        configPath = argsMap.get("path")
        languageName = argsMap.get("language")
        fileNameForTestingSingleFile = argsMap.get("file")
    }

    def setDefaultValuesIfArgsNull() {
        if (configPath == null) configPath = "C:\\\\Users\\\\s0041664\\\\Documents\\\\Projects\\\\DMT-DE\\\\Project Work\\\\translations\\\\"
        if (languageName == null) languageName = "Japanese"
    }

    private getConfigValues() {
        Config config = new Config(configPath)
        spreadsheetPath = configPath + config.get("library.question.spreadsheet.relative.path")
        libraryFilePath = configPath + config.get("library.question.files.relative.path")
    }
}
