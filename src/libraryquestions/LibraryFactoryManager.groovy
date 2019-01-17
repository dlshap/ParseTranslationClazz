package libraryquestions

import filemanagement.FileDirectoryMgr

class LibraryFactoryManager {
    String libraryFactoryFilePath
    String translatedLibraryFactoryFilePath
    String translationLanguage
    LibraryQuestionFieldFinder libraryQuestionFieldFinder

    LibraryFactoryManager(LibraryArgs libraryArgs) {
        translationLanguage = libraryArgs.languageName
        createLibraryQuestionFieldFinder(translationLanguage)
        buildLibraryFactoryFilePaths(libraryArgs)
        createLibraryFactoryOutputDirectory()
    }

    def createLibraryQuestionFieldFinder(translationLanguage) {
        libraryQuestionFieldFinder = new LibraryQuestionFieldFinder(translationLanguage)
    }

    def buildLibraryFactoryFilePaths(libraryArgs) {
        libraryFactoryFilePath = libraryArgs.startFilePath + "LibraryFactories\\\\"
        translatedLibraryFactoryFilePath = libraryArgs.startFilePath + "LibraryFactoriesTranslated\\\\"
    }

    def createLibraryFactoryOutputDirectory() {
        FileDirectoryMgr.makeDirectory(translatedLibraryFactoryFilePath)
    }

    def getLibraryFactoryForFileName(String shortName) {
        def libraryFactoryFileName = libraryFactoryFilePath + shortName + "ClassFactory.groovy"
        def translatedLibraryFactoryFileName = translatedLibraryFactoryFilePath + shortName + "ClassFactory.groovy.translated"
        def libraryFactory = new LibraryFactory(libraryFactoryFileName, translatedLibraryFactoryFileName, this)
        libraryFactory
    }
}
