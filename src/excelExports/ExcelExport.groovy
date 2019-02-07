package excelExports

import filemanagement.KeyFile

class ExcelExport {
    def pathName
    def fileName
    def shortName
    KeyFile excelExportFile

    ExcelExport(pathName, fileName) {
        this.pathName = pathName
        this.fileName = fileName
        this.shortName =  fileName - ~/\.\w{3}/              // remove file extension
        this.excelExportFile = new KeyFile(this.pathName + "\\\\" + this.fileName)
    }
}
