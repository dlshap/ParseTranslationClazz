package properties

import excelfilemanagement.SpreadsheetArgs

class PropertyArgs extends SpreadsheetArgs{

    String propertyFilePath

    PropertyArgs(args) {
        super(args)
    }

    def getConfigValues() {
        super.getConfigValues()
        propertyFilePath = basePath
        spreadsheetPath = basePath + config.get("properties.spreadsheet.relative.path")
    }
}
