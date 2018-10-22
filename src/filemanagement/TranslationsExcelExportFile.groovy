package filemanagement

import logging.Log

class TranslationsExcelExportFile extends KeyFile {

    def TranslationsExcelExportFile(fp) {
        super()
        def transFileName = FileChooser.chooseFile("Select Translation file", fp + "PropertyExports\\\\")
        openTransFile(transFileName)
    }

    def TranslationsExcelExportFile(comp, fp) {
        super()
        def transFileName = FileChooser.chooseFile("Select Translation file for $comp", fp + "PropertyExports\\\\")
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
