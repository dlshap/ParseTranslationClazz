package libraryquestions

import filemanagement.FileDirectoryMgr

class LibraryFactoryManager {

    ArrayList<String> classNames = []
    String libraryFactoryFilePath
    String translatedLibraryFactoryFilePath
    String translationLanguage
    LibraryQuestionFieldParser libraryQuestionFieldFinder  /* singleton for a given language */

    LibraryFactoryManager(LibraryArgs libraryArgs, LibraryExcelPropertyFile libraryExcelPropertyFile) {
        translationLanguage = libraryArgs.languageName
        loadClassNamesFromSheets(libraryArgs, libraryExcelPropertyFile)
        createLibraryQuestionFieldFinder(translationLanguage)
        buildLibraryFactoryFilePaths(libraryArgs)
        createLibraryFactoryOutputDirectory()
    }


    private loadClassNamesFromSheets(LibraryArgs libraryArgs, LibraryExcelPropertyFile libraryExcelPropertyFile) {
        def fileNameForTestingSingleFile = libraryArgs.fileNameForTestingSingleFile
        classNames = libraryExcelPropertyFile.workbook.sheetIterator().collect() { it.sheetName }
        classNames.removeAll() { it.contains("Table of Contents") }
        if (fileNameForTestingSingleFile != null)
            classNames.removeAll() { (!(it.equals(fileNameForTestingSingleFile))) }
    }

    def getClassNameList() {
        (classNames.collect().toString())[1..-2]
    }

    def getClassNameCount() {
        classNames.size()
    }

    def createLibraryQuestionFieldFinder(translationLanguage) {
        libraryQuestionFieldFinder = new LibraryQuestionFieldParser(translationLanguage)
    }

    def buildLibraryFactoryFilePaths(libraryArgs) {
        libraryFactoryFilePath = libraryArgs.libraryFilePath + "LibraryFactories\\"
        translatedLibraryFactoryFilePath = libraryArgs.libraryFilePath + "LibraryFactoriesTranslated\\"
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
