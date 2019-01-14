package libraryquestions

import filemanagement.FileMgr
import filemanagement.TextFile

class LibraryFactory {
    def libraryFactoryFile

    def LibraryFactory(libraryFactoryFileName) {
        libraryFactoryFile = new TextFile(libraryFactoryFileName, FileMgr.createFlag.CREATE)
    }

    def writeToFile(nextFactoryTextBlock) {
        libraryFactoryFile.writeToFile(nextFactoryTextBlock)
    }
}
