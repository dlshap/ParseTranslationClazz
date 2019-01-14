package libraryquestions

import filemanagement.FileDirectoryMgr

class LibraryFactories {
    def libraryFactoryFilePath
    def translatedLibraryFactoryFilePath
    def translationLanguage

    LibraryFactories(LibraryArgs libraryArgs) {
        translationLanguage = libraryArgs.languageName
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

    def getLibraryFactoryForFileName(String shortName, LibraryFactories libraryFactories) {
        def libraryFactoryFileName = libraryFactoryFilePath + shortName + "ClassFactory.groovy"
        def translatedLibraryFactoryFileName = translatedLibraryFactoryFilePath + shortName + "ClassFactory.groovy.translated"
        def libraryFactory = new LibraryFactory(libraryFactoryFileName, translatedLibraryFactoryFileName, libraryFactories)
        libraryFactory
    }
}
