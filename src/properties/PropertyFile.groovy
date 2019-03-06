package properties

import filemanagement.FileChooser
import filemanagement.LineFile
import i18n.Messages
import translations.TranslationProperties

import static filemanagement.BaseFile.CreateFlag.CREATE

class PropertyFile extends LineFile {

    static final PROPERTY_FILE_PROMPT = "prompt.for.message.properties.file.for"

    PropertyFile() {
    }

    static createNewTranslationPropertyFileFromPathAndFile(String filePath, String fileName) {
        PropertyFile propertyFile = new PropertyFile()
        String transFileName = outputFileName(filePath, fileName)
        propertyFile.openFile(transFileName, CREATE)
        propertyFile
    }

    private static outputFileName(String filePath, String fileName) {
        filePath + "\\PropertyFilesTranslated\\" + fileName + ".translated"
    }

    static openPropertyFileForComponentUsingChooser(componentName, componentPath) {
        PropertyFile propertyFile = new PropertyFile()
        def prompt = Messages.getString(PROPERTY_FILE_PROMPT, componentName)
        propertyFile.openFile(FileChooser.chooseFile(prompt, componentPath))
        propertyFile
    }

    static openPropertyFileFromFileName(String fileName) {
        PropertyFile propertyFile = new PropertyFile()
        propertyFile.openFile(fileName)
        propertyFile
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

    def getTranslationProperties() {
        new TranslationProperties(lines)
    }
}
