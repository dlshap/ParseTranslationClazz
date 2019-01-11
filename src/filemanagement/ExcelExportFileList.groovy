package filemanagement

class ExcelExportFileList {

    def excelExportFileList = []
    def smallExcelExportFileList = []

    ExcelExportFileList() {
    }

    ExcelExportFileList(excelFilePath) {
        excelExportFileList = new FileDirectoryMgr(excelFilePath).getFileList()
        removeFileExtensions()
    }

    def removeFileExtensions() {
        smallExcelExportFileList = excelExportFileList.collect { it - ~/\.\w{3}/ }             // remove file extension
    }

    def add(fileName) {
        excelExportFileList.add(fileName)
        removeFileExtensions()
    }

}
