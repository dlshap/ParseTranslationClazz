package libraryquestions

import filemanagement.BaseFile
import org.apache.poi.ss.usermodel.Workbook
import properties.ExcelPropertyFile
import properties.ExcelPropertyRow
import properties.ExcelPropertySheet

class LibrarySpreadsheetBuilder {

    static buildNewSpreadsheetFromModel(String newFileName, ExcelPropertyFile modelLibraryExcelFile) {
        ExcelPropertyFile newLibraryExcelFile = ExcelPropertyFile.createNewFileFromFileName(newFileName, BaseFile.CreateFlag.CREATE)
        while (modelLibraryExcelFile.hasNextExcelPropertySheet()) {
            ExcelPropertySheet modelPropertySheet = modelLibraryExcelFile.nextExcelPropertySheet()
            buildNewSheetFromModelSheet(newLibraryExcelFile, modelPropertySheet)
            print "." // for impatient users
        }
        newLibraryExcelFile
    }

    private static buildNewSheetFromModelSheet(ExcelPropertyFile newLibraryExcelFile, ExcelPropertySheet modelPropertySheet) {
        Workbook languageWorkbook = newLibraryExcelFile.workbook
        ExcelPropertySheet languagePropertySheet = ExcelPropertySheet.createExcelPropertySheetInWorkbookFromModelSheet(languageWorkbook, modelPropertySheet)
        buildDataRowsFromModel(languagePropertySheet, modelPropertySheet)
    }

     private static buildDataRowsFromModel(ExcelPropertySheet languagePropertySheet, ExcelPropertySheet modelPropertySheet) {
        while (modelPropertySheet.hasNextExcelPropertyRow()) {
            ExcelPropertyRow modelPropertyRow = modelPropertySheet.nextExcelPropertyRow()
            languagePropertySheet.cloneExcelRow(modelPropertyRow.row.getRowNum(), modelPropertyRow)
        }
    }

}
