package FileManagement

import Logging.Log

class TranslationFile extends KeyFile {

    def TranslationFile(fp) {
        super()
        def transFileName = FileChooser.chooseFile("Select Translation file", fp + "PropertyExports\\\\")
        if (transFileName == null) {
            Log.writeLine("exceptions", "No translation file selected.")
        } else {
            openFile(transFileName)
        }
    }
}
