package properties

import filemanagement.FileChooser
import filemanagement.FileMgr
import filemanagement.LineFile
import logging.Log

class PropertyFile extends LineFile {

    def componentName
    def propFileName

    def PropertyFile() {
        super()
    }

    def PropertyFile(componentName, componentPath) {
        super()
        this.componentName = componentName
        propFileName = FileChooser.chooseFile("Select Property file for $componentName", componentPath + "PropertyFiles\\\\")
        openPropFile()
    }

    def openPropFile() {
        if (propFileName == null) {
            Log.writeLine("exceptions", "No properties file selected for $componentName.")
        } else {
            openFile(propFileName)
        }
    }

    def getTranslatedOutputFileName() {
        def parentDir = new File(getDirPath()).getParent()
        def propertyOutFileName = parentDir + "\\\\PropertyFilesTranslated\\\\" + getFileName() + ".translated"
        propertyOutFileName
    }

    def openTranslatedOutputFile() {
        LineFile translatedFile
        if (theFile != null) {
            def propertyOutFileName = this.getTranslatedOutputFileName()
            translatedFile = new LineFile(propertyOutFileName, FileMgr.createFlag.CREATE)
        }
        translatedFile
    }
}
