package libraryquestions

import useful.ArgsParser

class LibraryArgs {
    def startFilePath
    def languageName
    def fileNameForTestingSingleFile

    LibraryArgs(args) {
        getValuesFromCommandLineArgs(args)
    }

    def getValuesFromCommandLineArgs(args) {
        def argsMap = new ArgsParser(args)
        startFilePath = argsMap.get("path")
        languageName = argsMap.get("language")
        fileNameForTestingSingleFile = argsMap.get("file")
    }
}
