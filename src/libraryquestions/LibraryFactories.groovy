package libraryquestions

import filemanagement.FileDirectoryMgr

class LibraryFactories {
    def libraryFactoryFilePath
    def libraryFactoryOutputFilePath

    LibraryFactories(libraryArgs) {
        buildLibraryFactoryFilePaths(libraryArgs)
        createLibraryFactoryOutputDirectory()
    }

    def buildLibraryFactoryFilePaths(libraryArgs) {
        libraryFactoryFilePath = libraryArgs.startFilePath + "LibraryFactories\\\\"
        libraryFactoryOutputFilePath = libraryArgs.startFilePath + "LibraryFactoriesTranslated\\\\"
    }

    def createLibraryFactoryOutputDirectory() {
        FileDirectoryMgr.makeDirectory(libraryFactoryOutputFilePath)
    }

    def getLibraryFactoryForFileName(shortName) {
        def libraryFactory = new LibraryFactory(libraryFactoryFilePath + shortName + "ClassFactory.groovy")
        libraryFactory
    }
}
