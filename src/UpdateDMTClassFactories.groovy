import com.sun.jna.Library
import excelExports.ExcelExport
import excelExports.ExcelExports
import libraryquestions.LibraryArgs
import libraryquestions.LibraryFactory
import libraryquestions.LibraryFactoryManager
import libraryquestions.LibraryTextBlock
import translations.Translation
import translations.TranslationFieldKeys
import libraryquestions.LibraryQuestionFieldFinder
import logging.Dates
import logging.Log
import translations.Translations

/**
 * Created by s0041664 on 8/14/2017.
 */
class UpdateDMTClassFactories {

    static startFilePath
    static languageName
    static fileNameForTestingSingleFile

//    static excelExportFileList = []

//    static libraryFactoryParser
//    static libraryFactoryWithNewTranslations
//    static nextFactoryTextBlock

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
        def libraryArgs = buildLibraryArgsFromCommandLineArgs(args)
        performTranslations(libraryArgs)
    }

    def buildLibraryArgsFromCommandLineArgs(args) {
        def libraryArgs = new LibraryArgs(args)
        getDefaultValuesIfArgsNull(libraryArgs)
        libraryArgs
    }

    def getDefaultValuesIfArgsNull(libraryArgs) {
        if (libraryArgs.startFilePath == null) libraryArgs.startFilePath = "C:\\\\Users\\\\s0041664\\\\Documents\\\\Projects\\\\DMT-DE\\\\Project Work\\\\translations\\\\DMT\\\\"
        if (libraryArgs.languageName == null) libraryArgs.languageName = "Japanese"
    }

    def performTranslations(libraryArgs) {
        openLogs(libraryArgs)
        def excelExports = buildExcelExports(libraryArgs)
        def libraryFactoryManager = buildLibraryFactoryManager(libraryArgs)
        updateLibraryFactoriesFromExcelExports(libraryFactoryManager, excelExports)
        closeLogs()
    }

    def openLogs(libraryArgs) {
        def logsFilePath = libraryArgs.startFilePath + "logs\\\\"
        Log.open logsFilePath + "log-library-translations.txt"
        Log.writeLine "Running on " + Dates.currentDateAndTime() + ":\r\n"
        Log.open "exceptions", logsFilePath + "log-library-exceptions.txt"
        Log.writeLine "exceptions", "Running on " + Dates.currentDateAndTime() + ":\r\n"
        Log.open "nocode", logsFilePath + "log-library-nocode.txt"
        Log.writeLine "nocode", "Running on " + Dates.currentDateAndTime() + ":\r\n"
    }

    def buildExcelExports(libraryArgs) {
        def excelExports = new ExcelExports(libraryArgs)
        excelExports
    }

    def buildLibraryFactoryManager(libraryArgs) {
        def libraryFactoryManager = new LibraryFactoryManager(libraryArgs)
        libraryFactoryManager
    }

    def updateLibraryFactoriesFromExcelExports(LibraryFactoryManager libraryFactoryManager, ExcelExports excelExports) {
        Log.writeLine("Processing ${excelExports.fileCount()} files: ${excelExports.getFileNameList()}")
        while (excelExports.hasNext()) {
            def nextExcelExport = excelExports.next()
            def nextLibraryFactory = getLibraryFactoryForExcelExport(libraryFactoryManager, nextExcelExport)
            updateLibraryFactoryFromExcelExport(nextLibraryFactory, nextExcelExport)
        }
    }

    def getLibraryFactoryForExcelExport(LibraryFactoryManager libraryFactoryManager, ExcelExport excelExport) {
        def classFileName = excelExport.getShortName()
        def libraryFactory = libraryFactoryManager.getLibraryFactoryForFileName(classFileName)
        libraryFactory
    }

    def updateLibraryFactoryFromExcelExport(LibraryFactory libraryFactory, ExcelExport excelExport) {
        def classFileName = excelExport.getShortName()
        addFilenameToLogs(classFileName)
        moveExcelTranslationsToLibraryFactory(excelExport, libraryFactory)
    }

    static addFilenameToLogs(classFileName) {
        Log.writeLine "\r\n$classFileName:"
        Log.writeLine("exceptions", "\r\n$classFileName:")
        Log.writeLine("nocode", "\r\n$classFileName:")
    }

    def moveExcelTranslationsToLibraryFactory(ExcelExport excelExport, LibraryFactory libraryFactory) {
        while (libraryFactory.hasNextLibraryTextBlock()) {
            def nextLibraryTextBlock = libraryFactory.nextLibraryTextBlock()
            def nextTranslatedFactoryTextBlock = getExcelTranslationsForNextLibraryTextBlock(excelExport, nextLibraryTextBlock)
            libraryFactory.writeTextBlockToTranslatedFile(nextTranslatedFactoryTextBlock)
        }
    }

    def getExcelTranslationsForNextLibraryTextBlock(ExcelExport excelExport, LibraryTextBlock libraryTextBlock) {
        TranslationFieldKeys factoryTranslationKeys = getKeysFromLibraryTextBlock(libraryTextBlock)
        Translation translation = getTranslationForKeys(excelExport, factoryTranslationKeys)
        String translatedFactoryTextBlock = applyTranslationToFactoryTextBlock(translation, libraryTextBlock)
        translatedFactoryTextBlock
    }

    def getKeysFromLibraryTextBlock(LibraryTextBlock libraryTextBlock) {
//        def bomFieldName = findBomFieldNameInLibraryFactory(libraryTextBlock)
//        def questionIdentifier = findQuestionIdentifierInText(libraryTextBlock)
        def bomFieldName = findFieldInLibraryText("BOM Fields", libraryTextBlock)
        def questionIdentifier = findFieldInLibraryText("Question Identifier", libraryTextBlock)
        if ((bomFieldName != null) || (questionIdentifier != null))
            translationFieldKeys = new TranslationFieldKeys(["BOM Fields": bomFieldName, "Question Identifier": questionIdentifier])
        else
            translationFieldKeys = null
        translationFieldKeys
    }

    def findFieldInLibraryText(String fieldName, LibraryTextBlock libraryTextBlock) {
        def fieldValue = null
        if (libraryTextBlock.lineContains(fieldName)) {
            fieldValue = libraryTextBlock.findFieldInLibraryText(fieldName)
        }
        fieldValue

    }

    def findBomFieldNameInLibraryFactory(LibraryTextBlock libraryTextBlock) {
        def bomFieldName = null
        if (libraryTextBlock.lineContains("BOM Fields")) {
            bomFieldName = libraryTextBlock.findFieldInLibraryText("BOM Fields")
        }
        bomFieldName
    }

    def findQuestionIdentifierInText(LibraryTextBlock libraryTextBlock) {
        def questionIdentifier = null
        if (libraryTextBlock.lineContains("Question Identifier")) {
            questionIdentifier = libraryTextBlock.findFieldInLibraryText("Question Identifier")
        }
        questionIdentifier
    }


    def getTranslationForKeys(nextExcelExport, factoryTranslationKeys) {

    }

    def applyTranslationToFactoryTextBlock(translation, nextFactoryTextBlock) {

    }


    def closeLogs() {
        Log.writeLine("\r\nDone at: " + Dates.currentDateAndTime())
        Log.writeLine("exceptions", "\r\nDone at: " + Dates.currentDateAndTime())
        Log.writeLine("nocode", "\r\nDone at: " + Dates.currentDateAndTime())
    }

