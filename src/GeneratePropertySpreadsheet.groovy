import filemanagement.BaseFile
import filemanagement.IgnoreFile
import i18n.LanguageLabels
import logging.Log
import logging.LogUtils
import properties.ExcelPropertyFile
import properties.ExcelPropertyRow
import properties.ExcelPropertySheet
import properties.PropertyFile
import translations.TranslationProperties
import useful.Args

class GeneratePropertySpreadsheet {

    Args propertyArgs
    String language, path       // args

    GeneratePropertySpreadsheet(args) {
        start(args)
    }

    static main(args) {
        new GeneratePropertySpreadsheet(args)
    }

    def start(args) {
        propertyArgs = new Args(args)
        setDefaultArgs()
        LogUtils.startUtil(this.getClass().name, path)
        if (!(LanguageLabels.isLanguageInList(language)))
            Log.writeLine"app", "ERROR: \"$language\" is not in language list"
        else
            generateSpreadsheet()
    }

    def setDefaultArgs() {
        language = propertyArgs.get("language")
        if (language == null)
            language = "All"
        path = propertyArgs.get("path")
        if (path == null)
            path = "C:\\Users\\s0041664\\Documents\\Projects\\DMT-DE\\Project Work\\translations\\"
    }

    def generateSpreadsheet() {
        ExcelPropertyFile modelExcelPropertyFile = chooseModelPropertySpreadsheet()
        if (modelExcelPropertyFile != null) {
            ExcelPropertyFile outputExcelPropertyFile = createOutputExcelPropertyFile(modelExcelPropertyFile)
            if (outputExcelPropertyFile != null) {
                while (modelExcelPropertyFile.hasNextExcelPropertySheet()) {
                    ExcelPropertySheet modelPropertySheet = modelExcelPropertyFile.nextExcelPropertySheet()
                    ExcelPropertySheet newPropertySheet = createPropertySheetFromPropertiesFileUsingModel(outputExcelPropertyFile, modelPropertySheet)
                    movePropertiesIntoPropertySheetUsingModelSheet(newPropertySheet, modelPropertySheet)
                }
                outputExcelPropertyFile.writeAndClose()
            }
        }
    }

    ExcelPropertyFile chooseModelPropertySpreadsheet() {
        def modelSpreadsheetPath = path + "\\Spreadsheets\\DMTDEPropertySpreadsheets\\"
        def language = propertyArgs.get("language")
        def modelFileName = modelSpreadsheetPath + "DMT-DE Properties translations ($language).xlsx"
        ExcelPropertyFile modelExcelPropertyFile = ExcelPropertyFile.openFileUsingFileName(modelFileName)
        modelExcelPropertyFile
    }

    ExcelPropertyFile createOutputExcelPropertyFile(ExcelPropertyFile modelExcelPropertyFile) {
        String outputFileName = buildOutputFileName(modelExcelPropertyFile)
        ExcelPropertyFile outputExcelPropertyFile = ExcelPropertyFile.createNewFileFromFileName(outputFileName, BaseFile.CreateFlag.CREATE)
        outputExcelPropertyFile
    }

    String buildOutputFileName(ExcelPropertyFile modelFile) {
        def outputPath = path + "\\Spreadsheets\\DMTDEPropertySpreadsheets\\new\\"
        def outputFileName = outputPath + "DMT-DE Properties translations ($language)_new.xlsx"
        outputFileName
    }

    ExcelPropertySheet createPropertySheetFromPropertiesFileUsingModel(ExcelPropertyFile outputExcelPropertyFile, ExcelPropertySheet modelPropertySheet) {
        def logPath = path + "\\Spreadsheets\\DMTDEPropertySpreadsheets\\logs\\"
        LogUtils.openLogs(logPath, "${modelPropertySheet.sheetName}-properties")
        ExcelPropertySheet outputPropertySheet = outputExcelPropertyFile.createNewExcelPropertySheetFromModel(modelPropertySheet)
        outputPropertySheet
    }

    def movePropertiesIntoPropertySheetUsingModelSheet(ExcelPropertySheet newPropertySheet, ExcelPropertySheet modelPropertySheet) {
        String propertyFileName = buildPropertyFileName(modelPropertySheet.sheetName)
        PropertyFile propertyFile = PropertyFile.openPropertyFileFromFileName(propertyFileName)
        TranslationProperties translationProperties = propertyFile.translationProperties
        String ignoreFileName = buildIgnoreFileName(modelPropertySheet.sheetName)
        IgnoreFile ignoreFile = new IgnoreFile(ignoreFileName)
        ArrayList<String> ignoreProperties = ignoreFile.ignoreList
        newPropertySheet.setLanguage(language)
        updateNewSheetFromPropertiesFileAndModelExceptIgnores(newPropertySheet, translationProperties, modelPropertySheet, ignoreProperties)
        logDeletedProperties(modelPropertySheet, newPropertySheet)
    }

