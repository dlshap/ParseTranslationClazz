package libraryquestions

import filemanagement.FileMgr
import filemanagement.TextFile

class LibraryFactory {
    TextFile libraryFactoryFile
    TextFile translatedLibraryFactoryFile
    LibraryFactoryParser libraryFactoryParser

    LibraryFactory(libraryFactoryFileName, translatedLibraryFactoryFileName, LibraryFactoryManager libraryFactories) {
        libraryFactoryFile = new TextFile(libraryFactoryFileName)
        translatedLibraryFactoryFile = new TextFile(translatedLibraryFactoryFileName, FileMgr.createFlag.CREATE)
        libraryFactoryParser = new LibraryFactoryParser(libraryFactoryFile)
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
