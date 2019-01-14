package libraryquestions

import filemanagement.FileDirectoryMgr

class LibraryFactories {
    def libraryFactoryFilePath
    def translatedLibraryFactoryFilePath

    LibraryFactories(libraryArgs) {
        buildLibraryFactoryFilePaths(libraryArgs)
        createLibraryFactoryOutputDirectory()
    }

    def buildLibraryFactoryFilePaths(libraryArgs) {
        libraryFactoryFilePath = libraryArgs.startFilePath + "LibraryFactories\\\\"
        translatedLibraryFactoryFilePath = libraryArgs.startFilePath + "LibraryFactoriesTranslated\\\\"
    }

    def createLibraryFactoryOutputDirectory() {
        FileDirectoryMgr.makeDirectory(translatedLibraryFactoryFilePath)
    }

    def getLibraryFactoryForFileName(shortName) {
        def libraryFactoryFileName = libraryFactoryFilePath + shortName + "ClassFactory.groovy"
        def translatedLibraryFactoryFileName = translatedLibraryFactoryFilePath + shortName + "ClassFactory.groovy.translated"
        def libraryFactory = new LibraryFactory(libraryFactoryFileName, translatedLibraryFactoryFileName)
        libraryFactory
    }
}
