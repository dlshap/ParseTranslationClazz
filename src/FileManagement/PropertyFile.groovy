package FileManagement

import Logging.Log

class PropertyFile extends LineFile {

    def PropertyFile() {
        super()
    }

    def PropertyFile(fp) {
        super()
        def propFileName = FileChooser.chooseFile("Select Property file", fp + "PropertyFiles\\\\")
//            def propFileName = fp + "PropertyFiles\\\\messages_ja.properties"
        if (propFileName == null) {
            Log.writeLine("exceptions", "No properties file selected.")
        } else {
            openFile(propFileName)
        }
    }

    def getTranslatedFileName() {
        def parentDir = new File(getDirPath()).getParent()
        def propertyOutFileName = parentDir + "\\\\PropertyFilesTranslated\\\\" + getFileName() + ".translated"
        propertyOutFileName
    }

    def openTranslatedFile() {
        LineFile translatedFile
        if (theFile != null) {
            def propertyOutFileName = this.getTranslatedFileName()
            translatedFile = new LineFile(propertyOutFileName, FileMgr.createFlag.CREATE)
        }
        translatedFile
    }
}
