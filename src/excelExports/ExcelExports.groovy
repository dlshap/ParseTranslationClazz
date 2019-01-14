package excelExports

import filemanagement.ExcelExportFileList

class ExcelExports {
    def excelExportFileList = []

    ExcelExports(libraryArgs) {
        loadFileListFromDirectoryInArgs(libraryArgs)
    }

    def loadFileListFromDirectoryInArgs(libraryArgs) {
        if (libraryArgs.fileNameForTestingSingleFile != null) {
            excelExportFileList = new ExcelExportFileList()
            excelExportFileList.add(libraryArgs.fileNameForTestingSingleFile)
        } else {
            excelExportFileList = new ExcelExportFileList(libraryArgs.startFilePath + "LibraryExports\\\\")
        }
    }
}
