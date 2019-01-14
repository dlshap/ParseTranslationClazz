package libraryquestions

/**
 * Created by s0041664 on 8/14/2017.
 */
class LibraryQuestionFieldFinder {

    def languageName = "English"
    def libraryQuestionRegexes = []

    def LibraryQuestionFieldFinder() {
        buildLibraryQuestionRegexes()
    }

    def LibraryQuestionFieldFinder(languageName) {
        this.languageName = languageName
        buildLibraryQuestionRegexes(languageName)
    }

    static LibraryLanguageLabels = [
            "Japanese" : "ja_JP",
            "English" : "en_US"
    ]

    def buildLibraryQuestionRegexes(languageName) {
        def languageLabel = LibraryLanguageLabels[languageName]
        libraryQuestionRegexes = [
                [excelExportFieldName: "Question Identifier", regex: /(?s)(.*/ + languageLabel + /.*?title.*?:)(.*?)([,\]].*)/],
                [excelExportFieldName: "Question Identifier Translated", regex: /(?s)(.*/ + languageLabel + /.*title.*?:)(.*?)([,\]].*)/],
                [excelExportFieldName: "BOM Fields", regex: /(?s)(.* new ClazzAttr.*name\s*:\s*?)(.*?)([,\]].*)/],
                [excelExportFieldName: "Questions and Answers Translated", regex: /(?s)(.*/ + languageLabel + /.*txt.*?:)(.*?)(,.*)/],
                [excelExportFieldName: "Help Text Translated", regex: /(?s)(.*/ + languageLabel + /.*helpText.*?:\s*?)(\S*?)(\].*\].*)/],
                [excelExportFieldName: "Description Text Translated", regex: /(?s)(.*/ + languageLabel + /.*desc.*?:)(.*?)(]\s*]\s*]?\s*\).*)/]
        ]
//        libraryQuestionRegexes = [
//                [excelExportFieldName: "Question Identifier", regex: /(?s)(.*en_US.*?title.*?:)(.*?)([,\]].*)/],
//                [excelExportFieldName: "Question Identifier Translated", regex: /(?s)(.*ja_JP.*title.*?:)(.*?)([,\]].*)/],
//                [excelExportFieldName: "BOM Fields", regex: /(?s)(.* new ClazzAttr.*name\s*:\s*?)(.*?)([,\]].*)/],
//                [excelExportFieldName: "Questions and Answers Translated", regex: /(?s)(.*ja_JP.*txt.*?:)(.*?)(,.*)/],
//                [excelExportFieldName: "Help Text Translated", regex: /(?s)(.*ja_JP.*helpText.*?:\s*?)(\S*?)(\].*\].*)/],
//                [excelExportFieldName: "Description Text Translated", regex: /(?s)(.*ja_JP.*desc.*?:)(.*?)(]\s*]\s*]?\s*\).*)/]
//        ]
    }

    def lineContains(aLine, excelExportFieldName) {
         def findRegex = libraryQuestionRegexes.find { map ->
            map.get("excelExportFieldName") == excelExportFieldName
        }.get("regex")
        def findMatch = aLine =~ findRegex
        (findMatch.count > 0)
    }

    def lineContains(aLine, aLanguage, excelExportFieldName) {
        /* set language name different from default */
        languageName = aLanguage
        lineContains(aLine, excelExportFieldName)
    }

    def findFieldInLibraryText(theText, excelExportFieldName) {
        def regex = libraryQuestionRegexes.find { map ->
            map.excelExportFieldName == excelExportFieldName
        }.regex
        def result = theText =~ regex
        def returnVal = null
        if (result.count > 0) {
            returnVal = result[0][2].trim()
            returnVal = returnVal.replaceAll(/^['"]|['"]$/, "")     // trim leading and trailing quotes
        }
        returnVal
    }

    def findFieldInLibraryText(theText, aLanguage, excelExportFieldName) {
        /* set language name different from default */
        languageName = aLanguage
        findFieldInLibraryText(theText, excelExportFieldName)
    }


        // this doesn't belong here...
    def getLibraryQuestionTranslators() {
        // return list of translator objects
        def libraryQuestionTranslators = []
        libraryQuestionRegexes.each { libraryQuestionMap ->
            libraryQuestionTranslators << new LibraryQuestionTranslator(libraryQuestionMap)
        }
        libraryQuestionTranslators
    }
}


