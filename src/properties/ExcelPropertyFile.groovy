package properties

import excelfilemanagement.ExcelFile
import org.apache.poi.ss.usermodel.Sheet

class ExcelPropertyFile extends ExcelFile {

    Iterator sheetIterator

    ExcelPropertyFile() {
    }

    static openExcelPropertyFileUsingChooser(prompt, filePath) {
        ExcelPropertyFile excelPropertyFile = new ExcelPropertyFile()
        excelPropertyFile.chooseFile(prompt, filePath)
        excelPropertyFile.buildSheetIterator()
        excelPropertyFile.fileName == null ? null : excelPropertyFile
    }

    private buildSheetIterator() {
        if (this.fileName != null)
            this.sheetIterator = workbook.sheetIterator()
    }

    /*************** public methods ****************/
    def getPropertySheet(String sheetName) {
        this.getPropertySheetWithHeaderLabelsInHeaderRowNum(sheetName, 0)     // usually header in row 0
    }

    def getPropertySheetWithHeaderLabelsInHeaderRowNum(String sheetName, int headerRowNum) {
        new ExcelPropertySheet(this, sheetName, headerRowNum)
    }

    def hasNextExcelPropertySheet() {
        sheetIterator.hasNext()
    }

    def nextExcelPropertySheet() {
        Sheet sheet = sheetIterator.next()
        new ExcelPropertySheet(sheet)
    }
}