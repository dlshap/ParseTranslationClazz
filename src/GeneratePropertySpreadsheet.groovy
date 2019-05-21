import filemanagement.BaseFile
import i18n.LanguageLabels
import i18n.Messages
import logging.Dates
import logging.Log
import org.apache.poi.ss.usermodel.CellStyle
import org.apache.poi.ss.usermodel.Row
import org.apache.poi.ss.usermodel.Workbook
import properties.ExcelPropertyFile
import properties.ExcelPropertyRow
import properties.ExcelPropertySheet
import properties.PropertyFile
import translations.TranslationProperties
import useful.Args

class GeneratePropertySpreadsheet {

    static final MODEL_SPREADSHEET_PROMPT = "prompt.for.translation.spreadsheet.for"

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
        if (!(LanguageLabels.isLanguageInList(language)))
            println "ERROR: \"$language\" is not in language list"
        else
            generateSpreadsheet()
    }

    def setDefaultArgs() {
        language = propertyArgs.get("language")
        if (language == null)
            language = "All"
        path = propertyArgs.get("path")
        if (path == null)
            path = "C:\\Users\\s0041664\\Documents\\Projects\\DMT-DE\\Project Work\\Translations\\"
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
        def modelSpreadsheetPath = path + "Spreadsheets\\PropertySpreadsheets\\DMTDE\\"
        def language = propertyArgs.get("language")
        def prompt = Messages.getString(MODEL_SPREADSHEET_PROMPT, "Master Properties", language)
        ExcelPropertyFile modelExcelPropertyFile = ExcelPropertyFile.openFileUsingChooser(prompt, modelSpreadsheetPath)
        modelExcelPropertyFile
    }

    ExcelPropertyFile createOutputExcelPropertyFile(ExcelPropertyFile modelExcelPropertyFile) {
        String outputFileName = buildOutputFileName(modelExcelPropertyFile)
        ExcelPropertyFile outputExcelPropertyFile = ExcelPropertyFile.createNewFileFromFileName(outputFileName, BaseFile.CreateFlag.CREATE)
        outputExcelPropertyFile
    }

    String buildOutputFileName(ExcelPropertyFile modelFile) {
        def outputPath = path + "\\Spreadsheets\\PropertySpreadsheets\\DMTDE\\"
        def outputFileName = outputPath + "\\new\\DMT-DE Properties Translations ($language)_new.xlsx"
        outputFileName
    }

    ExcelPropertySheet createPropertySheetFromPropertiesFileUsingModel(ExcelPropertyFile outputExcelPropertyFile, ExcelPropertySheet modelPropertySheet) {
        openTranslationLogsForSheet(modelPropertySheet.sheetName)
        ExcelPropertySheet outputPropertySheet = outputExcelPropertyFile.createNewExcelPropertySheetFromModel(modelPropertySheet)
        outputPropertySheet
    }

    def openTranslationLogsForSheet(String sheetName) {
        def logsFilePath = path + "\\Spreadsheets\\PropertySpreadsheets\\DMTDE\\logs\\"
        Log.open("adds", logsFilePath + "$sheetName log-property-adds.txt")
        Log.writeLine "adds", "Running on " + Dates.currentDateAndTime() + ":\r\n"
        Log.open("updates", logsFilePath + "$sheetName log-property-changes.txt")
        Log.writeLine "updates", "Running on " + Dates.currentDateAndTime() + ":\r\n"
        Log.open("deletes", logsFilePath + "$sheetName log-property-deletes.txt")
        Log.writeLine "deletes", "Running on " + Dates.currentDateAndTime() + ":\r\n"
    }

    def movePropertiesIntoPropertySheetUsingModelSheet(ExcelPropertySheet newPropertySheet, ExcelPropertySheet modelPropertySheet) {
        String propertyFileName = buildPropertyFileName(modelPropertySheet.sheetName)
        PropertyFile propertyFile = PropertyFile.openPropertyFileFromFileName(propertyFileName)
        TranslationProperties translationProperties = propertyFile.translationProperties
        String ignoreFileName = buildIgnoreFileName(modelPropertySheet.sheetName)
        PropertyFile ignoreFile = PropertyFile.openPropertyFileFromFileName(ignoreFileName)
        TranslationProperties ignoreProperties = ignoreFile.translationProperties
        newPropertySheet.setLanguage(language)
        updateNewSheetFromPropertiesFileAndModelExceptIgnores(newPropertySheet, translationProperties, modelPropertySheet, ignoreProperties)
        logDeletedProperties(modelPropertySheet, translationProperties)
    }

    def buildPropertyFileName(String sheetName) {
        path + "\\$sheetName\\PropertyFiles\\messages.properties"
    }

    def buildIgnoreFileName(String sheetName) {
        path + "\\$sheetName\\PropertyFiles\\ignore.messages.properties"
    }

    def updateNewSheetFromPropertiesFileAndModelExceptIgnores(ExcelPropertySheet newPropertySheet, TranslationProperties translationProperties, ExcelPropertySheet modelPropertySheet, TranslationProperties ignoreProperties) {
        int propIndex = 1
        while (translationProperties.hasNext()) {
            def property = translationProperties.next()
            def propertyKey = property.getKey()
            if (ignoreProperties.get(propertyKey) == null) {
                ExcelPropertyRow modelPropertyRow = modelPropertySheet.getFirstExcelPropertyRowMatchingKeys(["Message Key": propertyKey])
                if (modelPropertyRow != null)
                    updateTranslationInRow(newPropertySheet, property, modelPropertyRow, propIndex)
                else
                    addNewRowFromTranslation(newPropertySheet, property, propIndex)
                print ((propIndex.mod(100) == 0) ? ".\n" : ".")    // for impatient users
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

    CellStyle getDateStyle(ExcelPropertySheet excelPropertySheet) {
        Workbook workbook = excelPropertySheet.workbook
        CellStyle dateCellStyle = workbook.createCellStyle()
        short dateFormat = workbook.createDataFormat().getFormat("mm/dd/yyyy")
        dateCellStyle.setDataFormat(dateFormat)
        dateCellStyle
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

    def logDeletedProperties(ExcelPropertySheet modelPropertySheet, TranslationProperties translationProperties) {
        modelPropertySheet.resetRows()
        while (modelPropertySheet.hasNextExcelPropertyRow()) {
            ExcelPropertyRow modelRow = modelPropertySheet.nextExcelPropertyRow()
            String propertyKey = modelRow.getValue("Message Key")
            if (propertyKey != null && propertyKey != "" && propertyKey[0] != "#" && translationProperties.get(propertyKey) == null) {
                Log.writeLine("deletes", "Removed property: $propertyKey:${modelRow.getValue("English")}")
            }
        }
    }
}