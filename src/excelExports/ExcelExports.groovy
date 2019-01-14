package excelExports

import filemanagement.FileDirectoryMgr

class ExcelExports {
    def excelExportFilePath
    def excelExports = []
    def excelExportsIterator

    ExcelExports(libraryArgs) {
        setExcelExportLibraryPath(libraryArgs)
        buildExcelExports(libraryArgs)
    }

    def setExcelExportLibraryPath(libraryArgs) {
        excelExportFilePath = libraryArgs.startFilePath + "LibraryExports\\\\"
    }

    def buildExcelExports(libraryArgs) {
        buildExcelExportFileList(libraryArgs)
        excelExportsIterator = excelExports.iterator()
    }

    def add(fileName) {
        excelExports.add(new ExcelExport(excelExportFilePath, fileName))
    }

    def fileCount() {
        excelExports.size()
    }

    def buildExcelExportFileList(libraryArgs) {
        if (libraryArgs.fileNameForTestingSingleFile != null) {
            this.add(libraryArgs.fileNameForTestingSingleFile)
        } else {
            def excelExportDirectoryFileList = new FileDirectoryMgr(excelExportFilePath).getFileList()
            excelExportDirectoryFileList.each {this.add(it)}
        }
    }

    def getFileNameList() {
        def fileList = excelExports.collect {return it.fileName}
        fileList.toString()[1..-2]
    }

    def hasNext() {
        excelExportsIterator.hasNext()
    }

    def next() {
        excelExportsIterator.next()
    }

    def filePath() {
        excelExportFilePath
    }


}
