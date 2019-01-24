import excelExports.ExcelExport
import excelExports.ExcelExports
import libraryquestions.LibraryArgs
import libraryquestions.LibraryFactory
import libraryquestions.LibraryFactoryManager
import libraryquestions.LibraryTextBlock
import translations.Translation
import translations.TranslationFieldKeys
import logging.Dates
import logging.Log
import translations.Translations

/**
 * Created by s0041664 on 8/14/2017.
 */
class UpdateDMTClassFactories {

    UpdateDMTClassFactories(args) {
        start(args)
    }

    static main(args) {
        new UpdateDMTClassFactories(args)
    }

    def start(args) {
        def libraryArgs = new LibraryArgs(args)
        performTranslations(libraryArgs)
    }

    def performTranslations(libraryArgs) {
        openLogs(libraryArgs)
        def excelExports = new ExcelExports(libraryArgs)
        def libraryFactoryManager = new LibraryFactoryManager(libraryArgs)
        updateLibraryFactoriesFromEachExcelExports(libraryFactoryManager, excelExports)
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

    def updateLibraryFactoriesFromEachExcelExports(LibraryFactoryManager libraryFactoryManager, ExcelExports excelExports) {
        Log.writeLine("Processing ${excelExports.fileCount()} files: ${excelExports.getFileNameList()}")
        while (excelExports.hasNext()) {
            ExcelExport nextExcelExport = excelExports.next()
            addFileNameToLog(nextExcelExport)
            updateLibraryFactoriesFromNextExcelExport(libraryFactoryManager, nextExcelExport)
        }
    }

    def updateLibraryFactoriesFromNextExcelExport(LibraryFactoryManager libraryFactoryManager, ExcelExport excelExport) {
        Translations translationsFromExcelExport = Translations.createTranslationsFromExcelExport(excelExport)
        LibraryFactory libraryFactoryForExcelExport = getCorrespondingLibraryFactoryForExcelExport(libraryFactoryManager, excelExport)
        updateLibraryFactoryFromTranslations(libraryFactoryForExcelExport, translationsFromExcelExport)
    }


    def getCorrespondingLibraryFactoryForExcelExport(LibraryFactoryManager libraryFactoryManager, ExcelExport excelExport) {
        def classFileName = excelExport.getShortName()
        def libraryFactory = libraryFactoryManager.getLibraryFactoryForFileName(classFileName)
        libraryFactory
    }

    def addFileNameToLog(ExcelExport excelExport) {
        def classFileName = excelExport.getShortName()
        Log.writeLine "\r\n$classFileName:"
        Log.writeLine("exceptions", "\r\n$classFileName:")
        Log.writeLine("nocode", "\r\n$classFileName:")
    }

    def updateLibraryFactoryFromTranslations(LibraryFactory libraryFactory, Translations translationsFromExcelExport) {
        while (libraryFactory.hasNextLibraryTextBlock()) {
            def nextLibraryTextBlock = libraryFactory.nextLibraryTextBlock()
            def nextTranslatedLibraryText = getTranslatedTextForNextLibraryTextBlockFromExcelTranslations(nextLibraryTextBlock, translationsFromExcelExport)
            libraryFactory.writeTextBlockToTranslatedFile(nextTranslatedLibraryText)
        }
    }

    def getTranslatedTextForNextLibraryTextBlockFromExcelTranslations(LibraryTextBlock libraryTextBlock, Translations translationsFromExcelExport) {
        TranslationFieldKeys factoryTranslationKeys = getKeysFromLibraryTextBlock(libraryTextBlock)
        Translation translation = getTranslationForKeys(translationsFromExcelExport, factoryTranslationKeys)
        String translatedLibraryText = applyTranslationToLibraryTextBlock(translation, libraryTextBlock)
        translatedLibraryText
    }

    def getKeysFromLibraryTextBlock(LibraryTextBlock libraryTextBlock) {
        def bomFieldName = libraryTextBlock.findFieldInLibraryText("BOM Fields")
        def questionIdentifier = libraryTextBlock.findFieldInLibraryText("Question Identifier")
        def translationFieldKeys = null
        if ((bomFieldName != null) || (questionIdentifier != null))
            translationFieldKeys = new TranslationFieldKeys(["BOM Fields": bomFieldName, "Question Identifier": questionIdentifier])
        translationFieldKeys
    }

    def getTranslationForKeys(Translations translationsFromExcelExport, TranslationFieldKeys translationFieldKeys) {
        /*
      get translations for BOM Fields key...if more than one, get translations for question identifier and BOM Fields key (or null if no match)
       */
        def matchingTranslationFromExcelExport = null
        if (translationFieldKeys != null) {
            def matchingTranslations = translationsFromExcelExport.getTranslations(translationFieldKeys)
            if (singleMatchingTranslation(matchingTranslations)) {
                matchingTranslationFromExcelExport = matchingTranslations[0]
            }
        }
        matchingTranslationFromExcelExport
    }

    def singleMatchingTranslation(matchingTranslations) {
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

    def applyTranslationToLibraryTextBlock(Translation translation, LibraryTextBlock libraryTextBlock) {
        String translatedLibraryText = libraryTextBlock.translateAllFieldsFromTranslation(translation)
        translatedLibraryText
    }

//  static replaceLineWithTranslations() {
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


    def closeLogs() {
        Log.writeLine("\r\nDone at: " + Dates.currentDateAndTime())
        Log.writeLine("exceptions", "\r\nDone at: " + Dates.currentDateAndTime())
        Log.writeLine("nocode", "\r\nDone at: " + Dates.currentDateAndTime())
    }

/** ***********************************************************************************************************/


//    def translateFiles(libraryArgs) {
//        doTranslations()
//        cleanupAfterTranslations()
//    }

//    static cleanupAfterTranslations() {
//        closeLogs()
//    }

//    static doTranslations() {
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

}