/** ***********************************************************************************************************/


    def translateFiles(libraryArgs) {
        doTranslations()
        cleanupAfterTranslations()
    }

//    static cleanupAfterTranslations() {
//        closeLogs()
//    }

    static doTranslations() {
    }

//    static translateNextFileInFileList(classFileName) {
//        loadTranslationsFromExcelExport(classFileName)
//        loadLibraryFactoryParserFromLibraryFactoryFile(classFileName)
//        createLibraryFactoryForUpdatedTranslations(classFileName)
//        updateLibraryFactoryFromExcelTranslations()
//    }

//    static loadTranslationsFromExcelExport(classFileName) {
//        def translationsFromExcelExportFileName = classFileName + ".txt"
//        def translationsFromExcelExportPath = startFilePath + "LibraryExports\\\\"
//        def translationsFromExcelExportFile = new KeyFile(translationsFromExcelExportPath + translationsFromExcelExportFileName)
//        if (translationsFromExcelExportFile.exists()) {
//            translationsFromExcelExport = new Translations(translationsFromExcelExportFile)
//        } else {
//            Log.writeLine("exceptions", "Excel Export file: ${classFileName}.txt doesn't exist.")
//        }
//    }

//    static getNextFactoryTextBlock() {
//        nextFactoryTextBlock = libraryFactoryParser.next()
//    }

