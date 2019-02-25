import libraryquestions.LibraryArgs
import libraryquestions.LibraryFactory
import libraryquestions.LibraryFactoryManager
import libraryquestions.LibraryLogs
import libraryquestions.LibraryPropertyFile
import libraryquestions.LibraryTextBlock
import libraryquestions.LibraryTranslator
import logging.Log
import properties.ExcelPropertySheet
import translations.Translations
import useful.Messages

/**
 * Created by s0041664 on 8/14/2017.
 */
class UpdateDMTClassFactories {

    static final SPREADSHEET_PROMPT = "prompt.for.translation.spreadsheet.for"
    static final LIBRARYHEADERROW = 4

    UpdateDMTClassFactories(args) {
        start(args)
    }

    static main(args) {
        new UpdateDMTClassFactories(args)
    }

    def start(args) {
        def libraryArgs = new LibraryArgs(args)
        getDefaultValuesIfArgsNull(libraryArgs)
        performTranslations(libraryArgs)
    }

    static getDefaultValuesIfArgsNull(LibraryArgs libraryArgs) {
        if (libraryArgs.startFilePath == null) libraryArgs.startFilePath = "C:\\Users\\s0041664\\Documents\\Projects\\DMT-DE\\Project Work\\translations\\"
        if (libraryArgs.languageName == null) libraryArgs.languageName = "Japanese"
    }

    def performTranslations(LibraryArgs libraryArgs) {
        LibraryLogs.openLogs(libraryArgs)
        LibraryPropertyFile libraryPropertyFile = LibraryPropertyFile.openLibraryPropertyFileUsingChooser(
                Messages.getString(SPREADSHEET_PROMPT, "${libraryArgs.languageName}"), libraryArgs)
        if (libraryPropertyFile != null) {
            def libraryFactoryManager = new LibraryFactoryManager(libraryArgs)
            updateLibraryFactoriesFromLibraryPropertyFile(libraryFactoryManager, libraryPropertyFile)
        }
        LibraryLogs.closeLogs()
    }

    static updateLibraryFactoriesFromLibraryPropertyFile(LibraryFactoryManager libraryFactoryManager, LibraryPropertyFile libraryPropertyFile) {
        Log.writeLine("Processing ${libraryPropertyFile.getClassNameCount()} classes: ${libraryPropertyFile.getClassNameList()}")
        libraryPropertyFile.classNames.each { className ->
            addClassNameToLogs(className)
            ExcelPropertySheet excelPropertySheet = libraryPropertyFile.getPropertySheetWithHeaderLabelsInHeaderRowNum(className, LIBRARYHEADERROW)
            updateLibraryFactoriesFromNextExcelSheet(libraryFactoryManager, excelPropertySheet)
        }
    }

    static updateLibraryFactoriesFromNextExcelSheet(LibraryFactoryManager libraryFactoryManager, ExcelPropertySheet excelPropertySheet) {
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
            def nextLibraryTextBlock = libraryFactory.nextLibraryTextBlock()
            LibraryTranslator nextLibraryTranslator = new LibraryTranslator(nextLibraryTextBlock, translationsFromExcelSheet)
            def nextTranslatedLibraryText = nextLibraryTranslator.getTranslatedLibraryText()
            libraryFactory.writeTextBlockToTranslatedFile(nextTranslatedLibraryText)
        }
    }
}