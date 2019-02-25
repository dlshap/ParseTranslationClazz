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
        addFilesToExcelExports(libraryArgs)
        excelExportsIterator = excelExports.iterator()
    }

    def addFilesToExcelExports(libraryArgs) {
        if (libraryArgs.fileNameForTestingSingleFile != null) {
            this.addFile(libraryArgs.fileNameForTestingSingleFile + ".txt")
        } else {
            def excelExportDirectoryFileList = new FileDirectoryMgr(excelExportFilePath).getFileList()
            excelExportDirectoryFileList.each {this.addFile(it)}
        }
    }

    def addFile(fileName) {
        excelExports.add(new ExcelExport(excelExportFilePath, fileName))
    }

    def fileCount() {
        excelExports.size()
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
}
