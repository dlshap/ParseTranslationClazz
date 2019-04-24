package libraryquestions

import properties.ExcelPropertyFile

class LibraryExcelPropertyFile extends ExcelPropertyFile {

    LibraryExcelPropertyFile() {
    }

    LibraryExcelPropertyFile(String fileName, CreateFlag createFlag) {
        super(fileName, createFlag)
    }

    static openLibraryPropertyFileUsingChooser(String prompt, String path) {
        LibraryExcelPropertyFile libraryPropertyFile = new LibraryExcelPropertyFile()
        libraryPropertyFile.chooseLibraryPropertyFile(prompt, path)
        libraryPropertyFile.fileName == null ? null : libraryPropertyFile
    }

    private chooseLibraryPropertyFile(String prompt, String path) {
        super.openExcelFileUsingChooser(prompt, path)
    }

    static openLibraryPropertyFileUsingFileName(String fileName) {
        LibraryExcelPropertyFile libraryExcelPropertyFile = new LibraryExcelPropertyFile()
        libraryExcelPropertyFile.openExcelFileUsingFileName(fileName)
        libraryExcelPropertyFile.file.exists() ? libraryExcelPropertyFile : null
    }

    static LibraryExcelPropertyFile createNewFileFromFileName(String fileName, CreateFlag createFlag) {
        LibraryExcelPropertyFile libraryExcelPropertyFile = new LibraryExcelPropertyFile(fileName, createFlag)
        libraryExcelPropertyFile
    }
}
