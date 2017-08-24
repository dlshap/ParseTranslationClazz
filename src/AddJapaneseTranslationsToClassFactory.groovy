import FileManagement.TextFileMgr
import FileManagement.FileChooser

/**
 * Created by s0041664 on 8/24/2017.
 */
class AddJapaneseTranslationsToClassFactory {

    static addJapaneseDescriptionKey(origText) {
        def findPattern = /(desc.*]).*]]/
        def result = origText.replaceAll(findPattern) { m -> /${m[1]}, "ja_JP": ["desc": '']]]/ }
        result
    }

    static addJapaneseQuestionKeys(origText) {
        def findPattern = /(?s)(localizationMap.*?helpText])]/
        def result = origText.replaceAll(findPattern) { m -> /${m[1]}, "ja_JP": ["txt": '', "title": '', "helpText": '']]/ }
        result
    }

    static addJapaneseTranslations(TextFileMgr classFactoryFile) {
        def origText = classFactoryFile.getText()
        def newText
        newText = addJapaneseDescriptionKey(origText)
        newText = addJapaneseQuestionKeys(newText)
        newText
    }

    static main(args) {
        def fp = "C:\\\\Users\\\\s0041664\\\\Documents\\\\Projects\\\\DMT-DE\\\\Translations\\\\LibraryFactories"
        def fileName = new FileChooser().chooseFile(fp)
        if (fileName != null) {
            TextFileMgr classFactoryFile = new TextFileMgr(fileName)
            def newText = addJapaneseTranslations(classFactoryFile)
            if (newText != null) {
                classFactoryFile.makeBackupFile()
                classFactoryFile.setText(newText)
            }
        }
    }

}

