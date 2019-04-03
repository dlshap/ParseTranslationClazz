package libraryquestions

import excelfilemanagement.ExcelSheet
import excelfilemanagement.ExcelSheetProperties
import org.apache.poi.ss.usermodel.Cell
import org.apache.poi.ss.usermodel.CellStyle
import org.apache.poi.ss.usermodel.Row
import org.apache.poi.ss.usermodel.Sheet
import org.apache.poi.ss.usermodel.Workbook
import properties.ExcelPropertySheet

class ExcelLibrarySheet extends ExcelSheet {

//    static createExcelLibrarySheetFromAWorkbookSheet(Workbook workbook, Sheet sheet, int headerRowNum) {
//        //existing ExcelPropertySheet
//        ExcelLibrarySheet excelLibrarySheet = new ExcelLibrarySheet()
//        excelLibrarySheet.workbook = workbook
//        excelLibrarySheet.sheet = sheet
//        excelLibrarySheet.setupSheetFromSheetWithStyles(excelLibrarySheet, headerRowNum)
//        excelLibrarySheet
//    }
//
//    private setupSheetFromSheetWithStyles(ExcelLibrarySheet stylesSourceSheet, int headerRowNum) {
//        ArrayList<CellStyle> dataCellStyles = cloneStylesFromSheetWithStyles(stylesSourceSheet, headerRowNum + 1)
//        sheetProperties = new ExcelSheetProperties(this, headerRowNum, dataCellStyles)
//        resetRows()
//    }
//
//    private ArrayList<CellStyle> cloneStylesFromSheetWithStyles(ExcelLibrarySheet stylesSourceSheet, int rowNum) {
//        ArrayList<CellStyle> cellStyles = []
//        Row modelRow = stylesSourceSheet.getRow(rowNum)
//        for (int colNum = 0; colNum < modelRow.getLastCellNum(); colNum++) {
//            Cell modelCell = modelRow.getCell(colNum)
//            String cellValue = modelCell.toString().trim()
//            if ((cellValue == null) || (cellValue == ""))
//                break
//            else {
//                CellStyle modelCellStyle = modelCell.getCellStyle()
//                CellStyle cellStyle = workbook.createCellStyle()
//                cellStyle.cloneStyleFrom(modelCellStyle)
//                cellStyles << cellStyle
//            }
//        }
//        cellStyles
//    }
}
