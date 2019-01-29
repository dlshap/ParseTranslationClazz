import excelExports.ExcelExport
import excelExports.ExcelExports
import libraryquestions.LibraryArgs
import libraryquestions.LibraryFactory
import libraryquestions.LibraryFactoryManager
import libraryquestions.LibraryLogs
import libraryquestions.LibraryTextBlock
import libraryquestions.LibraryTranslator
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
            def nextExcelExport = excelExports.next()
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
            LibraryTextBlock nextLibraryTextBlock = libraryFactory.nextLibraryTextBlock()
            LibraryTranslator nextLibraryTranslator = new LibraryTranslator(nextLibraryTextBlock, translationsFromExcelExport)
            def nextTranslatedLibraryText = nextLibraryTranslator.getTranslatedText()
            libraryFactory.writeTextBlockToTranslatedFile(nextTranslatedLibraryText)
        }
    }
}