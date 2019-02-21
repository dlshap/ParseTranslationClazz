import excelExports.ExcelExport
import excelExports.ExcelExports
import libraryquestions.LibraryArgs
import libraryquestions.LibraryFactory
import libraryquestions.LibraryFactoryManager
import libraryquestions.LibraryLogs
import libraryquestions.LibraryPropertyFile
import libraryquestions.LibraryTranslator
import logging.Log
import org.apache.poi.ss.usermodel.Sheet
import translations.Translations
import useful.Messages

/**
 * Created by s0041664 on 8/14/2017.
 */
class UpdateDMTClassFactories {

    static final SPREADSHEET_PROMPT = "prompt.for.translation.spreadsheet.for"

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

    def getDefaultValuesIfArgsNull(LibraryArgs libraryArgs) {
        if (libraryArgs.startFilePath == null) libraryArgs.startFilePath = "C:\\Users\\s0041664\\Documents\\Projects\\DMT-DE\\Project Work\\translations\\"
        if (libraryArgs.languageName == null) libraryArgs.languageName = "Japanese"
    }

    def performTranslations(LibraryArgs libraryArgs) {
        LibraryLogs.openLogs(libraryArgs)
        def excelExports = new ExcelExports(libraryArgs)
        LibraryPropertyFile libraryPropertyFile = LibraryPropertyFile.openLibraryPropertyFileUsingChooser(
                Messages.getString(SPREADSHEET_PROMPT, "${libraryArgs.languageName}"), libraryArgs.startFilePath)
        if (libraryPropertyFile != null) {
            def libraryFactoryManager = new LibraryFactoryManager(libraryArgs)
            updateLibraryFactoriesFromEachExcelExport(libraryFactoryManager, excelExports)  // TODO: remove
//            updateLibraryFactoriesFromLibraryPropertyFile(libraryFactoryManager, libraryPropertyFile)
        }
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

    def updateLibraryFactoriesFromLibraryPropertyFile(LibraryFactoryManager libraryFactoryManager, LibraryPropertyFile libraryPropertyFile) {
        Log.writeLine("Processing ${libraryPropertyFile.getClassNameCount()} classes: ${libraryPropertyFile.getClassNameList()}")
        libraryPropertyFile.classNames.each { className ->
            addClassNameToLogs(className)
            updateLibraryFactoriesFromNextExcelSheet(libraryFactoryManager, libraryPropertyFile.getSheet(className))
        }
    }


    def updateLibraryFactoriesFromNextExcelExport(LibraryFactoryManager libraryFactoryManager, ExcelExport excelExport) {
        Translations translationsFromExcelExport = Translations.createTranslationsFromExcelExport(excelExport)
        LibraryFactory libraryFactoryForExcelExport = getCorrespondingLibraryFactoryForExcelExport(libraryFactoryManager, excelExport)
        updateLibraryFactoryFromTranslations(libraryFactoryForExcelExport, translationsFromExcelExport)
    }

    def updateLibraryFactoriesFromNextExcelSheet(LibraryFactoryManager libraryFactoryManager, Sheet sheet) {
        Translations translationsFromExcelExport = Translations.createLibraryTranslationsFromExcelSheet(sheet)
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

    def addClassNameToLogs(className) {
        Log.writeLine "\r\n$className:"
        Log.writeLine("exceptions", "\r\n$className:")
        Log.writeLine("nocode", "\r\n$className:")
    }

    def updateLibraryFactoryFromTranslations(LibraryFactory libraryFactory, Translations translationsFromExcelExport) {
        while (libraryFactory.hasNextLibraryTextBlock()) {
            def nextLibraryTextBlock = libraryFactory.nextLibraryTextBlock()
            LibraryTranslator nextLibraryTranslator = new LibraryTranslator(nextLibraryTextBlock, translationsFromExcelExport)
            def nextTranslatedLibraryText = nextLibraryTranslator.getTranslatedLibraryText()
            libraryFactory.writeTextBlockToTranslatedFile(nextTranslatedLibraryText)
        }
    }
}