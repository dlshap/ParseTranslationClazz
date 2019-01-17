package libraryquestions

import filemanagement.FileMgr
import filemanagement.TextFile

class LibraryFactory {
    LibraryFactoryManager libraryFactoryManager
    TextFile libraryFactoryFile
    TextFile translatedLibraryFactoryFile
    LibraryFactoryParser libraryFactoryParser

    LibraryFactory(String libraryFactoryFileName, String translatedLibraryFactoryFileName, LibraryFactoryManager libraryFactoryManager) {
        this.libraryFactoryManager = libraryFactoryManager
        this.libraryFactoryFile = new TextFile(libraryFactoryFileName)
        this.translatedLibraryFactoryFile = new TextFile(translatedLibraryFactoryFileName, FileMgr.createFlag.CREATE)
        this.libraryFactoryParser = new LibraryFactoryParser(libraryFactoryFile)
    }

    def writeTextBlockToTranslatedFile(String factoryTextBlock) {
        translatedLibraryFactoryFile.writeToFile(factoryTextBlock)
    }

    def hasNextTextBlock() {
        libraryFactoryParser.hasNextTextBlock()
    }

    def nextTextBlock() {
        libraryFactoryParser.nextTextBlock()
    }

}
