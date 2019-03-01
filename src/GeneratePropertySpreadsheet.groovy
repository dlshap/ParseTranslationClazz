import properties.ExcelPropertyFile
import properties.ExcelPropertyRow
import properties.ExcelPropertySheet
import useful.Args

import static filemanagement.BaseFile.CreateFlag.*

class GeneratePropertySpreadsheet {

    static final SPREADSHEET_PROMPT = "prompt.for.master.spreadsheet.for.component"

    Args propertyArgs

    GeneratePropertySpreadsheet(args) {
        start(args)
    }

    static main(args) {
        new GeneratePropertySpreadsheet(args)
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
            propertyArgs.set("path", "C:\\Users\\s0041664\\Documents\\Projects\\DMT-DE\\Project\\Work\\translations\\Spreadsheets\\PropertySpreadsheets\\DMTDE\\")
    }

    def generateSpreadsheet() {
        def componentName = "DMT"

        def filePath = propertyArgs.get("path")

        def fileName = "test.xls"

        ExcelPropertyFile inSpreadsheet = ExcelPropertyFile.openUsingChooser("Pick a file", filePath)
        ExcelPropertyFile outSpreadsheet = ExcelPropertyFile.createNewSpreadsheetFromFileName(filePath+fileName, CREATE) // create

        ExcelPropertySheet testInSheet = inSpreadsheet.nextExcelPropertySheet()
        def headerRowNum = testInSheet.headerRowNum
        def keyList = testInSheet.keyList

        ExcelPropertySheet testOutSheet = ExcelPropertySheet.createPropertySheetInExcelPropertyFile(outSpreadsheet, testInSheet.sheetName)
        testOutSheet.addHeaderRow(headerRowNum, keyList)
        ExcelPropertyRow excelPropertyRow = testInSheet.nextRow()
        def valueMap = excelPropertyRow.getPropertyMap()
        testInSheet.addRow(1,valueMap)



//        while (inSpreadsheet.hasNextExcelPropertySheet()) {
//            ExcelPropertySheet inSheet = inSpreadsheet.nextExcelPropertySheet()
//            println "Row number: ${inSheet.headerRowNum} Values: ${inSheet.keyList}"
//            ExcelPropertySheet outSheet = ExcelPropertySheet.createPropertySheetInExcelPropertyFile(outSpreadsheet, inSheet.sheetName)
////            while (inSheet.hasNextRow()) {
////                ExcelPropertyRow excelPropertyRow = inSheet.nextRow()
////                def rowValues = excelPropertyRow.getPropertyMap()
////                outSheet.addRow(rowValues)
////            }
//        }
        outSpreadsheet.writeAndClose()
    }
}
