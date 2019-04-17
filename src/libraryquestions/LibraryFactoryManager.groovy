package libraryquestions

import filemanagement.FileDirectoryMgr

class LibraryFactoryManager {
    String libraryFactoryFilePath
    String translatedLibraryFactoryFilePath
    String translationLanguage
    LibraryQuestionFieldParser libraryQuestionFieldFinder  /* singleton for a given language */

    LibraryFactoryManager(LibraryArgs libraryArgs) {
        translationLanguage = libraryArgs.languageName
        createLibraryQuestionFieldFinder(translationLanguage)
        buildLibraryFactoryFilePaths(libraryArgs)
        createLibraryFactoryOutputDirectory()
    }

    def createLibraryQuestionFieldFinder(translationLanguage) {
        libraryQuestionFieldFinder = new LibraryQuestionFieldParser(translationLanguage)
    }

    def buildLibraryFactoryFilePaths(libraryArgs) {
        libraryFactoryFilePath = libraryArgs.startFilePath + "\\DMT\\LibraryFactories\\"
        translatedLibraryFactoryFilePath = libraryArgs.startFilePath + "\\DMT\\LibraryFactoriesTranslated\\"
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
