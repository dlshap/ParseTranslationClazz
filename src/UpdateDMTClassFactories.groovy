import libraryquestions.LibraryArgs
import libraryquestions.LibraryFactory
import libraryquestions.LibraryFactoryManager
import libraryquestions.LibraryLogs
import libraryquestions.LibraryExcelPropertyFile
import libraryquestions.LibraryTextBlock
import libraryquestions.LibraryTranslator
import logging.Log
import properties.ExcelPropertySheet
import translations.Translations
import i18n.Messages

/**
 * Created by s0041664 on 8/14/2017.
 */
class UpdateDMTClassFactories {

    static final SPREADSHEET_PROMPT = "prompt.for.translation.spreadsheet.for"
//    static final LIBRARYHEADERROW = 4
    static final LIBRARYHEADERROW = 0

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

    static performTranslations(LibraryArgs libraryArgs) {
        LibraryLogs.openLogs(libraryArgs)
        def language = libraryArgs.languageName
        def spreadsheetPath = libraryArgs.spreadsheetPath
        LibraryExcelPropertyFile libraryPropertyFile = LibraryExcelPropertyFile.openLibraryPropertyFileUsingChooser(
                Messages.getString(SPREADSHEET_PROMPT, "Library Factory", "$language"), spreadsheetPath)
        if (libraryPropertyFile != null) {
            def libraryFactoryManager = new LibraryFactoryManager(libraryArgs, libraryPropertyFile)
            updateLibraryFactoriesFromLibraryPropertyFile(libraryFactoryManager, libraryPropertyFile)
        }
        LibraryLogs.closeLogs()
    }

    static updateLibraryFactoriesFromLibraryPropertyFile(LibraryFactoryManager libraryFactoryManager, LibraryExcelPropertyFile libraryPropertyFile) {
        Log.writeLine("Processing ${libraryFactoryManager.getClassNameCount()} classes: ${libraryFactoryManager.getClassNameList()}")
        libraryFactoryManager.classNames.each { className ->
            addClassNameToLogs(className)
            ExcelPropertySheet excelPropertySheet = libraryPropertyFile.getPropertySheetWithHeaderLabelsInHeaderRow(className, LIBRARYHEADERROW)
            updateLibraryFactoriesFromExcelSheet(libraryFactoryManager, excelPropertySheet)
        }
    }

    static updateLibraryFactoriesFromExcelSheet(LibraryFactoryManager libraryFactoryManager, ExcelPropertySheet excelPropertySheet) {
        Translations translationsFromExcelSheet = Translations.createLibraryTranslationsFromExcelSheet(excelPropertySheet)
        LibraryFactory libraryFactoryForExcelExport = getCorrespondingLibraryFactoryForExcelSheet(libraryFactoryManager, excelPropertySheet)
        updateLibraryFactoryFromTranslations(libraryFactoryForExcelExport, translationsFromExcelSheet)
    }

    static getCorrespondingLibraryFactoryForExcelSheet(LibraryFactoryManager libraryFactoryManager, ExcelPropertySheet excelPropertySheet) {
        def disclosureClassName = excelPropertySheet.sheetName
        def libraryFactory = libraryFactoryManager.getLibraryFactoryForFileName(disclosureClassName)
        libraryFactory
    }

    static addClassNameToLogs(className) {
        Log.writeLine "\r\n$className:"
        Log.writeLine("exceptions", "\r\n$className:")
        Log.writeLine("nocode", "\r\n$className:")
    }

    static updateLibraryFactoryFromTranslations(LibraryFactory libraryFactory, Translations translationsFromExcelSheet) {
        while (libraryFactory.hasNextLibraryTextBlock()) {
            LibraryTextBlock nextLibraryTextBlock = libraryFactory.nextLibraryTextBlock()
            LibraryTranslator nextLibraryTranslator = new LibraryTranslator(nextLibraryTextBlock, translationsFromExcelSheet)
            String nextTranslatedLibraryText = nextLibraryTranslator.getTranslatedLibraryText()
            libraryFactory.writeTextBlockToTranslatedFile(nextTranslatedLibraryText)
        }
    }
}