package libraryquestions

import properties.ExcelPropertyFile

class LibraryExcelPropertyFile extends ExcelPropertyFile {

    LibraryExcelPropertyFile() {
    }

    static openLibraryPropertyFileUsingChooser(String prompt, String path) {
        LibraryExcelPropertyFile libraryPropertyFile = new LibraryExcelPropertyFile()
        libraryPropertyFile.chooseLibraryPropertyFile(prompt, path)
        libraryPropertyFile.fileName == null ? null : libraryPropertyFile
    }

    private chooseLibraryPropertyFile(String prompt, String path) {
        super.openExcelFileUsingChooser(prompt, path)
    }
}
