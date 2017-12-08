package filemanagement

import logging.Log

class TranslationFile extends KeyFile {

    def TranslationFile(fp) {
        super()
        def transFileName = FileChooser.chooseFile("Select Translation file", fp + "PropertyExports\\\\")
        openTransFile(transFileName)
    }

    def TranslationFile(comp, fp) {
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
