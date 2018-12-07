import filemanagement.FileMgr
import filemanagement.FileDirectoryMgr
import filemanagement.KeyFile
import filemanagement.TextFile
import libraryquestions.LibraryFactory
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
    static excelExportFileName

    static classFileList = []
    static translationExcelExportFileList = []

    static translationsFromExcelExport
    static libraryClassFactoryParser
    static libraryClassFactoryWithNewTranslations

    static main(args) {
        buildArgsAndParameters(args)
        openLogs()
        translateFiles()
    }

    static buildArgsAndParameters(args) {
        getArgValues(args)
        getDefaultValuesIfArgsNull()
    }

    static getArgValues(args) {
        def argsMap = new ArgsParser(args)
        startFilePath = argsMap.get("path")
        languageName = argsMap.get("language")
        excelExportFileName = argsMap.get("file")
    }

    static getDefaultValuesIfArgsNull() {
        if (startFilePath == null) startFilePath = "C:\\\\Users\\\\s0041664\\\\Documents\\\\Projects\\\\DMT-DE\\\\Project Work\\\\translations\\\\DMT\\\\"
        if (languageName == null) languageName = "Japanese"
    }

    static openLogs() {
        def logsFilePath = startFilePath + "logs\\\\"
        Log.open logsFilePath + "log-library-translations.txt"
        Log.writeLine "Running on " + Dates.currentDateAndTime() + ":\r\n"
        Log.open "exceptions", logsFilePath + "log-library-exceptions.txt"
        Log.writeLine "exceptions", "Running on " + Dates.currentDateAndTime() + ":\r\n"
        Log.open "nocode", logsFilePath + "log-library-nocode.txt"
        Log.writeLine "nocode", "Running on " + Dates.currentDateAndTime() + ":\r\n"
    }

    static translateFiles() {
        setupForTranslations()
        doTranslations()
    }

    static setupForTranslations() {
        buildFileList()
        buildOutputDirectoryForUpdatedTranslations()
        cleanupAfterTranslations()
    }

    static buildFileList() {
        if (!(excelExportFileName == null)) {
            classFileList.add(FileDirectoryMgr.getSmallName(excelExportFileName))
        } else {
            buildFileListFromDirectory()
        }
    }

    static buildFileListFromDirectory() {
        translationExcelExportFileList = new FileDirectoryMgr(startFilePath + "LibraryExports\\\\").getFileList()
    }

    static buildOutputDirectoryForUpdatedTranslations() {
        FileDirectoryMgr.makeDirectory(startFilePath + "LibraryFactoriesTranslated\\\\")
    }

    static cleanupAfterTranslations() {
        closeLogs()
    }

    static closeLogs() {
        Log.writeLine("Done at: " + Dates.currentDateAndTime())
        Log.writeLine("exceptions", "Done at: " + Dates.currentDateAndTime())
        Log.writeLine("nocode", "Done at: " + Dates.currentDateAndTime())
    }

    static doTranslations() {
        Log.writeLine("Processing ${translationExcelExportFileList.size()} files: ${translationExcelExportFileList}")
        classFileList.forEach { nextClassFileName ->
            translateNextFileInFileList(nextClassFileName)
        }
    }

    static translateNextFileInFileList(nextClassFileName) {
        addFilenameToLogs(nextClassFileName)
        loadTranslationsFromExcelExport(nextClassFileName)
        loadLibraryFactoryParserFromLibraryFile(nextClassFileName)
        createLibraryFactoryWithNewTranslations(nextClassFileName)
        updateFactory()
    }

    static addFilenameToLogs(classFileName) {
        Log.writeLine "\r\n$classFileName:"
        Log.writeLine("exceptions", "\r\n$classFileName:")
        Log.writeLine("nocode", "\r\n$classFileName:")
    }

    static loadTranslationsFromExcelExport(classFileName) {
        def translationsFromExcelExportFile = new KeyFile(startFilePath + "LibraryExports\\\\" + classFileName + ".txt")
        if (translationsFromExcelExportFile.exists()) {
            translationsFromExcelExport = new Translations(translationsFromExcelExportFile)
        } else {
            Log.writeLine("exceptions", "Excel Export file: ${classFileName}.txt doesn't exist.")
        }
    }

    static createLibraryFactoryWithNewTranslations(classFileName) {
        def factoryTranslatedFileName = classFileName + ".translated"
        def factoryTranslatedPath = startFilePath + "LibraryFactoriesTranslated\\\\"
        libraryClassFactoryWithNewTranslations = new LibraryFactory(factoryTranslatedPath + factoryTranslatedFileName)
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


    static loadLibraryFactoryParserFromLibraryFile(fileName) {
        def groovyLibraryFileName = fileName + "ClassFactory.groovy"
        def groovyLibraryFile = new TextFile(startFilePath + "LibraryFactories\\\\" + groovyLibraryFileName)
        if (groovyLibraryFile.exists()) {
            libraryClassFactoryParser = new LibraryFileParser(groovyLibraryFile)
        } else {
            Log.writeLine "exceptions", "${groovyLibraryFile.getFullPathName()} doesn't exist"
        }
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

    static updateFactory() {
        if (libraryClassFactoryParser.hasNext()) {
            while (libraryClassFactoryParser.hasNext()) {
                def nextFactoryTextBlock = libraryClassFactoryParser.next()
                def bomFieldName = findBomFieldNameInText(nextFactoryTextBlock)
                if (bomFieldName != null) {
                    def translation = getTranslationForBomField(translationsFromExcelExport, bomFieldName)
                    if (translation != null) {
                        nextFactoryTextBlock = replaceLineWithTranslations(nextFactoryTextBlock, translation, bomFieldName)
                    }
                }
                libraryClassFactoryWithNewTranslations.add(nextFactoryTextBlock)
            }
        }
    }

}