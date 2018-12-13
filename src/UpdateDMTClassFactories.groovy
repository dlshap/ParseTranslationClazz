import filemanagement.FileDirectoryMgr
import filemanagement.KeyFile
import filemanagement.TextFile
import libraryquestions.LibraryFactory
import libraryquestions.LibraryFactoryBlockKey
import libraryquestions.LibraryFactoryParser
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
    static testFileName

    static excelExportFileList = []
    static translationExcelExportFileList = []

    static translationsFromExcelExport
    static libraryFactoryParser
    static libraryClassFactoryWithNewTranslations

    static nextFactoryTextBlock
    static translationFromExcelExport
    static libraryFactoryBlockKey

    static main(args) {
        buildArgsAndParameters(args)
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
        testFileName = argsMap.get("file")
    }

    static getDefaultValuesIfArgsNull() {
        if (startFilePath == null) startFilePath = "C:\\\\Users\\\\s0041664\\\\Documents\\\\Projects\\\\DMT-DE\\\\Project Work\\\\translations\\\\DMT\\\\"
        if (languageName == null) languageName = "Japanese"
    }

    static translateFiles() {
        openLogs()
        setupForTranslations()
        doTranslations()
        cleanupAfterTranslations()
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

    static setupForTranslations() {
        buildFileList()
        buildOutputDirectoryForUpdatedTranslations()
    }

    static buildFileList() {
        if (!(testFileName == null)) {
            excelExportFileList.add(FileDirectoryMgr.getSmallName(testFileName))
        } else {
            buildFileListFromExcelExportDirectory()
        }
    }

    static buildFileListFromExcelExportDirectory() {
        excelExportFileList = new FileDirectoryMgr(startFilePath + "LibraryExports\\\\").getFileList()
        excelExportFileList = excelExportFileList.collect { it - ~/\.\w{3}/ }             // remove file extension
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
        excelExportFileList.forEach { nextExcelExportFileName ->
            translateNextFileInFileList(nextExcelExportFileName)
        }
    }

    static translateNextFileInFileList(classFileName) {
        addFilenameToLogs(classFileName)
        loadTranslationsFromExcelExport(classFileName)
        loadLibraryFactoryParserFromLibraryFactoryFile(classFileName)
        createLibraryFactoryForNewTranslations(classFileName)
        updateLibraryFactoryFromExcelTranslations()
    }

    static addFilenameToLogs(classFileName) {
        Log.writeLine "\r\n$classFileName:"
        Log.writeLine("exceptions", "\r\n$classFileName:")
        Log.writeLine("nocode", "\r\n$classFileName:")
    }

    static loadTranslationsFromExcelExport(classFileName) {
        def translationsFromExcelExportFileName = classFileName + ".txt"
        def translationsFromExcelExportPath = startFilePath + "LibraryExports\\\\"
        def translationsFromExcelExportFile = new KeyFile(translationsFromExcelExportPath + translationsFromExcelExportFileName)
        if (translationsFromExcelExportFile.exists()) {
            translationsFromExcelExport = new Translations(translationsFromExcelExportFile)
        } else {
            Log.writeLine("exceptions", "Excel Export file: ${classFileName}.txt doesn't exist.")
        }
    }

    static createLibraryFactoryForNewTranslations(classFileName) {
        def factoryTranslatedFileName = classFileName + ".translated"
        def factoryTranslatedPath = startFilePath + "LibraryFactoriesTranslated\\\\"
        libraryClassFactoryWithNewTranslations = new LibraryFactory(factoryTranslatedPath + factoryTranslatedFileName)
    }

    static updateLibraryFactoryFromExcelTranslations() {
        while (libraryFactoryParser.hasNext()) {
            getNextFactoryTextBlock()
            getTranslationsForNextFactoryTextBlock()
        }
        WriteTranslatedFactoryTextBlockToTranslatedFile()
    }

    static getNextFactoryTextBlock() {
        nextFactoryTextBlock = libraryFactoryParser.next()
    }

    static getTranslationsForNextFactoryTextBlock() {
        getKeysFromFactoryTextBlock()
        getTranslationsForFactoryTextBlockKeys()
        updateFactoryTextBlockWithTranslatedColumns()
    }

    static getKeysFromFactoryTextBlock() {
        def bomFieldName = findBomFieldNameInText()
        def questionIdentifier = findQuestionIdentifierInText()
        if ((bomFieldName != null) || (questionIdentifier != null))
        libraryFactoryBlockKey = new LibraryFactoryBlockKey(["BOM Fields": bomFieldName, "Question Identifier": questionIdentifier])
    }

    static findBomFieldNameInText() {
        def bomFieldName = null
        if (LibraryQuestionMatchers.lineContains(nextFactoryTextBlock, "BOM Fields")) {
            bomFieldName = LibraryQuestionMatchers.findValueInTextUsingLibraryQuestionMatcher(nextFactoryTextBlock, "BOM Fields")
        }
        bomFieldName
    }

    static findQuestionIdentifierInText() {
        def questionIdentifier = null
        if (LibraryQuestionMatchers.lineContains(nextFactoryTextBlock, "Question Identifier")) {
            questionIdentifier = LibraryQuestionMatchers.findValueInTextUsingLibraryQuestionMatcher(nextFactoryTextBlock, "Question Identifier")
        }
        questionIdentifier
    }

    static getTranslationsForFactoryTextBlockKeys() {
        if (libraryFactoryBlockKey != null) {
            def bomFieldName = libraryFactoryBlockKey.getKey("BOM Fields")
            translationFromExcelExport = getTranslationForBomField(bomFieldName)
        }
    }

    static getTranslationForBomField(bomFieldName) {
        def translationKeyName = LibraryQuestionMatchers.getLibraryQuestionRegexMapValue("BOM Fields", "excelColumnName")
        def translation = translationsFromExcelExport.getTranslation(translationKeyName, bomFieldName)
        if (translation == null) {
            Log.writeLine "exceptions", "Missing translation for BOM Field: $bomFieldName"
        }
        translation
    }

    static updateFactoryTextBlockWithTranslatedColumns() {
        if (translationFromExcelExport != null) {
            def bomFieldName = libraryFactoryBlockKey.getKey("BOM Fields")
            nextFactoryTextBlock = replaceLineWithTranslations(nextFactoryTextBlock, translationFromExcelExport, bomFieldName)
        }
    }

    static WriteTranslatedFactoryTextBlockToTranslatedFile() {
        libraryClassFactoryWithNewTranslations.add(nextFactoryTextBlock)
    }


    static loadLibraryFactoryParserFromLibraryFactoryFile(fileName) {
        def libraryFactoryFileName = fileName + "ClassFactory.groovy"
        def libraryFactoryFile = new TextFile(startFilePath + "LibraryFactories\\\\" + libraryFactoryFileName)
        if (libraryFactoryFile.exists()) {
            libraryFactoryParser = new LibraryFactoryParser(libraryFactoryFile)
        } else {
            Log.writeLine "exceptions", "${libraryFactoryFile.getFullPathName()} doesn't exist"
        }
    }

    static replaceLineWithTranslations(nextFactoryBlock, translation, bomFieldName) {
        def libraryQuestionTranslators = LibraryQuestionMatchers.getLibraryQuestionTranslators()
        libraryQuestionTranslators.eachWithIndex { it, i ->
            // get field name from translator
            def translationKey = it.getValue("excelColumnName")
            if (translationKey.toLowerCase().contains("translated")) {
                // get translation value from translation (keyfile)
                def translationValue = translation.get(translationKey)
                // translate it if there is a match...leave alone if not
                if (translationValue != "") {
                    nextFactoryBlock = it.translate(nextFactoryBlock, translationValue, bomFieldName)
                }
            }
        }
        nextFactoryBlock
    }

}