import org.apache.poi.openxml4j.util.ZipSecureFile
import properties.ExcelPropertyFile
import properties.ExcelPropertyRow
import properties.ExcelPropertySheet
import useful.Args
import useful.Config

class PlayWithSpreadsheets {

    Args propertyArgs

    PlayWithSpreadsheets(args) {
        start(args)
    }

    static main(args) {
        new PlayWithSpreadsheets(args)
    }

    def start(args) {
        propertyArgs = new Args(args)
        setDefaultArgs()
        generateSpreadsheet()
    }

    def setDefaultArgs() {
        if (propertyArgs.get("language") == null)
            propertyArgs.set("language", "Japanese")

        if (propertyArgs.get("path") == null)
            propertyArgs.set("path", "C:\\Users\\s0041664\\Documents\\Projects\\DMT-DE\\Project Work\\translations\\")
    }

    def generateSpreadsheet() {
        ZipSecureFile.setMinInflateRatio(0);
        ExcelPropertyFile fromFile = ExcelPropertyFile.openFileUsingChooser("Pick a spreadsheet", propertyArgs.get("path"))
        if (fromFile != null) {
            while (fromFile.hasNextExcelPropertySheet()) {
                ExcelPropertySheet modelPropertySheet = fromFile.nextExcelPropertySheet()
                print modelPropertySheet.sheetName + ","
            }
        }
    }

//    def duplicateModelInNewFile(ExcelPropertySheet modelPropertySheet, ExcelPropertySheet toPropertySheet) {
//        modelPropertySheet.resetRows()
//        while (modelPropertySheet.hasNextExcelPropertyRow()) {
//            print "."
//            ExcelPropertyRow fromRow = modelPropertySheet.nextExcelPropertyRow()
//            ExcelPropertyRow toRow = cloneModelRowAddingTranslatedColumns(fromRow, toPropertySheet)
//        }
//    }

//    ExcelPropertyRow cloneModelRowAddingTranslatedColumns(ExcelPropertyRow fromRow, ExcelPropertySheet toPropertySheet) {
//        Iterator cellIterator = fromRow.row.cellIterator()
//        while (cellIterator.hasNext()) {
//            cellIterator.next()
//        }
//    }
}