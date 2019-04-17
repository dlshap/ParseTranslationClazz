package libraryquestions

import filemanagement.BaseFile
import logging.Log
import org.apache.poi.ss.usermodel.Workbook
import properties.ExcelPropertyFile
import properties.ExcelPropertyRow
import properties.ExcelPropertySheet

class LibrarySpreadsheetUpdater {

    static buildNewSpreadsheetFromModel(String newFileName, ExcelPropertyFile modelLibraryExcelFile) {
        ExcelPropertyFile newLibraryExcelFile = ExcelPropertyFile.createNewFileFromFileName(newFileName, BaseFile.CreateFlag.CREATE)
        while (modelLibraryExcelFile.hasNextExcelPropertySheet()) {
            ExcelPropertySheet modelPropertySheet = modelLibraryExcelFile.nextExcelPropertySheet()
            buildNewSheetFromModelSheet(newLibraryExcelFile, modelPropertySheet)
            print "." // for impatient users
        }
        print "\n"
        newLibraryExcelFile
    }

    private
    static buildNewSheetFromModelSheet(ExcelPropertyFile newLibraryExcelFile, ExcelPropertySheet modelPropertySheet) {
        Workbook languageWorkbook = newLibraryExcelFile.workbook
        ExcelPropertySheet languagePropertySheet = ExcelPropertySheet.createExcelPropertySheetInWorkbookFromModelSheet(languageWorkbook, modelPropertySheet)
        buildDataRowsFromModel(languagePropertySheet, modelPropertySheet)
    }

    private
    static buildDataRowsFromModel(ExcelPropertySheet languagePropertySheet, ExcelPropertySheet modelPropertySheet) {
        while (modelPropertySheet.hasNextExcelPropertyRow()) {
            ExcelPropertyRow modelPropertyRow = modelPropertySheet.nextExcelPropertyRow()
            languagePropertySheet.cloneExcelRow(modelPropertyRow.row.getRowNum(), modelPropertyRow)
        }
    }

    static ExcelPropertyRow getRowMatchingKeysFromModelRow(ExcelPropertySheet sheet, ExcelPropertyRow oldRow) {
        Map<String, String> rowKeys = getRowKeys(oldRow)
        sheet.getFirstExcelPropertyRowMatchingKeys(rowKeys)
    }

    static Map<String, String> getRowKeys(ExcelPropertyRow row) {
        Map<String, String> rowKeys = [:]
        LibraryColumns.libraryKeyColumns.each { fieldName ->
            rowKeys[fieldName] = row.getValue(fieldName)
        }
        rowKeys
    }

    static updateNewRowTranslationsFromOldRowForSheet(ExcelPropertyRow newRow, ExcelPropertyRow oldRow, String sheetName) {
        def today = Calendar.getInstance().time
        LibraryColumns.libraryTranslatedColumns.each { englishColumnName, translatedColumnName ->
            newRow.setValue(translatedColumnName, oldRow.getValue(translatedColumnName))
            String newEnglishName = (newRow.getValue(englishColumnName)).trim()
            String oldEnglishName = (oldRow.getValue(englishColumnName)).trim()
            if (!(newEnglishName.equals(oldEnglishName))) {
                newRow.setValue("Date Changed", today)
                Log.writeLine("updates", "$sheetName: English text changed for $englishColumnName from: $oldEnglishName to: $newEnglishName")
            }
        }
    }
}
