import excelfilemanagement.ExcelPropertyFile
import properties.PropertyFile
import excelExports.PropertiesExcelExportFile
import logging.Dates
import logging.Log
import translations.IgnorePropertyList
import translations.Properties
import translations.Translations
import translations.Translation
import useful.ArgsParser
import useful.Messages

/**
 * Created by s0041664 on 8/25/2017.
 */
class UpdateDmtDeTranslatedProperties {

    def startFilePath        // "root" filepath
    def languageName         // language for this translation

    static final SPREADSHEET_PROMPT = "prompt.for.translation.spreadsheet.for.component"


    def componentList = ["DMT", "DE"]       // list for looping through components


    def PropertiesExcelExportFile translationsExcelExportFile
    def ExcelPropertyFile translationsExcelFile
    def Translations translationsFromExcelExport
    def PropertyFile propertyFile
    def Properties propertiesFromPropertyFile
    def IgnorePropertyList ignorePropertyList

    static main(args) {
        new UpdateDmtDeTranslatedProperties(args)
    }

    UpdateDmtDeTranslatedProperties(args) {
        start(args)
    }

    def start(args) {
        buildArgsAndParameters(args)
        moveTranslationsFromExcelExportToPropertiesFiles()
    }

    def buildArgsAndParameters(args) {
        getArgValues(args)
        getDefaultValuesIfArgsNull()
    }

    def getArgValues(args) {
        def argsMap = new ArgsParser(args)
        languageName = argsMap.get("language")
        startFilePath = argsMap.get("path")
    }

    def getDefaultValuesIfArgsNull() {
        if (startFilePath == null) startFilePath = "C:\\\\Users\\\\s0041664\\\\Documents\\\\Projects\\\\DMT-DE\\\\Project Work\\\\translations\\\\"
        if (languageName == null) languageName = "Japanese"
    }

    def moveTranslationsFromExcelExportToPropertiesFiles() {
        componentList.each { componentName ->
            doTranslationsForComponent(componentName)
        }
    }

    def doTranslationsForComponent(componentName) {
        def componentFilePath = buildFilePathForComponent(componentName)
        buildIgnorePropertyListForComponentFromIgnoreFile(componentName, componentFilePath)
        openTranslationLogsForComponent(componentName, componentFilePath)
        moveTranslationsToPropertiesForComponent(componentName, componentFilePath)
    }

    def buildFilePathForComponent(componentName) {
        startFilePath + componentName + "\\\\"
    }

    def buildIgnorePropertyListForComponentFromIgnoreFile(componentName, componentFilePath) {
        ignorePropertyList = new IgnorePropertyList(componentFilePath + "logs\\\\ignoreMessages.txt")
    }

    def openTranslationLogsForComponent(componentName, componentFilePath) {
        def logsFilePath = componentFilePath + "logs\\\\"
        //default log: property translations (successful) log
        Log.open(logsFilePath + "$componentName log-property-translations.txt")
        Log.writeLine "Running on " + Dates.currentDateAndTime() + ":\r\n"
        //exceptions log: property exceptions log
        Log.open("exceptions", logsFilePath + "$componentName log-property-exceptions.txt")
        Log.writeLine "exceptions", "Running on " + Dates.currentDateAndTime() + ":\r\n"
    }

    def moveTranslationsToPropertiesForComponent(componentName, componentFilePath) {
        if (buildPropertiesAndTranslationsObjectsForComponent(componentName, componentFilePath)) {
            copyTranslations()
            logMissingTranslations()
        }
    }

    def buildPropertiesAndTranslationsObjectsForComponent(componentName, componentFilePath) {
        // first build translations object...if that's ok
        if (buildTranslationsObject(componentName))
            buildPropertiesObject(componentName, componentFilePath)
    }

    def buildTranslationsObject(componentName) {
//        if (openTranslationsExcelExportFile())
//            buildTranslationsFromExport()

        translationsExcelFile = openTranslationsExcelFile(componentName)
        if (!(translationsExcelFile.isNull()))
            buildTranslationsFromExcelFile(componentName)
    }

    def openTranslationsExcelExportFile() {
        // open property file
        translationsExcelExportFile = new PropertiesExcelExportFile(componentName, componentFilePath)
        (translationsExcelExportFile.file != null)       // return true if there is a file (open was successful)
    }

    def buildTranslationsFromExport() {
        translationsFromExcelExport = new Translations(translationsExcelExportFile)
    }

    def openTranslationsExcelFile(componentName) {
        def excelFilePath = startFilePath + "Spreadsheets\\PropertySpreadsheets\\DMTDE"
        def chooserPrompt = Messages.getString(SPREADSHEET_PROMPT, componentName)
        ExcelPropertyFile.getPropertiesExcelFileUsingChooser(excelFilePath, chooserPrompt, componentName)
    }

