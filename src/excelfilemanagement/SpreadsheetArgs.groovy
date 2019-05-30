package excelfilemanagement

import useful.Args
import useful.Config

class SpreadsheetArgs {
    String basePath
    String spreadsheetPath
    String language
    String fileNameForTestingSingleFile
    Config config

    SpreadsheetArgs(args) {
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

    def getConfigValues() {
        config = new Config(basePath)
    }
}
