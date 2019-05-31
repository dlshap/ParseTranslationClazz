package main

import i18n.LanguageLabels
import libraryquestions.LibraryArgs
import libraryquestions.LibraryFactory
import libraryquestions.LibraryFactoryManager
import libraryquestions.LibraryLogs
import libraryquestions.LibraryExcelPropertyFile
import libraryquestions.LibraryTextBlock
import libraryquestions.LibraryTranslator
import logging.Log
import logging.LogUtils
import properties.ExcelPropertySheet
import translations.Translations
import i18n.Messages

/**
 * Created by s0041664 on 8/14/2017.
 */
class UpdateDMTClassFactories {

    static final SPREADSHEET_PROMPT = "prompt.for.translation.spreadsheet.for"
    static final LIBRARYHEADERROW = 0

    LibraryArgs libraryArgs

    static main(args) {
        new UpdateDMTClassFactories(args)
    }

    UpdateDMTClassFactories(args) {
        start(args)
    }

    def start(args) {
        libraryArgs = new LibraryArgs(args)
        LogUtils.startLogging(this.getClass().name, libraryArgs.basePath)
        if (LanguageLabels.isLanguageInList(this.libraryArgs.language)) {
            performTranslations()
        }
    }

    private def performTranslations() {
        LibraryLogs.openLogs(libraryArgs)
        def language = libraryArgs.language
        def spreadsheetPath = libraryArgs.spreadsheetPath
        LibraryExcelPropertyFile libraryPropertyFile = LibraryExcelPropertyFile.openLibraryPropertyFileUsingChooser(
                Messages.getString(SPREADSHEET_PROMPT, "Library Factory", "$language"), spreadsheetPath)
        if (libraryPropertyFile != null) {
            def libraryFactoryManager = new LibraryFactoryManager(libraryArgs, libraryPropertyFile)
            updateLibraryFactoriesFromLibraryPropertyFile(libraryFactoryManager, libraryPropertyFile)
        }
        LibraryLogs.closeLogs()
    }

    private def updateLibraryFactoriesFromLibraryPropertyFile(LibraryFactoryManager libraryFactoryManager, LibraryExcelPropertyFile libraryPropertyFile) {
        Log.writeLine("Processing ${libraryFactoryManager.getClassNameCount()} classes: ${libraryFactoryManager.getClassNameList()}")
        libraryFactoryManager.classNames.each { className ->
            addClassNameToLogs(className)
            ExcelPropertySheet excelPropertySheet = libraryPropertyFile.getPropertySheetWithHeaderLabelsInHeaderRow(className, LIBRARYHEADERROW)
            updateLibraryFactoriesFromExcelSheet(libraryFactoryManager, excelPropertySheet)
        }
    }

    private def updateLibraryFactoriesFromExcelSheet(LibraryFactoryManager libraryFactoryManager, ExcelPropertySheet excelPropertySheet) {
        Translations translationsFromExcelSheet = Translations.createLibraryTranslationsFromExcelSheet(excelPropertySheet)
        LibraryFactory libraryFactoryForExcelExport = getCorrespondingLibraryFactoryForExcelSheet(libraryFactoryManager, excelPropertySheet)
        updateLibraryFactoryFromTranslations(libraryFactoryForExcelExport, translationsFromExcelSheet)
    }

    private def getCorrespondingLibraryFactoryForExcelSheet(LibraryFactoryManager libraryFactoryManager, ExcelPropertySheet excelPropertySheet) {
        def disclosureClassName = excelPropertySheet.sheetName
        def libraryFactory = libraryFactoryManager.getLibraryFactoryForFileName(disclosureClassName)
        libraryFactory
    }

    private def addClassNameToLogs(className) {
        Log.writeLine "\r\n$className:"
        Log.writeLine("exceptions", "\r\n$className:")
        Log.writeLine("nocode", "\r\n$className:")
    }

    private def updateLibraryFactoryFromTranslations(LibraryFactory libraryFactory, Translations translationsFromExcelSheet) {
        while (libraryFactory.hasNextLibraryTextBlock()) {
            LibraryTextBlock nextLibraryTextBlock = libraryFactory.nextLibraryTextBlock()
            LibraryTranslator nextLibraryTranslator = new LibraryTranslator(nextLibraryTextBlock, translationsFromExcelSheet)
            String nextTranslatedLibraryText = nextLibraryTranslator.getTranslatedLibraryText()
            libraryFactory.writeTextBlockToTranslatedFile(nextTranslatedLibraryText)
        }
    }
}