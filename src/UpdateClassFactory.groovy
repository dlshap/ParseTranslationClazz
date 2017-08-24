import FileManagement.FileMgr
import FileManagement.FileDirectoryMgr
import FileManagement.KeyFileMgr
import FileManagement.TextFileMgr
import LibraryQuestions.LibraryFileParser
import LibraryQuestions.LibraryQuestionMatchers
import Logging.Dates
import Logging.Log
import Translations.Translations
import Translations.Translation

/**
 * Created by s0041664 on 8/14/2017.
 */
class UpdateClassFactory {


    static replaceLineWithTranslations(nextText, translation, bomFieldName) {
        def libraryQuestionTranslators = LibraryQuestionMatchers.getLibraryQuestionTranslators()
        libraryQuestionTranslators.eachWithIndex { it, i ->
            // get field name from translator
            def translationKey = it.getValue("transKeyField")
            // get translation value from translation (keyfile)
            def translationValue = translation.getTranslationValue(translationKey)
            // translate it if there is a match...leave alone if not
            if (translationValue != "") {
                nextText = it.translate(nextText, translationValue, bomFieldName)
            }
        }
        nextText
    }

    static findBomFieldNameInText(nextText) {
        def bomFieldName
        if (LibraryQuestionMatchers.lineContains(nextText, "BOM Fields")) {
            bomFieldName = LibraryQuestionMatchers.getFactoryMatchingValue(nextText, "BOM Fields")
        }
        bomFieldName
    }

    static getTranslationForBomField(translations, bomFieldName) {
        def translationKeyName = LibraryQuestionMatchers.getValue("BOM Fields", "transKeyField")
        def translation = translations.getTranslation(translationKeyName, bomFieldName)
        if (translation == null) {
            Log.writeLine "Missing translation for BOM Field: $bomFieldName"
        }
        translation
    }


    static updateFactory(transFile, TextFileMgr factoryFile, TextFileMgr factoryOutFile) {
        def factoryParser = new LibraryFileParser(factoryFile)
        def translations = new Translations(transFile)
        if (factoryParser.hasNext()) {
            while (factoryParser.hasNext()) {
                def nextText = factoryParser.next()
                def bomFieldName = findBomFieldNameInText(nextText)
                if (bomFieldName != null) {
                    def translation = getTranslationForBomField(translations, bomFieldName)
                    if (translation != null) {
                        nextText = replaceLineWithTranslations(nextText, translation, bomFieldName)
                    }
                }
                factoryOutFile.writeToFile(nextText)
            }
        }
    }

    static openLogs(fp) {
        Log.open(fp + "log-translate-library.txt")
        Log.writeLine("Running on: " + Dates.currentDateAndTime())
        Log.open("overwrites", fp + "log-overwrites.txt")
        Log.writeLine("overwrites", "Running on: " + Dates.currentDateAndTime())
        Log.open("nocode", fp + "log-nocode.txt")
        Log.writeLine("nocode", "Running on: " + Dates.currentDateAndTime())
    }

    static getFileList(fp) {
        def fileList = new FileDirectoryMgr(fp + "Exports\\\\").getFileList()
        FileDirectoryMgr.makeDirectory(fp + "LibraryFactoriesTranslated\\\\")       // make it if it doesn't exist
        fileList
    }

    static openTranslationFile(fp, fileName) {
        def transFile = new KeyFileMgr(fp + "Exports\\\\" + fileName)
        if (!transFile.exists()) {
            Log.writeLine("Translation file: $fileName doesn't exist.")
        }
        transFile
    }

    static openFactoryFile(fp, fileName) {
        def smallName = FileDirectoryMgr.getSmallName(fileName)       // no file extension
        def factoryFileName = smallName + "ClassFactory.groovy"
        def factoryFile = new TextFileMgr(fp + "LibraryFactories\\\\" + factoryFileName)
        if (!factoryFile.exists()) {
            Log.writeLine "${factoryFile.getFullPathName()} doesn't exist"
        }
        factoryFile
    }

    static openFactoryTranslatedFile(fp, fileName) {
        def factoryTranslatedFileName = fileName + ".translated"
        def factoryTranslatedPath = fp + "LibraryFactoriesTranslated\\\\"
        def factoryTranslatedFile = new TextFileMgr(factoryTranslatedPath + factoryTranslatedFileName, FileMgr.createFlag.CREATE)
        factoryTranslatedFile
    }

    static addFileToLogs(fileName) {
        Log.writeLine "\r\n$fileName:"
        Log.writeLine("overwrites", "\r\n$fileName:")
        Log.writeLine("nocode", "\r\n$fileName:")
    }

    static addDoneToLogs() {
        Log.writeLine("Done at: " + Dates.currentDateAndTime())
        Log.writeLine("overwrites", "Done at: " + Dates.currentDateAndTime())
        Log.writeLine("nocode", "Done at: " + Dates.currentDateAndTime())
    }

    static main(args) {
        /** ****************************************************/
        def testFile = ""   // "" if not testing a single file
        /** ****************************************************/

        def fp = "C:\\\\Users\\\\s0041664\\\\Documents\\\\Projects\\\\DMT-DE\\\\Translations\\\\"
        openLogs(fp)

        def fileList = [testFile]
        if (testFile == "") {
            fileList = getFileList(fp)
        }
        fileList.forEach {
            addFileToLogs(it)
            def transFile = openTranslationFile(fp, it)
            def factoryFile = openFactoryFile(fp, it)
            if (transFile.exists() && factoryFile.exists()) {
                def factoryOutFile = openFactoryTranslatedFile(fp, factoryFile.getFileName())
                updateFactory(transFile, factoryFile, factoryOutFile)
            }
        }
        addDoneToLogs()
    }
}