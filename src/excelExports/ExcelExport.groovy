package excelExports

import filemanagement.KeyFile

class ExcelExport {
    def pathName
    def fileName
    def shortName

    ExcelExport(pathName, fileName) {
        this.pathName = pathName
        this.fileName = fileName
        this.shortName =  fileName - ~/\.\w{3}/              // remove file extension
    }

    def file() {
        new KeyFile(this.pathName + "\\\\" + this.fileName)
    }
}
