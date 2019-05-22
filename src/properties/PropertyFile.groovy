package properties

import filemanagement.LineFile
import translations.TranslationProperties

import static filemanagement.BaseFile.CreateFlag.CREATE

class PropertyFile extends LineFile {

    PropertyFile() {
    }

    static createNewTranslationPropertyFileFromFileName(String fileName) {
        PropertyFile propertyFile = new PropertyFile()
        propertyFile.openFile(fileName, CREATE)
        propertyFile
    }

     static openPropertyFileFromFileName(String fileName) {
        PropertyFile propertyFile = new PropertyFile()
        propertyFile.openFile(fileName)
        propertyFile
    }

    def getTranslationProperties() {
        new TranslationProperties(lines)
    }
}
