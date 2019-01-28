import excelExports.ExcelExport
import excelExports.ExcelExports
import libraryquestions.LibraryArgs
import libraryquestions.LibraryFactory
import libraryquestions.LibraryFactoryManager
import libraryquestions.LibraryLogs
import libraryquestions.LibraryTextBlock
import translations.Translation
import translations.TranslationFieldKeys
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
        LibraryLogs.openLogs(libraryArgs)
        def excelExports = new ExcelExports(libraryArgs)
        def libraryFactoryManager = new LibraryFactoryManager(libraryArgs)
        updateLibraryFactoriesFromEachExcelExport(libraryFactoryManager, excelExports)
        LibraryLogs.closeLogs()
    }


    def updateLibraryFactoriesFromEachExcelExport(LibraryFactoryManager libraryFactoryManager, ExcelExports excelExports) {
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
        def disclosureClassName = excelExport.getShortName()
        def libraryFactory = libraryFactoryManager.getLibraryFactoryForFileName(disclosureClassName)
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
            def nextTranslatedLibraryText = getTranslatedTextForNextLibraryTextBlockUsingExcelTranslations(nextLibraryTextBlock, translationsFromExcelExport)
            libraryFactory.writeTextBlockToTranslatedFile(nextTranslatedLibraryText)
        }
    }

    def getTranslatedTextForNextLibraryTextBlockUsingExcelTranslations(LibraryTextBlock libraryTextBlock, Translations translationsFromExcelExport) {
        TranslationFieldKeys factoryTranslationKeys = getKeyValuesFromLibraryTextBlock(libraryTextBlock)
        Translation translation = getTranslationForKeys(translationsFromExcelExport, factoryTranslationKeys)
        String translatedLibraryText = applyTranslationToLibraryTextBlock(translation, libraryTextBlock)
        translatedLibraryText
    }

    def getKeyValuesFromLibraryTextBlock(LibraryTextBlock libraryTextBlock) {
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
        Translation matchingTranslationFromExcelExport = null
        if (translationFieldKeys != null) {
            def matchingTranslations = translationsFromExcelExport.getTranslationsFromKeyFields(translationFieldKeys)
            if (isSingleMatchingTranslationForKeys(matchingTranslations, translationFieldKeys)) {
                matchingTranslationFromExcelExport = matchingTranslations[0]
                matchingTranslationFromExcelExport.translationFieldKeys = translationFieldKeys  // used for missing value messages
            }
        }
        matchingTranslationFromExcelExport
    }

    def isSingleMatchingTranslationForKeys(matchingTranslations, TranslationFieldKeys translationFieldKeys) {
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
        if (translation != null)
            translatedLibraryText = libraryTextBlock.translateAllFieldsFromTranslation(translation)
        translatedLibraryText
    }

}