    def buildTranslationsFromExcelFile(componentName) {
        translationsFromExcelExport = Translations.createTranslationsFromExcelPropertiesFile(translationsExcelFile, componentName)
    }

    def buildPropertiesObject(componentName, componentFilePath) {
        if (openPropertyFile(componentName, componentFilePath))
            buildPropertiesFromPropertyFile()
    }

    def openPropertyFile(componentName, componentFilePath) {
        propertyFile = new PropertyFile(componentName, componentFilePath)
        (propertyFile.file != null)                      // return true if there is a file (open was successful)
    }

    def buildPropertiesFromPropertyFile() {
        //get property list
        propertiesFromPropertyFile = new Properties(propertyFile)
    }

    def copyTranslations() {
        updatePropertiesFromTranslations()
        propertiesFromPropertyFile.writePropertiesToTranslatedOutputFile()
    }

    def updatePropertiesFromTranslations() {
        // loop through property value translations from Excel export
        while (translationsFromExcelExport.hasNext()) {
            updateAPropertyFromAnExcelExportRow()
        }
    }

    def updateAPropertyFromAnExcelExportRow() {
        def nextExcelExportTranslation = translationsFromExcelExport.next()
        def nextTranslationKey = nextExcelExportTranslation.get("Message Key")
        def nextTranslationValue = nextExcelExportTranslation.get(languageName)
        if ((nextTranslationValue != null)  && (!(nextTranslationKey == null || nextTranslationKey.charAt(0) == "#")))
            replaceOriginalValueWithNewValue(nextTranslationKey, nextTranslationValue)
    }

    def replaceOriginalValueWithNewValue(nextTranslationKey, nextTranslationValue) {
        def originalPropertyValue = propertiesFromPropertyFile.get(nextTranslationKey)
        if (originalPropertyValue != null) {
            if (!originalPropertyValue.equals(nextTranslationValue)) {
                propertiesFromPropertyFile.set(nextTranslationKey, nextTranslationValue)
                Log.writeLine("Property $nextTranslationKey: '$originalPropertyValue' replaced by '$nextTranslationValue'")
            }
        } else {
            Log.writeLine("exceptions", "Translated property '$nextTranslationKey' not found in properties file")
        }
    }

    def logMissingTranslations() {
        Log.writeLine("exceptions", "\r\n******* Missing translation keys or values:")
        logTranslationKeysWithNoValues()
        logPropertiesWithNoTranslations()
    }

    def logTranslationKeysWithNoValues() {
        // write exceptions accumulated while moving translations into properties file
        Log.writeLine("exceptions", "\r\n******* No $languageName translation in Excel export:")
        def noTranslationList = translationsFromExcelExport.getTranslations("$languageName", "")
        if (noTranslationList != null) {
            noTranslationList.each { Translation it ->
                def translationKey = it.get("Message Key")
                if ((translationKey != "") && (translationKey.charAt(0) != "#") && (!(ignorePropertyList.contains(translationKey)))) {
                    Log.writeLine("exceptions", "Property '$translationKey' has no $languageName translation in Excel export.")
                }
            }
        }
    }

    def logPropertiesWithNoTranslations() {
//      loop through all properties (iterator), find matching translation from Excel export if it exists; otherwise log missing translation
        propertiesFromPropertyFile.rewind()
        while (propertiesFromPropertyFile.hasNext()) {
            logIfNoMatchingExcelExportTranslation()
        }
    }

    def logIfNoMatchingExcelExportTranslation() {
        def nextProperty = propertiesFromPropertyFile.next()
        String nextPropertyKey = nextProperty.key
        if ((nextPropertyKey.charAt(0) != "*") && (!(ignorePropertyList.contains(nextPropertyKey)))) {
            //pseudo-properties (comments) have '*' in first character (maybe should trim left?)
            Translation matchingExcelExportTranslation = translationsFromExcelExport.getTranslation("Message Key", nextPropertyKey)
            if (matchingExcelExportTranslation == null)
                Log.writeLine("exceptions", "Property '$nextPropertyKey' does not have corresponding 'Message Key' in translation Excel export.")
            else {
                def test1 = matchingExcelExportTranslation.get(languageName)
                if (matchingExcelExportTranslation.get(languageName) == null) {
                    println nextPropertyKey
                    Log.writeLine("exceptions", "Property '$nextPropertyKey' in property file, but no $languageName translation in translation Excel export.")
                }
            }
        }
    }
}
