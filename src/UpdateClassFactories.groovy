import FileManagement.FileMgr
import FileManagement.FileDirectoryMgr
import FileManagement.KeyFile
import FileManagement.TextFile
import LibraryQuestions.LibraryFileParser
import LibraryQuestions.LibraryQuestionMatchers
import Logging.Dates
import Logging.Log
import Translations.Translations

/**
 * Created by s0041664 on 8/14/2017.
 */
class UpdateClassFactories {


    static openLogs(fp) {
        Log.open(fp + "log-library-translations.txt")
        Log.writeLine("Running on: " + Dates.currentDateAndTime())
        Log.open("exceptions", fp + "log-library-exceptions.txt")
        Log.writeLine("exceptions", "Running on: " + Dates.currentDateAndTime())
        Log.open("nocode", fp + "log-library-nocode.txt")
        Log.writeLine("nocode", "Running on: " + Dates.currentDateAndTime())
    }

   static findBomFieldNameInText(nextText) {
        def bomFieldName = null
        if (LibraryQuestionMatchers.lineContains(nextText, "BOM Fields")) {
            bomFieldName = LibraryQuestionMatchers.getFactoryMatchingValue(nextText, "BOM Fields")
        }
        bomFieldName
    }

    static getTranslationForBomField(translations, bomFieldName) {
        def translationKeyName = LibraryQuestionMatchers.getValue("BOM Fields", "transKeyField")
        def translation = translations.getTranslation(translationKeyName, bomFieldName)
        if (translation == null) {
            Log.writeLine "exceptions", "Missing translation for BOM Field: $bomFieldName"
        }
        translation
    }


    static openTranslationFile(fp, fileName) {
        def transFile = new KeyFile(fp + "LibraryExports\\\\" + fileName)
        if (!transFile.exists()) {
            Log.writeLine("exceptions", "Translation file: $fileName doesn't exist.")
        }
        transFile
    }

    static openFactoryFile(fp, fileName) {
        def smallName = FileDirectoryMgr.getSmallName(fileName)       // no file extension
        def factoryFileName = smallName + "ClassFactory.groovy"
        def factoryFile = new TextFile(fp + "LibraryFactories\\\\" + factoryFileName)
        if (!factoryFile.exists()) {
            Log.writeLine "${factoryFile.getFullPathName()} doesn't exist"
        }
        factoryFile
    }

    static openFactoryTranslatedFile(fp, fileName) {
        def factoryTranslatedFileName = fileName + ".translated"
        def factoryTranslatedPath = fp + "LibraryFactoriesTranslated\\\\"
        def factoryTranslatedFile = new TextFile(factoryTranslatedPath + factoryTranslatedFileName, FileMgr.createFlag.CREATE)
        factoryTranslatedFile
    }

    static addFileToLogs(fileName) {
        Log.writeLine "\r\n$fileName:"
        Log.writeLine("exceptions", "\r\n$fileName:")
        Log.writeLine("nocode", "\r\n$fileName:")
    }

    static addDoneToLogs() {
        Log.writeLine("Done at: " + Dates.currentDateAndTime())
        Log.writeLine("exceptions", "Done at: " + Dates.currentDateAndTime())
        Log.writeLine("nocode", "Done at: " + Dates.currentDateAndTime())
    }

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

    static updateFactory(transFile, TextFile factoryFile, TextFile factoryOutFile) {
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

    static buildFileList(fp) {
        def fileList = new FileDirectoryMgr(fp + "LibraryExports\\\\").getFileList()
        FileDirectoryMgr.makeDirectory(fp + "LibraryFactoriesTranslated\\\\")       // make it if it doesn't exist
        fileList
    }

    static getFilePath(args) {
        def fp //filepath
        if (args.size() == 0)
            fp = "C:\\\\Users\\\\s0041664\\\\Documents\\\\Projects\\\\DMT-DE\\\\Translations\\\\"
        else
            fp = args[0]
        fp
    }

    static getFileList(fp, args) {
        def fileList
        if (args.size() > 1) {
             fileList = [args[1]]
        } else {
             fileList = buildFileList(fp)
        }
        fileList
    }

    static main(args) {
        def fp = getFilePath(args)
        openLogs(fp)
        def fileList = getFileList(fp, args)
        Log.writeLine("Processing ${fileList.size()} files: ${fileList}")
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