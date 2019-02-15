import excelfilemanagement.ExcelFile
import excelfilemanagement.ExcelUtil
import libraryquestions.LibraryPropertyFile
import org.apache.poi.ss.usermodel.Cell
import org.apache.poi.ss.usermodel.Row
import org.apache.poi.ss.usermodel.Sheet
import org.apache.poi.ss.usermodel.Workbook
import filemanagement.BaseFile


class TryExcel {

    TryExcel() {
        start();
    }

    static main(args) {
        new TryExcel();
    }

    def start() {

        def filePath = "C:\\Users\\s0041664\\Documents\\Projects\\DMT-DE\\Project Work\\Translations\\DMT\\"
        LibraryPropertyFile libraryPropertyFile = LibraryPropertyFile.openLibraryPropertyFileUsingChooser("Pick a sheet", filePath)
        if (libraryPropertyFile != null) {
            println libraryPropertyFile.classNameList
            Sheet sheet = libraryPropertyFile.getSheet("BusinessOrganization")
            println sheet.sheetName
            println sheet.size()
//            println libraryPropertyFile.sheets
        }
    }
}

//        def fileName = "test.xls"
//
//        ExcelFile inSpreadsheet = ExcelFile.openSpreadsheetUsingChooser("Pick a file", filePath)
//        ExcelFile outSpreadsheet = ExcelFile.createNewSpreadsheetFromFileName(filePath+fileName, BaseFile.createFlag.CREATE) // create
//
//        Workbook inWorkbook = inSpreadsheet.excelWorkbook
//        Workbook outWorkbook = outSpreadsheet.excelWorkbook
//        Iterator inSheetIterator = inWorkbook.sheetIterator()
//        inSheetIterator.each { Sheet inSheet ->
//            Sheet outSheet = outWorkbook.createSheet(inSheet.sheetName)
//            Iterator rowIterator = inSheet.rowIterator()
//            rowIterator.eachWithIndex { Row inRow, int rowNum ->
//                Row outRow = outSheet.createRow(rowNum)
//                Iterator inCellIterator = inRow.cellIterator()
//                inCellIterator.eachWithIndex { Cell inCell, int cellNum ->
//                    Cell outCell = outRow.createCell(cellNum)
//                    outCell.setCellValue(ExcelUtil.toStringWithOnlyIntegerNumerics(inCell))
//                }
//            }
//        }
//        outSpreadsheet.writeAndClose()
//    }
