import FileManagement.TextFile
import FileManagement.FileChooser

/**
 * Created by s0041664 on 8/24/2017.
 */
class AddJapaneseTranslationsToClassFactory {

    static addJapaneseDescriptionKey(origText) {
        def crlf = "\r\n"
        def findPattern = /(desc.*]).*]]/
        def result = origText.replaceAll(findPattern) { m -> /${m[1]}, $crlf"ja_JP": [$crlf"desc": '']]]/ }
        result
    }

    static addJapaneseQuestionKeys(origText) {
        def crlf = "\r\n"
        def findPattern = /(?s)(localizationMap.*?helpText])]/
        def result = origText.replaceAll(findPattern) { m -> /${m[1]}, $crlf"ja_JP": ["txt": '', "title": '', $crlf"helpText": '']]/ }
        result
    }

    static addJapaneseTranslations(TextFile classFactoryFile) {
        def origText = classFactoryFile.getText()
        def newText
        newText = addJapaneseDescriptionKey(origText)
        newText = addJapaneseQuestionKeys(newText)
        newText
    }

    static getFilePath(args) {
        def fp //filepath
        if (args.size() == 0)
            fp = "C:\\\\Users\\\\s0041664\\\\Documents\\\\Projects\\\\DMT-DE\\\\Translations\\\\"
        else
            fp = args[0]
        fp
    }

    static main(args) {
        def fp = getFilePath(args)
        def factoryPath = fp + "\\\\LibraryFactories"
        def fileName = FileChooser.chooseFile("Select Library Factory to update", factoryPath)
        if (fileName != null) {
            TextFile classFactoryFile = new TextFile(fileName)
            def newText = addJapaneseTranslations(classFactoryFile)
            if (newText != null) {
                classFactoryFile.makeBackupFile()
                classFactoryFile.setText(newText)
            }
        }
    }
}

