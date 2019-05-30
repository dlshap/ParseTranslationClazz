import i18n.LanguageLabels
import logging.Log
import logging.LogUtils
import properties.GeneratePropertySpreadsheet
import properties.PropertyArgs

class GeneratePropertySpreadsheets {

    PropertyArgs propertyArgs

    static main(args) {
        new GeneratePropertySpreadsheets(args)
    }

    GeneratePropertySpreadsheets(args) {
        start(args)
    }

    def start(args) {
        propertyArgs = new PropertyArgs(args)
        LogUtils.startLogging(this.getClass().name, propertyArgs.basePath)
        if (propertyArgs.language.toLowerCase() == "all") {
            runForAllLanguages()
        } else {
            runForOneLanguage()
        }
    }

    private def runForAllLanguages() {
        Log.writeLine"app", "Running for all languages..."
        ArrayList<String> languageList = LanguageLabels.getLanguageList()
        languageList.each {
            if (it != "English") {
                propertyArgs.language = it
                Log.writeLine "app", "Running for language: ${propertyArgs.language}..."
                new GeneratePropertySpreadsheet(propertyArgs)
            }
        }
    }

    private def runForOneLanguage() {
        Log.writeLine "app", "Running for language: ${propertyArgs.language}..."
        new GeneratePropertySpreadsheet(propertyArgs)
    }
}
