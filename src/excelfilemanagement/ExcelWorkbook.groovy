package excelfilemanagement

import org.apache.poi.ss.usermodel.Workbook

class ExcelWorkbook {

    Workbook workbook

    def workbookStream

    def write() {
        workbook.write(workbookStream)
    }

    def close() {
        workbookStream.close()
    }

    def create(String sheetName) {
        workbook.createSheet(sheetName)
    }
}