    def buildPropertyFileName(String sheetName) {
        path + "\\$sheetName\\PropertyFiles\\messages.properties"
    }

    def buildIgnoreFileName(String sheetName) {
        path + "\\$sheetName\\PropertyFiles\\ignore.messages.properties"
    }

    def updateNewSheetFromPropertiesFileAndModelExceptIgnores(ExcelPropertySheet newPropertySheet, TranslationProperties translationProperties, ExcelPropertySheet modelPropertySheet, ArrayList<String> ignoreProperties) {
        int propIndex = 1
        while (translationProperties.hasNext()) {
            def property = translationProperties.next()
            def propertyKey = property.getKey()
            if (!(ignoreProperties.contains(propertyKey))) {
                ExcelPropertyRow modelPropertyRow = modelPropertySheet.getFirstExcelPropertyRowMatchingKeys(["Message Key": propertyKey])
                if (modelPropertyRow != null)
                    updateTranslationInRow(newPropertySheet, property, modelPropertyRow, propIndex)
                else
                    addNewRowFromTranslation(newPropertySheet, property, propIndex)
                print((propIndex.mod(100) == 0) ? ".\n" : ".")    // for impatient users
                propIndex++
            }
        }
        print "\n"
    }

    def updateTranslationInRow(ExcelPropertySheet newPropertySheet, property, ExcelPropertyRow modelPropertyRow, int propIndex) {
        def today = Calendar.getInstance().time
        ExcelPropertyRow newPropertyRow = newPropertySheet.cloneExcelPropertyRow(propIndex, modelPropertyRow)
        def oldEnglishValue = modelPropertyRow.getValue("English").trim()
        def newEnglishValue = property.getValue().trim()
        newPropertyRow.setValue("Index", propIndex)
        if (oldEnglishValue != newEnglishValue) {
            Log.writeLine "updates", "Changing property ${property.getKey()}: Old: $oldEnglishValue New: $newEnglishValue "
            newPropertyRow.setValue("English", newEnglishValue)
            newPropertyRow.setValue("Date Changed", today)
            newPropertyRow.setStyle("Date Changed", newPropertySheet.getDateStyle())
        }
    }

    def addNewRowFromTranslation(ExcelPropertySheet newPropertySheet, property, int propIndex) {
        def today = Calendar.getInstance().time
        def propertyMap = [:]
        if ((property.getKey())[0] != "*") {
            Log.writeLine "adds", "New property added: ${property.getKey()}"
            propertyMap.put("Date Changed", today)
        }
        propertyMap.put("Index", propIndex)
        def propertyId = (property.getKey())[0] == "*" ? property.getValue() : property.getKey()
        def propertyValue = (property.getKey())[0] == "*" ? "" : property.getValue()
        propertyMap.put("Message Key", propertyId)
        propertyMap.put("English", propertyValue)
        propertyMap.put(newPropertySheet.getLanguage(), "")
        newPropertySheet.addRow(propIndex, propertyMap)
        applyDateStyleToDateChangedCell(newPropertySheet, propIndex)
    }

    private applyDateStyleToDateChangedCell(ExcelPropertySheet excelPropertySheet, Integer rowNumber) {
        ExcelPropertyRow excelPropertyRow = excelPropertySheet.getExcelPropertyRow(rowNumber)
        excelPropertyRow.setStyle("Date Changed", excelPropertySheet.getDateStyle())
    }

    def logDeletedProperties(ExcelPropertySheet modelPropertySheet, ExcelPropertySheet newPropertySheet) {
        modelPropertySheet.resetRows()
        while (modelPropertySheet.hasNextExcelPropertyRow()) {
            ExcelPropertyRow modelRow = modelPropertySheet.nextExcelPropertyRow()
            String propertyKey = modelRow.getValue("Message Key")
            if (propertyKey != null && propertyKey != "" && propertyKey[0] != "#") {
                ExcelPropertyRow newRow = newPropertySheet.getFirstExcelPropertyRowMatchingKeys(["Message Key": propertyKey])
                if (newRow == null) {
                    Log.writeLine("deletes", "Removed property: $propertyKey = ${modelRow.getValue("$language")}")
                }
            }
        }
    }
}