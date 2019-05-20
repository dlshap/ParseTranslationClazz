package properties

import filemanagement.LineFile
import translations.TranslationProperties

import static filemanagement.BaseFile.CreateFlag.CREATE

class PropertyFile extends LineFile {

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

    static openPropertyFileFromFileName(String fileName) {
        PropertyFile propertyFile = new PropertyFile()
        propertyFile.openFile(fileName)
        propertyFile
    }

    def getTranslationProperties() {
        new TranslationProperties(lines)
    }
}
