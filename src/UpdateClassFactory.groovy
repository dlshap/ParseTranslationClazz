import FileManagement.FileDirectoryMgr
import FileManagement.KeyFile
import FileManagement.LineFile
import LibraryQuestions.LibraryQuestionMatchers
import Logging.Log
import Translations.Translations
import Translations.Translation

/**
 * Created by s0041664 on 8/14/2017.
 */
class UpdateClassFactory {

    def replaceLineWithTranslations(nextLine, translation, bomFieldName) {
        def libraryQuestionTranslators = LibraryQuestionMatchers.getLibraryQuestionTranslators()
        libraryQuestionTranslators.each {
            // get field name from translator
            def translationKey = it.getValue("transKeyField")
            // get translation value from translation (keyfile)
            def translationValue = translation.getTranslationValue(translationKey)
            // translate it if there is a match...leave alone if not
            nextLine = it.translate(nextLine, translationValue, bomFieldName)
        }
        nextLine
    }

    def updateFactory(transFile, factoryFile, factoryOutFile) {
        def translations = new Translations(transFile)
        Translation translation
        if (factoryFile.hasNext()) {
            def bomFieldName = null
            def translationKeyName = null
            while (factoryFile.hasNext()) {
                def nextLine = factoryFile.nextLine()
                if (LibraryQuestionMatchers.lineContains(nextLine, "BOM Fields")) {
                    bomFieldName = LibraryQuestionMatchers.getFactoryMatchingValue(nextLine, "BOM Fields")
                    translationKeyName = LibraryQuestionMatchers.getValue("BOM Fields", "transKeyField")
                    translation = translations.getTranslation(translationKeyName, bomFieldName)
                    if (!translation) {
                        Log.writeLine "Missing translation for BOM Field: $bomFieldName"
                    }
                }
                if (translation) {
                    nextLine = replaceLineWithTranslations(nextLine, translation, bomFieldName)
                }
                factoryOutFile.writeLine(nextLine)
            }
        }
    }


    static main(args) {
        def fp = "C:\\\\Users\\\\s0041664\\\\Documents\\\\Projects\\\\DMT-DE\\\\Translations\\\\"
        Log.open(fp + "translate-library-log.txt")
        Log.open("overwrites", fp + "overwrites-log.txt")

        def fileList = new FileDirectoryMgr(fp + "Exports\\\\").getFileList()
        FileDirectoryMgr.makeDirectory(fp + "LibraryFactoriesTranslated\\\\")       // make it if it doesn't exist

//        fileList.forEach {                    // comment out for testing
        def it = "AviationExperience.txt"     /* for testing */

            Log.writeLine "\r\n$it:"
//            Log.writeLine("overwrites", "\r\n$it:")
            def transFile = new KeyFile(fp + "Exports\\\\" + it)
            def smallName = FileDirectoryMgr.getSmallName(it)
            def factoryFileName = smallName + "ClassFactory.groovy"
            def factoryFile = new LineFile(fp + "LibraryFactories\\\\" + factoryFileName)
            if (factoryFile.exists()) {
                def factoryOutFileName = factoryFileName + ".translated"
                def factoryOutFile = new LineFile(fp + "LibraryFactoriesTranslated\\\\" + factoryOutFileName, "create")
                def ucf = new UpdateClassFactory()
                ucf.updateFactory(transFile, factoryFile, factoryOutFile)
            } else {
                Log.writeLine "$factoryFileName doesn't exist"
            }
//        }                                     // comment out for testing
    }
}