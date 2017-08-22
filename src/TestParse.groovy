import FileManagement.FileMgr
import FileManagement.FileDirectoryMgr
import FileManagement.KeyFileMgr
import FileManagement.LineFileMgr
import FileManagement.TextFileMgr
import LibraryQuestions.LibraryFileParser
import LibraryQuestions.LibraryQuestionMatchers
import Logging.Log
import Translations.Translations
import Translations.Translation

/**
 * Created by s0041664 on 8/18/2017.
 */
class TestParse {

    def nextText = """
currentAttr = new ClazzAttr([name: 'groundedInd', type: 'Boolean', localizedAttributesMap:
            ["en_US": [desc: description],
             "ja_JP": [desc: '申込者が航空業務の停止処分を受けたかどうか。技能証明の取消等も含む。']]] )
        currentClazz.attrs.push(currentAttr)
        defaultQuestion = 'Have you ever been grounded or had your license revoked?'
        localizationMap = ["en_US": [txt: defaultQuestion, title: 'Grounded Indicator', helpText: null],
                           "ja_JP": [txt: '業務停止もしくは技能証明の取消を受けたことはありますか。',
                                     title: '業務停止インジケーター', helpText: null]]
        currentAttr.quests.push ( buildRadioQuestion(currentClazz, currentAttr, localizationMap) )

        description = 'FAA Medical Examination Class obtained'
        """

    def replaceLineWithTranslations(nextText, translation, bomFieldName) {
        def libraryQuestionTranslators = LibraryQuestionMatchers.getLibraryQuestionTranslators()
        libraryQuestionTranslators.each {
            // get field name from translator
            def translationKey = it.getValue("transKeyField")
            // get translation value from translation (keyfile)
            def translationValue = translation.getTranslationValue(translationKey)
            // translate it if there is a match...leave alone if not
            if (translationValue) {
                nextText = it.translate(nextText, translationValue, bomFieldName)
            }
        }
        nextText
    }

    def updateFactory(transFile, TextFileMgr factoryFile, TextFileMgr factoryOutFile) {

        def translations = new Translations(transFile)
        Translations.Translation translation
        def bomFieldName = null
        def translationKeyName = null
        if (LibraryQuestionMatchers.lineContains(nextText, "BOM Fields")) {
            bomFieldName = LibraryQuestionMatchers.getFactoryMatchingValue(nextText, "BOM Fields")
            translationKeyName = LibraryQuestionMatchers.getValue("BOM Fields", "transKeyField")
            translation = translations.getTranslation(translationKeyName, bomFieldName)
            if (!translation) {
                Log.writeLine "Missing translation for BOM Field: $bomFieldName"
            }
        }
        if (translation) {
            def newText = replaceLineWithTranslations(nextText, translation, bomFieldName)
            nextText = newText
//            Log.writeLine("overwrites", newText)
        }
        factoryOutFile.writeToFile(nextText)
    }


    static main(args) {
        def fp = "C:\\\\Users\\\\s0041664\\\\Documents\\\\Projects\\\\DMT-DE\\\\Translations\\\\"
        Log.open(fp + "translate-library-log.txt")
        Log.open("overwrites", fp + "overwrites-log.txt")

        def fileList = new FileDirectoryMgr(fp + "Exports\\\\").getFileList()
        FileDirectoryMgr.makeDirectory(fp + "LibraryFactoriesTranslated\\\\")       // make it if it doesn't exist

        def it = "AviationExperience.txt"     /* for testing */

        Log.writeLine "\r\n$it:"
        def transFile = new KeyFileMgr(fp + "Exports\\\\" + it)
        def smallName = FileDirectoryMgr.getSmallName(it)
        def factoryFileName = smallName + "ClassFactory.groovy"
        def factoryFile = new TextFileMgr(fp + "LibraryFactories\\\\" + factoryFileName)
        if (factoryFile.exists()) {
            def factoryOutFileName = factoryFileName + ".translated"
            def factoryOutFile = new TextFileMgr(fp + "LibraryFactoriesTranslated\\\\" + factoryOutFileName, FileMgr.createFlag.CREATE)

            // do it
            new TestParse().updateFactory(transFile, factoryFile, factoryOutFile)

        } else {
            Log.writeLine "$factoryFileName doesn't exist"
        }
    }
}
