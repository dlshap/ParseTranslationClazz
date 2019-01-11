package properties

import filemanagement.FileChooser
import filemanagement.KeyFile
import logging.Log

class PropertiesExcelExportFile extends KeyFile {

    def componentName
    def transFileName

    def PropertiesExcelExportFile(componentName, componentFilePath) {
        super()
        this.componentName = componentName
        transFileName = FileChooser.chooseFile("Select Translation.groovy file for $componentName", componentFilePath + "PropertyExports\\\\")
        openTransFile()
    }

    def openTransFile() {
        if (transFileName == null) {
            Log.writeLine("exceptions", "No translation file selected for $componentName.")
        } else {
            openFile(transFileName)
        }

    }
}
