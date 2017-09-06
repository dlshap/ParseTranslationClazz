package FileManagement

import Logging.Log

class TranslationFile extends KeyFileMgr {

    def TranslationFile(fp) {
        def transFileName = new FileChooser().chooseFile(fp + "PropertyExports\\\\")
//        def transFileName = fp + "PropertyExports\\\\DMT-export.txt"         //testing
        if (transFileName == null) {
            Log.writeLine("exceptions", "No translation file found.")
        } else {
            openFile(transFileName)
        }
    }
}
