import i18n.LanguageLabels
import libraryquestions.GenerateLibrarySpreadsheet
import libraryquestions.LibraryArgs
import logging.LogUtils

class GenerateLibrarySpreadsheets {

    LibraryArgs libraryArgs

    static main(args) {
        new GenerateLibrarySpreadsheets(args)
    }

    GenerateLibrarySpreadsheets(args) {
        start(args)
    }

    def start(args) {
        libraryArgs = new LibraryArgs(args)
        LogUtils.startLogging(this.getClass().name, libraryArgs.basePath)
        if (libraryArgs.language.toLowerCase() == "all") {
            runForAllLanguages()
        } else {
            runForOneLanguage()
        }
    }

    private def runForAllLanguages() {
        ArrayList<String> languageList = LanguageLabels.getLanguageList()
        languageList.each {
            if (it != "English") {
                libraryArgs.language = it
                new GenerateLibrarySpreadsheet(libraryArgs)
            }
        }
    }

    private def runForOneLanguage() {
        new GenerateLibrarySpreadsheet(libraryArgs)
    }
}
