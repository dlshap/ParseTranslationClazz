package filemanagement

import logging.Log

class TranslationsExcelExportFile extends KeyFile {

    def TranslationsExcelExportFile(exportFilePath) {
        super()
        def transFileName = FileChooser.chooseFile("Select Translation file", exportFilePath + "PropertyExports\\\\")
        openTransFile(transFileName)
    }

    def TranslationsExcelExportFile(componentName, componentFilePath) {
        super()
        def transFileName = FileChooser.chooseFile("Select Translation file for $componentName", componentFilePath + "PropertyExports\\\\")
        openTransFile(transFileName)
    }

    def openTransFile(transFileName) {
        if (transFileName == null) {
            Log.writeLine("exceptions", "No translation file selected.")
        } else {
            openFile(transFileName)
        }

    }
}