//    static getTranslationForKeys() {
//        /*
//        get translations for BOM Fields key...if more than one, get translations for question identifier and BOM Fields key (or null if no match)
//         */
//        matchingTranslationFromExcelExport = null
//        if (translationFieldKeys != null) {
//            def matchingTranslations = translationsFromExcelExport.getTranslations(translationFieldKeys)
//            if (singleMatchingTranslation(matchingTranslations)) {
//                matchingTranslationFromExcelExport = matchingTranslations[0]
//            }
//        }
//    }
//
//    static singleMatchingTranslation(matchingTranslations) {
//        def translationCount = matchingTranslations.size()
//        if (translationCount == 1)
//            return true
//        else if (translationCount == 0) {
//            Log.writeLine "exceptions", "Missing translation for keys: ${translationFieldKeys.getKeyList()}"
//            return false
//        } else if (translationCount > 1) {
//            Log.writeLine "exceptions", "Multiple translations for keys: ${translationFieldKeys.getKeyList()}"
//            return false
//        }
//
//    }
//
//    static WriteTranslatedFactoryTextBlockToTranslatedFile() {
//        libraryFactoryWithNewTranslations.add(nextFactoryTextBlock)
//    }

//    static loadLibraryFactoryParserFromLibraryFactoryFile(fileName) {
//        def libraryFactoryFileName = fileName + "ClassFactory.groovy"
//        def libraryFactoryFile = new TextFile(startFilePath + "LibraryFactoryManager\\\\" + libraryFactoryFileName)
//        if (libraryFactoryFile.exists()) {
//            libraryFactoryParser = new LibraryFactoryParser(libraryFactoryFile)
//            libraryQuestionFieldFinder = new LibraryQuestionFieldFinder(languageName)
//        } else {
//            Log.writeLine "exceptions", "${libraryFactoryFile.getFullPathName()} doesn't exist"
//        }
//    }
//
//    static replaceLineWithTranslations() {
//        if ((translationFieldKeys != null) && (matchingTranslationFromExcelExport != null)) {
//            def tryToTranslateFactoryTextBlock = nextFactoryTextBlock
//            def libraryQuestionTranslators = libraryQuestionFieldFinder.getLibraryQuestionTranslators()
//            libraryQuestionTranslators.eachWithIndex { LibraryQuestionTranslator it, i ->
//                // get field name from translator
//                def translationKey = it.getValue("excelExportFieldName")
//                if (translationKey.toLowerCase().contains("translated")) {
//                    // get translation value from translation (keyfile)
//                    def translationValue = matchingTranslationFromExcelExport.get(translationKey)
//                    // translate it if there is a match...leave alone if not
//                    if (translationValue != "") {
//                        tryToTranslateFactoryTextBlock = it.translate(tryToTranslateFactoryTextBlock, languageName, translationValue, translationFieldKeys)
//                    }
//                }
//            }
//            nextFactoryTextBlock = tryToTranslateFactoryTextBlock
//        }
//    }

}