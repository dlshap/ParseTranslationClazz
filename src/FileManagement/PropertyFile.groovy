package FileManagement

import Logging.Log

class PropertyFile extends LineFileMgr {

    def PropertyFile() {
        super()
    }

    def PropertyFile(fp) {
        super()
        def propFileName = new FileChooser().chooseFile("Select Property file", fp + "PropertyFiles\\\\")
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
        LineFileMgr translatedFile
        if (theFile != null) {
            def propertyOutFileName = this.getTranslatedFileName()
            translatedFile = new LineFileMgr(propertyOutFileName, FileMgr.createFlag.CREATE)
        }
        translatedFile
    }
}
