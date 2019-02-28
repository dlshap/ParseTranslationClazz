import filemanagement.BaseFile

import static filemanagement.BaseFile.createFile
import static filemanagement.BaseFile.CreateFlag.CREATE


class TryExcel {

    TryExcel() {
        start();
    }

    static main(args) {
        new TryExcel();
    }

    def start() {

        def filePath = "C:\\Users\\s0041664\\Documents\\Projects\\DMT-DE\\Project Work\\Translations\\DMT\\doofus\\doofus\\"

        def file = BaseFile.createFile(filePath + "test.txt", BaseFile.CreateFlag.CREATE)

        file.file << "test45"

//        BaseFile baseFile = createFile(filePath + "test.txt", CREATE)

//        LineFile lineFile = new LineFile(filePath+"\\doofus\\test.txt", CREATE)
//        lineFile.writeLine("test")
    }
}

//        def fileName = "test.xls"
//
//        ExcelFile inSpreadsheet = ExcelFile.openSpreadsheetUsingChooser("Pick a file", filePath)
//        ExcelFile outSpreadsheet = ExcelFile.createNewSpreadsheetFromFileName(filePath+fileName, BaseFile.CreateFlag.CREATE) // create
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
