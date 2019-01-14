package libraryquestions

import filemanagement.FileMgr
import filemanagement.TextFile

class LibraryFactory {
    def libraryFactoryFile
    def translatedLibraryFactoryFile

    LibraryFactory(libraryFactoryFileName, translatedLibraryFactoryFileName) {
        libraryFactoryFile = new TextFile(libraryFactoryFileName)
        translatedLibraryFactoryFile = new TextFile(translatedLibraryFactoryFileName, FileMgr.createFlag.CREATE)
    }

    def writeToFile(nextFactoryTextBlock) {
        libraryFactoryFile.writeToFile(nextFactoryTextBlock)
    }

}
