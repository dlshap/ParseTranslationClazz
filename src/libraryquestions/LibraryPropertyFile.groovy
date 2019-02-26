package libraryquestions

import properties.ExcelPropertyFile

class LibraryPropertyFile extends ExcelPropertyFile {

    ArrayList<String> classNames = []

    LibraryPropertyFile() {
    }

    static openLibraryPropertyFileUsingChooser(String prompt, LibraryArgs libraryArgs) {
        LibraryPropertyFile libraryPropertyFile = new LibraryPropertyFile()
        libraryPropertyFile.buildLibraryPropertyFileUsingChooser(prompt, libraryArgs)
        libraryPropertyFile.fileName == null ? null : libraryPropertyFile
    }

    def buildLibraryPropertyFileUsingChooser(String prompt, LibraryArgs libraryArgs) {
        this.chooseLibraryPropertyFile(prompt, libraryArgs.startFilePath)
        if (this.fileName != null)
            loadClassNamesFromSheets(libraryArgs.fileNameForTestingSingleFile)
    }

    def chooseLibraryPropertyFile(prompt, filePath) {
        def startDir = new File(filePath)
        def spreadsheetDirectory = startDir.getParent() + "\\Spreadsheets\\DMTQuestionLibrarySpreadsheets\\"
        chooseFile(prompt, spreadsheetDirectory)
    }

    def loadClassNamesFromSheets(String fileNameForTestingSingleFile) {
        classNames = workbook.sheetIterator().collect() { it.sheetName }
        classNames.removeAll() { it.contains("Table of Contents") }
        if (fileNameForTestingSingleFile != null)
            classNames.removeAll() { (!(it.equals(fileNameForTestingSingleFile))) }
    }

    def getClassNameList() {
        (classNames.collect().toString())[1..-2]
    }

    def getClassNameCount() {
        classNames.size()
    }

    def getSheet(String sheetName) {
        workbook.getSheet(sheetName)
    }
}
