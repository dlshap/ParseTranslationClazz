import filemanagement.FileChooser
import filemanagement.TextFile
import useful.Args

/**
 * Created by s0041664 on 8/24/2017.
 */
class AddAnotherLanguageToClassFactory {

    static final linebreak = "\r\n             "
    Args argsParser
    TextFile classFactoryFile
    String newText

    AddAnotherLanguageToClassFactory() {
    }

    static main(args) {
        new AddAnotherLanguageToClassFactory().start(args)
    }

    def start(args) {
        argsParser = new Args(args)
        selectFactoryFile()
        addTranslationFieldsToFactoryFile()
        writeTextToFactoryFile()
    }

    def selectFactoryFile() {
        def factoryPath = getFactoryPath()
        openFactoryFile(factoryPath)
    }

    def getFactoryPath() {
        def path = argsParser.argsMap.get("path")
        if (path == null)
            path = "C:\\\\Users\\\\s0041664\\\\Documents\\\\Projects\\\\DMT-DE\\\\Project Work\\\\translations\\\\"
        def factoryPath = path //+ "\\\\AddToFactoriesTest"
        factoryPath
    }

    def openFactoryFile(factoryPath) {
        def fileName = FileChooser.chooseFileAndReturnFilename("Select Library Factory to update", factoryPath)
        if (fileName != null) {
            try {
                classFactoryFile = new TextFile(fileName)
            } catch (Exception e) {
                println "Error: '$fileName' is not a text file."
            }
        }
    }

    def addTranslationFieldsToFactoryFile() {
        newText = ""
        if (classFactoryFile != null) {
            addQuestionIdentifier()
            addQuestionAndAnswers()
            addHelpText()
            addDescription()
        }
    }

    def addQuestionIdentifier() {

    }

    def addQuestionAndAnswers() {

    }

    def addHelpText() {

    }

    def addDescription() {

    }

    def writeTextToFactoryFile() {

    }

//    def closeUp() {
        //        def newText = addJapaneseTranslations(classFactoryFile)
//        if (newText != null) {
//            classFactoryFile.makeBackupFile()
//            classFactoryFile.setText(newText)
//        }

//    }
}

//    static doNothing(fp) {
//        }
//    }
//
//    static addJapaneseTranslations(TextFile classFactoryFile) {
//        def origText = classFactoryFile.getText()
//        def newText
//        newText = addJapaneseDescriptionKey(origText)
//        newText = addJapaneseQuestionKeys(newText)
//        newText
//    }
//
//    static addJapaneseDescriptionKey(origText) {
//        def findPattern = /(desc.*]).*]]/
//        def result = origText.replaceAll(findPattern) { m -> /${m[1]}, $linebreak"ja_JP": ["desc": '']]]/ }
//        result
//    }
//
//    static addJapaneseQuestionKeys(origText) {
//        def findPattern = /(?s)(localizationMap.*?helpText])]/
//        def result = origText.replaceAll(findPattern) { m -> /${m[1]}, $linebreak"ja_JP": ["txt": '', "title": '', $linebreak"helpText": '']]/ }
//        result
//    }



