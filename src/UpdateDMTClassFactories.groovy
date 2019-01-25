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
      get translations for multiple keys...if not exactly one match, return null
       */
        def matchingTranslationFromExcelExport = null
        if (translationFieldKeys != null) {
            def matchingTranslations = translationsFromExcelExport.getTranslationsFromKeyFields(translationFieldKeys)
            if (singleMatchingTranslationForKeys(matchingTranslations, translationFieldKeys)) {
                matchingTranslationFromExcelExport = new Translation(matchingTranslations[0], translationFieldKeys)
            }
        }
        matchingTranslationFromExcelExport
    }

    def singleMatchingTranslationForKeys(matchingTranslations, TranslationFieldKeys translationFieldKeys) {
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
        String translatedLibraryText = libraryTextBlock.textBlock
        if (!(translation == null))
            translatedLibraryText = libraryTextBlock.translateAllFieldsFromTranslation(translation)
        translatedLibraryText
    }

    def closeLogs() {
        Log.writeLine("\r\nDone at: " + Dates.currentDateAndTime())
        Log.writeLine("exceptions", "\r\nDone at: " + Dates.currentDateAndTime())
        Log.writeLine("nocode", "\r\nDone at: " + Dates.currentDateAndTime())
    }
}