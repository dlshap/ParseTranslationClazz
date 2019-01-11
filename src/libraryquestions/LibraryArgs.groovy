package libraryquestions

import useful.ArgsParser

class LibraryArgs {
    def startFilePath
    def languageName
    def fileNameForTestingSingleFile

    LibraryArgs(args) {
        getArgValues(args)
    }

    def getArgValues(args) {
        def argsMap = new ArgsParser(args)
        startFilePath = argsMap.get("path")
        languageName = argsMap.get("language")
        fileNameForTestingSingleFile = argsMap.get("file")
    }

 }
