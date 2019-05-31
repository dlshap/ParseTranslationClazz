package libraryquestions

import excelfilemanagement.SpreadsheetArgs

class LibraryArgs extends SpreadsheetArgs{

    String libraryFactoryPath
    String librarySpreadsheetLogPath
    String libraryFactoryLogPath

    LibraryArgs(args) {
        super(args)
    }

    def getConfigValues() {
        super.getConfigValues()
        spreadsheetPath = basePath + config.get("library.spreadsheet.relative.path")
        libraryFactoryPath = basePath + config.get("library.factory.relative.path")
        librarySpreadsheetLogPath = basePath + config.get("library.spreadsheet.log.relative.path")
        libraryFactoryLogPath = basePath + config.get("library.factory.log.relative.path")
    }
}
