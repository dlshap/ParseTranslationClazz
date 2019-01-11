import filemanagement.FileDirectoryMgr
import filemanagement.KeyFile
import filemanagement.TextFile
import libraryquestions.LibraryArgs
import libraryquestions.LibraryFactory
import libraryquestions.LibraryQuestionTranslator
import translations.Translation
import translations.TranslationFieldKeys
import libraryquestions.LibraryFactoryParser
import libraryquestions.LibraryQuestionFieldFinder
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
    static fileNameForTestingSingleFile

    def libraryArgs

    static excelExportFileList = []

    static libraryFactoryParser
    static libraryFactoryWithNewTranslations
    static nextFactoryTextBlock

    static Translations translationsFromExcelExport
    static Translation matchingTranslationFromExcelExport
    static TranslationFieldKeys translationFieldKeys

    static LibraryQuestionFieldFinder libraryQuestionFieldFinder

    UpdateDMTClassFactories(args) {
        start(args)
    }

    static main(args) {
        new UpdateDMTClassFactories(args)
    }

    def start(args) {
        buildArgsAndParameters(args)
        translateFiles()
    }

    def buildArgsAndParameters(args) {
        libraryArgs = new LibraryArgs(args)
        getDefaultValuesIfArgsNull(libraryArgs)
    }

    def getDefaultValuesIfArgsNull(libraryArgs) {
        if (libraryArgs.startFilePath == null) libraryArgs.startFilePath = "C:\\\\Users\\\\s0041664\\\\Documents\\\\Projects\\\\DMT-DE\\\\Project Work\\\\translations\\\\DMT\\\\"
        if (libraryArgs.languageName == null) libraryArgs.languageName = "Japanese"
        //temp
        startFilePath = libraryArgs.startFilePath
        languageName = libraryArgs.languageName
        fileNameForTestingSingleFile = libraryArgs.fileNameForTestingSingleFile
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
        if (fileNameForTestingSingleFile != null) {
            excelExportFileList.add(FileDirectoryMgr.getSmallName(fileNameForTestingSingleFile))
        } else {
            buildExcelExportFileListFromDirectoryList()
        }
    }

    static buildExcelExportFileListFromDirectoryList() {
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
        Log.writeLine("Processing ${excelExportFileList.size()} files: ${excelExportFileList}")
        excelExportFileList.forEach { nextExcelExportFileName ->
            translateNextFileInFileList(nextExcelExportFileName)
        }
    }

    static translateNextFileInFileList(classFileName) {
        addFilenameToLogs(classFileName)
        loadTranslationsFromExcelExport(classFileName)
        loadLibraryFactoryParserFromLibraryFactoryFile(classFileName)
        createLibraryFactoryForUpdatedTranslations(classFileName)
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

    static createLibraryFactoryForUpdatedTranslations(classFileName) {
        def factoryTranslatedPath = startFilePath + "LibraryFactoriesTranslated\\\\"
        def factoryTranslatedFileName = classFileName + "ClassFactory.translated"
        libraryFactoryWithNewTranslations = new LibraryFactory(factoryTranslatedPath + factoryTranslatedFileName)
    }

    static updateLibraryFactoryFromExcelTranslations() {
        while (libraryFactoryParser.hasNext()) {
            getNextFactoryTextBlock()
            getTranslationsForNextFactoryTextBlock()
            WriteTranslatedFactoryTextBlockToTranslatedFile()
        }
    }

    static getNextFactoryTextBlock() {
        nextFactoryTextBlock = libraryFactoryParser.next()
    }

    static getTranslationsForNextFactoryTextBlock() {
        getKeysFromFactoryTextBlock()
        getTranslationForKeys()
        replaceLineWithTranslations()
    }

    static getKeysFromFactoryTextBlock() {
        def bomFieldName = findBomFieldNameInText()
        def questionIdentifier = findQuestionIdentifierInText()
        if ((bomFieldName != null) || (questionIdentifier != null))
            translationFieldKeys = new TranslationFieldKeys(["BOM Fields": bomFieldName, "Question Identifier": questionIdentifier])
        else
            translationFieldKeys = null
    }

    static findBomFieldNameInText() {
        def bomFieldName = null
        if (libraryQuestionFieldFinder.lineContains(nextFactoryTextBlock, "BOM Fields")) {
            bomFieldName = libraryQuestionFieldFinder.findFieldInLibraryText(nextFactoryTextBlock, languageName, "BOM Fields")
        }
        bomFieldName
    }

    static findQuestionIdentifierInText() {
        def questionIdentifier = null
        if (libraryQuestionFieldFinder.lineContains(nextFactoryTextBlock, "Question Identifier")) {
            questionIdentifier = libraryQuestionFieldFinder.findFieldInLibraryText(nextFactoryTextBlock, "Question Identifier")
        }
        questionIdentifier
    }

    static getTranslationForKeys() {
        /*
        get translations for BOM Fields key...if more than one, get translations for question identifier and BOM Fields key (or null if no match)
         */
        matchingTranslationFromExcelExport = null
        if (translationFieldKeys != null) {
            def matchingTranslations = translationsFromExcelExport.getTranslations(translationFieldKeys)
            if (singleMatchingTranslation(matchingTranslations)) {
                matchingTranslationFromExcelExport = matchingTranslations[0]
            }
        }
    }

    static singleMatchingTranslation(matchingTranslations) {
        def translationCount = matchingTranslations.size()
        if (translationCount == 1)
            return true
        else if (translationCount == 0) {
            Log.writeLine "exceptions", "Missing translation for keys: ${translationFieldKeys.getKeyList()}"
            return false
        } else if (translationCount > 1) {
             Log.writeLine "exceptions", "Multiple translations for keys: ${translationFieldKeys.getKeyList()}"
            return false
        }

    }

    static WriteTranslatedFactoryTextBlockToTranslatedFile() {
        libraryFactoryWithNewTranslations.add(nextFactoryTextBlock)
    }


    static loadLibraryFactoryParserFromLibraryFactoryFile(fileName) {
        def libraryFactoryFileName = fileName + "ClassFactory.groovy"
        def libraryFactoryFile = new TextFile(startFilePath + "LibraryFactories\\\\" + libraryFactoryFileName)
        if (libraryFactoryFile.exists()) {
            libraryFactoryParser = new LibraryFactoryParser(libraryFactoryFile)
            libraryQuestionFieldFinder = new LibraryQuestionFieldFinder(languageName)
        } else {
            Log.writeLine "exceptions", "${libraryFactoryFile.getFullPathName()} doesn't exist"
        }
    }

    static replaceLineWithTranslations() {
        if ((translationFieldKeys != null) && (matchingTranslationFromExcelExport != null)) {
            def tryToTranslateFactoryTextBlock = nextFactoryTextBlock
            def libraryQuestionTranslators = libraryQuestionFieldFinder.getLibraryQuestionTranslators()
            libraryQuestionTranslators.eachWithIndex { LibraryQuestionTranslator it, i ->
                // get field name from translator
                def translationKey = it.getValue("excelExportFieldName")
                if (translationKey.toLowerCase().contains("translated")) {
                    // get translation value from translation (keyfile)
                    def translationValue = matchingTranslationFromExcelExport.get(translationKey)
                    // translate it if there is a match...leave alone if not
                    if (translationValue != "") {
                        tryToTranslateFactoryTextBlock = it.translate(tryToTranslateFactoryTextBlock, languageName, translationValue, translationFieldKeys)
                    }
                }
            }
            nextFactoryTextBlock = tryToTranslateFactoryTextBlock
        }
    }

}