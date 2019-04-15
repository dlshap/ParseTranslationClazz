package libraryquestions

import properties.ExcelPropertyFile
import properties.ExcelPropertyRow
import properties.ExcelPropertySheet

class LibrarySpreadsheetUpdater extends LibrarySpreadsheetBuilder {

    ExcelPropertyFile oldLibraryExcelFile

    LibrarySpreadsheetUpdater(ExcelPropertyFile oldLibraryExcelFile) {
        this.oldLibraryExcelFile = oldLibraryExcelFile
        String pathName = oldLibraryExcelFile.file.getParent() + "\\"
        String newLibraryFileName = pathName + "\\new\\" + oldLibraryExcelFile.fileName
        this.createNewLibraryExcelFileFromFileName(newLibraryFileName)
    }

    def updateSpreadsheetFromModel(ExcelPropertyFile modelLibraryExcelFile) {
        while (modelLibraryExcelFile.hasNextExcelPropertySheet()) {
            ExcelPropertySheet modelSheet = modelLibraryExcelFile.nextExcelPropertySheet()
            String sheetName = modelSheet.sheetName
            ExcelPropertySheet oldSheet = oldLibraryExcelFile.getExcelPropertySheet(sheetName)
            if (oldSheet == null) {
                buildNewSheetFromModelSheet(modelSheet)
            } else {
                buildNewSheetFromModelAndOrig(modelSheet, oldSheet)
            }
            print "." // for impatient users
        }
        newLibraryExcelFile.writeAndClose()
    }

    def buildNewSheetFromModelAndOrig(ExcelPropertySheet modelSheet, ExcelPropertySheet oldSheet) {
        while (modelSheet.hasNextExcelPropertyRow()) {
            ExcelPropertyRow modelRow = modelSheet.nextExcelPropertyRow()
            ExcelPropertyRow oldRow = getOldRowMatchingKeysFromModelRow(oldSheet, modelRow)
            buildNewRowFromModelRowAndOldRow(modelRow, oldRow)
        }
    }

    private getOldRowMatchingKeysFromModelRow(ExcelPropertySheet oldSheet, ExcelPropertyRow modelRow) {
        Map<String, String> rowKeys = [:]
        ["Question Identifier", "BOM Fields"].each { fieldName ->
            rowKeys[fieldName] = modelRow.getValue(fieldName)
        }
        oldSheet.getFirstExcelPropertyRowMatchingKeys(rowKeys)
    }

    private buildNewRowFromModelRowAndOldRow(ExcelPropertyRow modelRow, ExcelPropertyRow oldRow) {

    }


    // get model keys
    // get old row matching model keys
    // if nomatch, gen row from model row
    // otherwise,
    //      if translated field, use old value
    //      otherwise
    //          if model doesn't match old, mark date field
    //          use model value

}
