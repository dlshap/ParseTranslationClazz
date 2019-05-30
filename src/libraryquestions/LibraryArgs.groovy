package libraryquestions

import excelfilemanagement.SpreadsheetArgs

class LibraryArgs extends SpreadsheetArgs{

    String libraryFilePath

    LibraryArgs(args) {
        super(args)
    }

    def getConfigValues() {
        super.getConfigValues()
        libraryFilePath = basePath + config.get("library.question.files.relative.path")
        spreadsheetPath = basePath + config.get("library.question.spreadsheet.relative.path")
    }
}
