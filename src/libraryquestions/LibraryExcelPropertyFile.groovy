package libraryquestions

import properties.ExcelPropertyFile

class LibraryExcelPropertyFile extends ExcelPropertyFile {

    LibraryExcelPropertyFile() {
    }

    static openLibraryPropertyFileUsingChooser(String prompt, String path) {
        LibraryExcelPropertyFile libraryPropertyFile = new LibraryExcelPropertyFile()
        libraryPropertyFile.buildLibraryPropertyFileUsingChooser(prompt, path)
        libraryPropertyFile.fileName == null ? null : libraryPropertyFile
    }

    private buildLibraryPropertyFileUsingChooser(String prompt, String path) {
        this.chooseLibraryPropertyFile(prompt, path)
    }

    private chooseLibraryPropertyFile(prompt, filePath) {
        def startDir = new File(filePath)
        def spreadsheetDirectory = startDir.getParent() + "\\Spreadsheets\\DMTQuestionLibrarySpreadsheets\\"
        openExcelFileUsingChooser(prompt, spreadsheetDirectory)
    }

}
