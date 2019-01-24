package libraryquestions

import filemanagement.FileMgr
import filemanagement.TextFile

class LibraryFactory {
    LibraryFactoryManager libraryFactoryManager
    TextFile libraryFactoryFile
    TextFile translatedLibraryFactoryFile
    def libraryTextBlocks = []
    Iterator libraryTextBlockIterator

    LibraryFactory(String libraryFactoryFileName, String translatedLibraryFactoryFileName, LibraryFactoryManager libraryFactoryManager) {
        this.libraryFactoryManager = libraryFactoryManager
        this.libraryFactoryFile = new TextFile(libraryFactoryFileName)
        this.translatedLibraryFactoryFile = new TextFile(translatedLibraryFactoryFileName, FileMgr.createFlag.CREATE)
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

    def writeTextBlockToTranslatedFile(LibraryTextBlock libraryTextBlock) {
        def libraryText = libraryTextBlock.textBlock
        translatedLibraryFactoryFile.writeToFile(libraryText)
    }

    def hasNextLibraryTextBlock() {
        libraryTextBlockIterator.hasNext()
    }

    def nextLibraryTextBlock() {
        libraryTextBlockIterator.next()
    }

}
