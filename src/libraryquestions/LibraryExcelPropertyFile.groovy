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

    static openLibraryProperty

    private buildLibraryPropertyFileUsingChooser(String prompt, String path) {
        this.chooseLibraryPropertyFile(prompt, path)
    }

    private chooseLibraryPropertyFile(String prompt, String path) {
        super.openExcelFileUsingChooser(prompt, path)
    }
}
