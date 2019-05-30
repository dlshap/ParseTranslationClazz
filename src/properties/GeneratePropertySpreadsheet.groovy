package properties

import filemanagement.BaseFile
import filemanagement.IgnoreFile
import i18n.LanguageLabels
import logging.Log
import logging.LogUtils
import translations.TranslationProperties

class GeneratePropertySpreadsheet {

    PropertyArgs propertyArgs
    String language       // args

    GeneratePropertySpreadsheet(propertyArgs) {
        this.propertyArgs = propertyArgs
        if (!(LanguageLabels.isLanguageInList(this.propertyArgs.language))) {
            Log.writeLine  "ERROR: \"${this.propertyArgs.language}\" is not in language list"
        } else {
            generateSpreadsheet()
        }
    }

    private generateSpreadsheet() {this.propertyArgs.language
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

    private ExcelPropertyFile chooseModelPropertySpreadsheet() {
        def modelSpreadsheetPath = propertyArgs.spreadsheetPath
        def modelFileName = modelSpreadsheetPath + "DMT-DE Properties translations (${propertyArgs.language}).xlsx"
        ExcelPropertyFile modelExcelPropertyFile = ExcelPropertyFile.openFileUsingFileName(modelFileName)
        modelExcelPropertyFile
    }

    private ExcelPropertyFile createOutputExcelPropertyFile(ExcelPropertyFile modelExcelPropertyFile) {
        String outputFileName = buildOutputFileName(modelExcelPropertyFile)
        ExcelPropertyFile outputExcelPropertyFile = ExcelPropertyFile.createNewFileFromFileName(outputFileName, BaseFile.CreateFlag.CREATE)
        outputExcelPropertyFile
    }

    private String buildOutputFileName(ExcelPropertyFile modelFile) {
        def outputPath = propertyArgs.spreadsheetPath + "\\new\\"
        def outputFileName = outputPath + "DMT-DE Properties translations (${propertyArgs.language})_new.xlsx"
        outputFileName
    }

    private ExcelPropertySheet createPropertySheetFromPropertiesFileUsingModel(ExcelPropertyFile outputExcelPropertyFile, ExcelPropertySheet modelPropertySheet) {
        def logPath = propertyArgs.spreadsheetPath + "\\logs\\"
        LogUtils.openLogs(logPath, "${modelPropertySheet.sheetName}-properties")
        ExcelPropertySheet outputPropertySheet = outputExcelPropertyFile.createNewExcelPropertySheetFromModel(modelPropertySheet)
        outputPropertySheet
    }

    private movePropertiesIntoPropertySheetUsingModelSheet(ExcelPropertySheet newPropertySheet, ExcelPropertySheet modelPropertySheet) {
        String propertyFileName = buildPropertyFileName(modelPropertySheet.sheetName)
        PropertyFile propertyFile = PropertyFile.openPropertyFileFromFileName(propertyFileName)
        TranslationProperties translationProperties = propertyFile.translationProperties
        String ignoreFileName = buildIgnoreFileName(modelPropertySheet.sheetName)
        IgnoreFile ignoreFile = new IgnoreFile(ignoreFileName)
        ArrayList<String> ignoreProperties = ignoreFile.ignoreList
        newPropertySheet.setLanguage(propertyArgs.language)
        updateNewSheetFromPropertiesFileAndModelExceptIgnores(newPropertySheet, translationProperties, modelPropertySheet, ignoreProperties)
        logDeletedProperties(modelPropertySheet, newPropertySheet)
    }

    private buildPropertyFileName(String sheetName) {
        propertyArgs.propertyFilePath + "\\$sheetName\\PropertyFiles\\messages.properties"
    }

    private buildIgnoreFileName(String sheetName) {
        propertyArgs.propertyFilePath + "\\$sheetName\\PropertyFiles\\ignore.messages.properties"
    }

    private updateNewSheetFromPropertiesFileAndModelExceptIgnores(ExcelPropertySheet newPropertySheet, TranslationProperties translationProperties, ExcelPropertySheet modelPropertySheet, ArrayList<String> ignoreProperties) {
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
                print((propIndex % 100 == 0) ? ".\n" : ".")    // for impatient users
                propIndex++
            }
        }
        print "\n"
    }

    private updateTranslationInRow(ExcelPropertySheet newPropertySheet, property, ExcelPropertyRow modelPropertyRow, int propIndex) {
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

    private addNewRowFromTranslation(ExcelPropertySheet newPropertySheet, property, int propIndex) {
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

    private logDeletedProperties(ExcelPropertySheet modelPropertySheet, ExcelPropertySheet newPropertySheet) {
        modelPropertySheet.resetRows()
        while (modelPropertySheet.hasNextExcelPropertyRow()) {
            ExcelPropertyRow modelRow = modelPropertySheet.nextExcelPropertyRow()
            String propertyKey = modelRow.getValue("Message Key")
            if (propertyKey != null && propertyKey != "" && propertyKey[0] != "#") {
                ExcelPropertyRow newRow = newPropertySheet.getFirstExcelPropertyRowMatchingKeys(["Message Key": propertyKey])
                if (newRow == null) {
                    Log.writeLine("deletes", "Removed property: $propertyKey = ${modelRow.getValue(propertyArgs.language)}")
                }
            }
        }
    }
}