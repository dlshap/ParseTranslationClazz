import excelfilemanagement.ExcelFile
import excelfilemanagement.ExcelUtil
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

        def filePath = "C:\\Users\\s0041664\\Documents\\Projects\\DMT-DE\\Project Work\\Translations\\Spreadsheets\\PropertySpreadsheets\\DMTDE\\"

        def fileName = "test.xls"

        ExcelFile inSpreadsheet = ExcelFile.openSpreadsheetUsingChooser("Pick a file", filePath)
        ExcelFile outSpreadsheet = ExcelFile.createNewSpreadsheetFromFileName(filePath+fileName, BaseFile.createFlag.CREATE) // create

        Workbook inWorkbook = inSpreadsheet.workbook
        Workbook outWorkbook = outSpreadsheet.workbook
        Iterator inSheetIterator = inWorkbook.sheetIterator()
        inSheetIterator.each { Sheet inSheet ->
            Sheet outSheet = outWorkbook.createSheet(inSheet.sheetName)
            Iterator rowIterator = inSheet.rowIterator()
            rowIterator.eachWithIndex { Row inRow, int rowNum ->
                Row outRow = outSheet.createRow(rowNum)
                Iterator inCellIterator = inRow.cellIterator()
                inCellIterator.eachWithIndex { Cell inCell, int cellNum ->
                    Cell outCell = outRow.createCell(cellNum)
                    outCell.setCellValue(ExcelUtil.toStringWithOnlyIntegerNumerics(inCell))
                }
            }
        }
        outSpreadsheet.writeAndClose()
    }
}

//        Sheet inSheet = inWorkbook.getSheet("DMT")
//        Sheet outSheetDMT = outWorkbook.createSheet("DMT")
//        Iterator rowIterator = inSheet.rowIterator()
//        rowIterator.each { Row row ->
//            Iterator cellIterator = row.cellIterator()
//            def allCells = cellIterator.collect()
//            println "Row: ${row.rowNum} Cells: $allCells"
//        }

//        ExcelPropertyFile fromFile = ExcelPropertyFile.createFileUsingChooser("Gimme a file", filePath)
//        ExcelPropertyFile toFile = ExcelPropertyFile.createNewExcelPropertyFileFromFileName(filePath + fileName, BaseFile.createFlag.CREATE)
//
//        println "DMT From...first row: ${fromSheet1.getFirstRowNum()} last row: ${fromSheet1.getLastRowNum()}"
//        println "DE From...first row: ${fromSheet2.getFirstRowNum()} last row: ${fromSheet2.getLastRowNum()}"
//
//        while (fromSheet1.hasNextRow()) {
//            ExcelRow excelRow = fromSheet1.nextExcelRow()
//            println excelRow.collectCellsAsStrings()
//            def cellCollection = excelRow.collectCells()
//        }
//        ExcelPropertyFile fromFile = ExcelPropertyFile.getExcelPropertyFileUsingChooser(filePath, "Gimme some data")
//        if (fromFile != null) {
//            ExcelPropertyFile toFile = ExcelPropertyFile.createNewExcelPropertyFileFromFileName(filePath + fileName, BaseFile.createFlag.CREATE)
//            copyFromTo(fromFile, toFile)
//        }
//    def copyFromTo(fromFile, toFile) {

//            Font font = workbook.createFont()
//            font.setBold(true)
//            CellStyle style = workbook.createCellStyle()
//            style.setFont(font)
//
//            Sheet sheet = workbook.createSheet("Test")
//            int firstRow = sheet.getFirstRowNum()
//            int lastRow = sheet.getLastRowNum()
////        println "first row: $firstRow, last row: $lastRow"
//            0.upto(5) { int rowNum ->
//                Row row = sheet.createRow(rowNum)
//                println "row: $rowNum divide: ${rowNum % 2}"
//                (0..3).each { cellNum ->
//                    Cell cell = row.createCell(cellNum)
//                    cell.setCellValue(cellNum)
//                    if (rowNum % 2 == 0)
//                        cell.setCellStyle(style)
//                }
//            }
//            workbook.createSheet("Test2")
//            excelPropertyFile.writeAndClose()
//    }


//    def useExcelFile() {
//        def outputFileName = "DMT-DE Properties Translations(${propertyArgs.get("language")}).xlsx"
//        try {
//            def createFlag = propertyArgs.get("overwrite") == "yes" ? BaseFile.createFlag.CREATE : BaseFile.createFlag.CREATE_ONLY_IF_NO_EXISTING_FILE
//            outputPropertySpreadsheet = ExcelPropertyFile.createNewExcelPropertyFileFromFileName(outputPath, outputFileName, createFlag)
//        } catch (OverwriteFileException e) {
//            println "! ${e.toString()} !"
//        }
//    }
//
//    def getWorkBookFromFile(file) {
//        if (file != null) {
//            def fileName = file.name
//            def fileExtension = fileName.substring(fileName.indexOf("."))
//            if (fileExtension == ".xlsx")
//                new XSSFWorkbook()
//            else
//                new HSSFWorkbook()
//        }
//    }

//        ExcelPropertyFile excelFile = ExcelPropertyFile.getPropertiesExcelFileFromFileAndPathNames(filePath, "test.xlsx")

//        def chooserPrompt = Messages.getString("prompt.for.spreadsheet", "DMT")
//        ExcelPropertyFile excelPropertyFile = ExcelPropertyFile.getExcelPropertyFileUsingChooser(filePath, chooserPrompt, "DMT")

//        ExcelPropertySheet sheet = excelPropertyFile.getPropertySheet("DMT")
//
//        while (sheet.hasNextRow()) {
//            ExcelPropertyRow excelPropertyRow = sheet.nextRow()
//            def keyMap = excelPropertyRow.getPropertyValueMap()
//            println keyMap
//        }


