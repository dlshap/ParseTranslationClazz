import excelfilemanagement.ExcelRow
import excelfilemanagement.ExcelSheet
import properties.ExcelPropertyFile
import filemanagement.BaseFile

class TryExcel {

    TryExcel() {
        start();
    }

    static main(args) {
        def tryExcel = new TryExcel();
    }

    def start() {

        def componentName = "DMT"

        def filePath = "C:\\Users\\s0041664\\Documents\\Projects\\DMT-DE\\Project Work\\Translations\\Spreadsheets\\PropertySpreadsheets\\DMTDE\\"

        def fileName = "test.xls"

        ExcelPropertyFile fromFile = ExcelPropertyFile.createFileUsingChooser("Gimme a file", filePath)
        ExcelPropertyFile toFile = ExcelPropertyFile.createNewExcelPropertyFileFromFileName(filePath + fileName, BaseFile.createFlag.CREATE)
        ExcelSheet fromSheet1 = fromFile.getExcelSheet("DMT")
        ExcelSheet fromSheet2 = fromFile.getExcelSheet("DE")

        println "DMT From...first row: ${fromSheet1.getFirstRowNum()} last row: ${fromSheet1.getLastRowNum()}"
        println "DE From...first row: ${fromSheet2.getFirstRowNum()} last row: ${fromSheet2.getLastRowNum()}"

        while (fromSheet1.hasNextRow()) {
            ExcelRow excelRow = fromSheet1.nextExcelRow()
//            def x = []
//            println excelRow.cellIterator.collect() { Cell cell ->
//                new ExcelCell(cell).toString()
//            }
            println excelRow.collectCellsAsStrings()
//            println x
        }

//        ExcelPropertyFile fromFile = ExcelPropertyFile.getExcelPropertyFileUsingChooser(filePath, "Gimme some data")
//        if (fromFile != null) {
//            ExcelPropertyFile toFile = ExcelPropertyFile.createNewExcelPropertyFileFromFileName(filePath + fileName, BaseFile.createFlag.CREATE)
//            copyFromTo(fromFile, toFile)
//        }
    }

    def copyFromTo(fromFile, toFile) {

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
    }
}

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


