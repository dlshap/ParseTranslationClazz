package libraryquestions

import filemanagement.BaseFile
import filemanagement.TextFile

class LibraryFactory {
    LibraryFactoryManager libraryFactoryManager
    TextFile libraryFactoryFile
    TextFile translatedLibraryFactoryFile
    ArrayList<LibraryTextBlock> libraryTextBlocks = []
    Iterator libraryTextBlockIterator

    LibraryFactory(String libraryFactoryFileName, String translatedLibraryFactoryFileName, LibraryFactoryManager libraryFactoryManager) {
        this.libraryFactoryManager = libraryFactoryManager
        this.libraryFactoryFile = new TextFile(libraryFactoryFileName)
        this.translatedLibraryFactoryFile = new TextFile(translatedLibraryFactoryFileName, BaseFile.CreateFlag.CREATE)
        buildLibraryTextBlocks(libraryFactoryFile)
    }

    def buildLibraryTextBlocks(TextFile libraryFactoryFile) {
        def libraryQuestionFieldFinder = libraryFactoryManager.libraryQuestionFieldFinder
        def libraryFactoryParser = new LibraryFactoryParser(libraryFactoryFile)
        while (libraryFactoryParser.hasNextTextBlock()) {
            def nextTextBlock = new LibraryTextBlock(libraryFactoryParser.nextTextBlock(), libraryQuestionFieldFinder)
            libraryTextBlocks << nextTextBlock
        }
        libraryTextBlockIterator = libraryTextBlocks.iterator()
    }

    def writeTextBlockToTranslatedFile(String libraryText) {
        translatedLibraryFactoryFile.writeToFile(libraryText)
    }

    def hasNextLibraryTextBlock() {
        libraryTextBlockIterator.hasNext()
    }

    LibraryTextBlock nextLibraryTextBlock() {
        libraryTextBlockIterator.next()
    }

}
