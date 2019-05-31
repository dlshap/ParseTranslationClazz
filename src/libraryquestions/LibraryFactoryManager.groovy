package libraryquestions

import filemanagement.FileDirectoryMgr

class LibraryFactoryManager {

    ArrayList<String> classNames = []
    LibraryArgs libraryArgs
    String libraryFactoryNewPath
    LibraryQuestionFieldParser libraryQuestionFieldFinder  /* singleton for a given language */

    LibraryFactoryManager(LibraryArgs libraryArgs, LibraryExcelPropertyFile libraryExcelPropertyFile) {
        this.libraryArgs = libraryArgs
        loadClassNamesFromSheets(libraryExcelPropertyFile)
        createLibraryQuestionFieldFinder(libraryArgs.language)
        buildLibraryFactoryFilePaths()
        createLibraryFactoryOutputDirectory()
    }


    private def loadClassNamesFromSheets(LibraryExcelPropertyFile libraryExcelPropertyFile) {
        def fileNameForTestingSingleFile = libraryArgs.fileNameForTestingSingleFile
        classNames = libraryExcelPropertyFile.workbook.sheetIterator().collect() { it.sheetName }
        classNames.removeAll() { it.contains("Table of Contents") }
        if (fileNameForTestingSingleFile != null)
            classNames.removeAll() { (!(it == fileNameForTestingSingleFile)) }
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

    def buildLibraryFactoryFilePaths() {
        libraryFactoryNewPath = libraryArgs.libraryFactoryPath + "new\\"
    }

    def createLibraryFactoryOutputDirectory() {
        FileDirectoryMgr.makeDirectory(libraryFactoryNewPath)
    }

    def getLibraryFactoryForFileName(String shortName) {
        def libraryFactoryName = libraryArgs.libraryFactoryPath + shortName + "ClassFactory.groovy"
        def libraryFactoryNewName = libraryFactoryNewPath + shortName + "ClassFactory_new.groovy"
        def libraryFactory = new LibraryFactory(libraryFactoryName, libraryFactoryNewName, this)
        libraryFactory
    }
}
