package FileManagement

import Logging.Log

class PropertyFile {

    String fp
    LineFileMgr propertyFile

    def PropertyFile(fp) {
        this.fp = fp
        def propFileName = new FileChooser().chooseFile(fp + "PropertyFiles\\\\")
//            def propFileName = fp + "PropertyFiles\\\\messages_ja.properties"
        if (propFileName == null) {
            Log.writeLine("exceptions", "No properties file found.")
        } else {
            this.propertyFile = openPropertyFile(propFileName)
        }
    }

    def openPropertyFile(propFileName) {
        def propFile = new LineFileMgr(propFileName)
        if (!propFile.exists()) {
            Log.writeLine("exceptions", "Property file $propFileName doesn't exist.")
        }
        propFile
    }

    def hasNext() {
        propertyFile.hasNext()
    }

    def next() {
        propertyFile.next()
    }

    def getOutFileName() {
        def propertyOutFileName = fp + "PropertyFilesTranslated\\\\" + propertyFile.getFileName() + ".translated"
        propertyOutFileName
    }

    def openPropertyOutFile() {
        LineFileMgr propertyOutFile
        if (this.propertyFile != null) {
            def propertyOutFileName = this.getOutFileName()
            propertyOutFile = new LineFileMgr(propertyOutFileName, FileMgr.createFlag.CREATE)
        }
        propertyOutFile
    }
}
