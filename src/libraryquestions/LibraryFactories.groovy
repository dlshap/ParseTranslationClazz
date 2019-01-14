package libraryquestions

import filemanagement.FileDirectoryMgr

class LibraryFactories {

    LibraryFactories(libraryArgs) {
        buildOutputDirectoryForUpdatedTranslations(libraryArgs)
    }

    def buildOutputDirectoryForUpdatedTranslations(libraryArgs) {
        FileDirectoryMgr.makeDirectory(libraryArgs.startFilePath + "LibraryFactoriesTranslated\\\\")
    }


}
