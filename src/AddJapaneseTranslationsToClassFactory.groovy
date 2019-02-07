import filemanagement.TextFile
import filemanagement.FileChooser

/**
 * Created by s0041664 on 8/24/2017.
 */
class AddJapaneseTranslationsToClassFactory {

    static final linebreak = "\r\n             "

    static main(args) {
        def fp = getFilePath(args)
        def factoryPath = fp //+ "\\\\AddToFactoriesTest"
        def fileName = FileChooser.chooseFileAndReturnFilename("Select Library Factory to update", factoryPath)
        if (fileName != null) {
            TextFile classFactoryFile = new TextFile(fileName)
            def newText = addJapaneseTranslations(classFactoryFile)
            if (newText != null) {
                classFactoryFile.makeBackupFile()
                classFactoryFile.setText(newText)
            }
        }
    }

    static getFilePath(args) {
        String fp //filepath
        def lastChar
        if (args.size() == 0)
            fp = "C:\\\\Users\\\\s0041664\\\\Documents\\\\Projects\\\\DMT-DE\\\\Project Work\\\\translations\\\\"
        else {
            fp = args[0]
            if (fp[-1] != "\\") fp += "\\"
        }
        fp
    }

    static addJapaneseTranslations(TextFile classFactoryFile) {
        def origText = classFactoryFile.getText()
        def newText
        newText = addJapaneseDescriptionKey(origText)
        newText = addJapaneseQuestionKeys(newText)
        newText
    }

    static addJapaneseDescriptionKey(origText) {
        def findPattern = /(desc.*]).*]]/
        def result = origText.replaceAll(findPattern) { m -> /${m[1]}, $linebreak"ja_JP": ["desc": '']]]/ }
        result
    }

    static addJapaneseQuestionKeys(origText) {
        def findPattern = /(?s)(localizationMap.*?helpText])]/
        def result = origText.replaceAll(findPattern) { m -> /${m[1]}, $linebreak"ja_JP": ["txt": '', "title": '', $linebreak"helpText": '']]/ }
        result
    }

}

