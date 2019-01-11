package filemanagement

import logging.Log

class TranslationsExcelExportFile extends KeyFile {

    def componentName
    def transFileName

//    def TranslationsExcelExportFile(exportFilePath) {
//        super()
//        def transFileName = FileChooser.chooseFile("Select Translation.groovy file", exportFilePath + "PropertyExports\\\\")
//        openTransFile(transFileName)
//    }

    def TranslationsExcelExportFile(componentName, componentFilePath) {
        super()
        this.componentName = componentName
        transFileName = FileChooser.chooseFile("Select Translation.groovy file for $componentName", componentFilePath + "PropertyExports\\\\")
        openTransFile()
    }

    def openTransFile() {
        if (transFileName == null) {
            Log.writeLine("exceptions", "No translation file selected for $componentName.")
        } else {
            openFile(transFileName)
        }

    }
}
