package properties

import excelfilemanagement.ExcelFile
import excelfilemanagement.ExcelWorkbookForOutput


class ExcelPropertyFile extends ExcelFile {

    ExcelPropertyFile() {
    }

    static openExcelProertyFileUsingChooser(prompt, filePath) {
        ExcelPropertyFile excelPropertyFile = new ExcelPropertyFile()
        excelPropertyFile.chooseFile(prompt, filePath)
    }

    /*************** public methods ****************/
    def getPropertySheet(String sheetName) {
        this.getPropertySheetWithHeaderLabelsInHeaderRowNum(sheetName, 0)     // usually header in row 0
    }

    def getPropertySheetWithHeaderLabelsInHeaderRowNum(String sheetName, int headerRowNum) {
        new ExcelPropertySheet(this, sheetName, headerRowNum)
    }
}