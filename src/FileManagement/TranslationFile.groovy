package FileManagement

import Logging.Log

class TranslationFile extends KeyFileMgr {

    def TranslationFile(fp) {
        super()
        def transFileName = new FileChooser().chooseFile("Select Translation file", fp + "PropertyExports\\\\")
//        def transFileName = fp + "PropertyExports\\\\DMT-export.txt"         //testing
        if (transFileName == null) {
            Log.writeLine("exceptions", "No translation file selected.")
        } else {
            openFile(transFileName)
        }
    }
}
