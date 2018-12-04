import filemanagement.FileMgr
import filemanagement.FileDirectoryMgr
import filemanagement.KeyFile
import filemanagement.TextFile
import libraryquestions.LibraryFileParser
import libraryquestions.LibraryQuestionMatchers
import logging.Dates
import logging.Log
import translations.Translations
import useful.ArgsParser

/**
 * Created by s0041664 on 8/14/2017.
 */
class UpdateDMTClassFactories {

    static startFilePath
    static languageName
    static fileList

    static main(args) {
        buildArgsAndParameters(args)
        openLogs(startFilePath)
        buildFileList()
        translateLibraryFiles()
    }

    static translateLibraryFiles() {
        Log.writeLine("Processing ${fileList.size()} files: ${fileList}")
        fileList.forEach {
            addFileToLogs(it)
            def transFile = openTranslationFile(startFilePath, it)
            def factoryFile = openFactoryFile(startFilePath, it)
            if (transFile.exists() && factoryFile.exists()) {
                def factoryOutFile = openFactoryTranslatedFile(startFilePath, factoryFile.getFileName())
                updateFactory(transFile, factoryFile, factoryOutFile)
            }
        }
        addDoneToLogs()
    }

    static buildArgsAndParameters(args) {
        getArgValues(args)
        getDefaultValuesIfArgsNull()
    }

    static getArgValues(args) {
        def argsMap = new ArgsParser(args)
        languageName = argsMap.get("language")
        startFilePath = argsMap.get("path")
    }

    static getDefaultValuesIfArgsNull() {
        if (startFilePath == null) startFilePath = "C:\\\\Users\\\\s0041664\\\\Documents\\\\Projects\\\\DMT-DE\\\\Project Work\\\\translations\\\\DMT\\\\"
        if (languageName == null) languageName = "Japanese"
    }

    static openLogs(fp) {
        def logFp = fp + "logs\\\\"
        Log.open logFp + "log-library-translations.txt"
        Log.writeLine "Running on " + Dates.currentDateAndTime() + ":\r\n"
        Log.open "exceptions", logFp + "log-library-exceptions.txt"
        Log.writeLine "exceptions", "Running on " + Dates.currentDateAndTime() + ":\r\n"
        Log.open "nocode", logFp + "log-library-nocode.txt"
        Log.writeLine "nocode", "Running on " + Dates.currentDateAndTime() + ":\r\n"
    }

    static buildFileList() {
        //TODO: Get single file argument from args object
//        def fileList
//        if (args.size() > 1) {
//            fileList = [args[1]]
//        } else {
//            fileList = getFilesInLibraryFactoryDirectory()
//        }
        getFilesInLibraryFactoryDirectory()
    }

    static getFilesInLibraryFactoryDirectory() {
        fileList = new FileDirectoryMgr(startFilePath + "LibraryExports\\\\").getFileList()
        FileDirectoryMgr.makeDirectory(startFilePath + "LibraryFactoriesTranslated\\\\")
    }


//    static getFilePath(args) {
//        def fp //filepath
//        if (args.size() == 0)
//            fp = "C:\\\\Users\\\\s0041664\\\\Documents\\\\Projects\\\\DMT-DE\\\\Project Work\\\\translations\\\\"
//        else {
//            fp = args[0]
//            if (fp[-1] != "\\") fp += "\\"
//        }
//        fp
//    }


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
            Log.writeLine "exceptions", "${factoryFile.getFullPathName()} doesn't exist"
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

    static replaceLineWithTranslations(nextFactoryBlock, translation, bomFieldName) {
        def libraryQuestionTranslators = LibraryQuestionMatchers.getLibraryQuestionTranslators()
        libraryQuestionTranslators.eachWithIndex { it, i ->
            // get field name from translator
            def translationKey = it.getValue("transKeyField")
            // get translation value from translation (keyfile)
            def translationValue = translation.get(translationKey)
            // translate it if there is a match...leave alone if not
            if (translationValue != "") {
                nextFactoryBlock = it.translate(nextFactoryBlock, translationValue, bomFieldName)
            }
        }
        nextFactoryBlock
    }

    static updateFactory(transFile, TextFile factoryFile, TextFile factoryOutFile) {
        def factoryParser = new LibraryFileParser(factoryFile)
        def translations = new Translations(transFile)
        if (factoryParser.hasNext()) {
            while (factoryParser.hasNext()) {
                def nextFactoryTextBlock = factoryParser.next()
                def bomFieldName = findBomFieldNameInText(nextFactoryTextBlock)
                if (bomFieldName != null) {
                    def translation = getTranslationForBomField(translations, bomFieldName)
                    if (translation != null) {
                        nextFactoryTextBlock = replaceLineWithTranslations(nextFactoryTextBlock, translation, bomFieldName)
                    }
                }
                factoryOutFile.writeToFile(nextFactoryTextBlock)
            }
        }
    }

}