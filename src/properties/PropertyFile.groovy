package properties

import filemanagement.FileChooser
import filemanagement.LineFile
import filemanagement.BaseFile
import logging.Log

class PropertyFile extends LineFile {

    def componentName
    def propFileName

    PropertyFile() {
    }

    PropertyFile(componentName, componentPath) {
        this.componentName = componentName
        propFileName = FileChooser.chooseFileAndReturnFilename("Select Property file for $componentName", componentPath + "PropertyFiles\\\\")
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
        def propertyOutFileName = parentDir + "\\PropertyFilesTranslated\\" + fileName + ".translated"
        propertyOutFileName
    }

    def openTranslatedOutputFile() {
        LineFile translatedFile
        if (file != null) {
            def propertyOutFileName = this.getTranslatedOutputFileName()
            translatedFile = new LineFile(propertyOutFileName, BaseFile.createFlag.CREATE)
        }
        translatedFile
    }
}
