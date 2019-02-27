package properties

import filemanagement.FileChooser
import filemanagement.LineFile
import filemanagement.BaseFile
import logging.Log

import static filemanagement.BaseFile.createFlag.CREATE

class PropertyFile extends LineFile {

    def componentName
    def propFileName

    PropertyFile() {
    }

    static createNewTranslationPropertyFile(String filePath, String fileName) {
        PropertyFile propertyFile = new PropertyFile()
        String transFileName = outputFileName(filePath, fileName)
        propertyFile.openFile(transFileName, CREATE)
        propertyFile
    }

    private static outputFileName(String filePath, String fileName) {
        filePath + "\\PropertyFilesTranslated\\" + fileName + ".translated"
    }

//    ToDo: remove
//    PropertyFile(componentName, componentPath) {
//        this.componentName = componentName
//        propFileName = FileChooser.chooseFileAndReturnFilename("Select Property file for $componentName", componentPath + "PropertyFiles\\\\")
//        openPropFile()
//    }
//

    static createPropertyFileForComponentUsingChooser(componentName, componentPath) {
        PropertyFile propertyFile = new PropertyFile()
        propertyFile.componentName = componentName
        propertyFile.propFileName = FileChooser.chooseFileAndReturnFilename("Select Property file for $componentName", componentPath + "PropertyFiles\\\\")
        propertyFile.openPropFile()
        propertyFile
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
        def propertyOutFileName = outputFileName(parentDir, fileName)
        propertyOutFileName
    }

    def openTranslatedOutputFile() {
        LineFile translatedFile
        if (file != null) {
            def propertyOutFileName = this.getTranslatedOutputFileName()
            translatedFile = new LineFile(propertyOutFileName, CREATE)
        }
        translatedFile
    }
}
