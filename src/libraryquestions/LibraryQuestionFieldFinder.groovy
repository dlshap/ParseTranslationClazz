package libraryquestions

/**
 * Created by s0041664 on 8/14/2017.
 */
class LibraryQuestionFieldFinder {

    static languageName = "English"

    static LibraryLanguagePrefixes = [
            "Japanese" : "ja_JP",
            "English" : "en_US"
    ]

    static LibraryQuestionRegexes = [
        [excelExportFieldName: "Question Identifier", regex: /(?s)(.*en_US.*?title.*?:)(.*?)([,\]].*)/],
        [excelExportFieldName: "Question Identifier Translated", regex: /(?s)(.*ja_JP.*title.*?:)(.*?)([,\]].*)/],
        [excelExportFieldName: "BOM Fields", regex: /(?s)(.* new ClazzAttr.*name\s*:\s*?)(.*?)([,\]].*)/],
        [excelExportFieldName: "Questions and Answers Translated", regex: /(?s)(.*ja_JP.*txt.*?:)(.*?)(,.*)/],
        [excelExportFieldName: "Help Text Translated", regex: /(?s)(.*ja_JP.*helpText.*?:\s*?)(\S*?)(\].*\].*)/],
        [excelExportFieldName: "Description Text Translated", regex: /(?s)(.*ja_JP.*desc.*?:)(.*?)(]\s*]\s*]?\s*\).*)/]
    ]

//    static LibraryQuestionRegexes = [
//            [excelExportFieldName: "Question Identifier", regex: /(?s)(.*en_US.*?title.*?:)(.*?)([,\]].*)/],
//            [excelExportFieldName: "Question Identifier Translated", regex: /(?s)(.*${LibraryLanguagePrefixes.get(languageName)}.*title.*?:)(.*?)([,\]].*)/],
//            [excelExportFieldName: "BOM Fields", regex: /(?s)(.* new ClazzAttr.*name\s*:\s*?)(.*?)([,\]].*)/],
//            [excelExportFieldName: "Questions and Answers Translated", regex: /(?s)(.*\${LibraryLanguagePrefixes.get(languageName)}.*txt.*?:)(.*?)(,.*)/],
//            [excelExportFieldName: "Help Text Translated", regex: /(?s)(.*${LibraryLanguagePrefixes.get(languageName)}.*helpText.*?:\s*?)(\S*?)(\].*\].*)/],
//            [excelExportFieldName: "Description Text Translated", regex: /(?s)(.*${LibraryLanguagePrefixes.get(languageName)}.*desc.*?:)(.*?)(]\s*]\s*]?\s*\).*)/]
//    ]

    static lineContains(aLine, excelExportFieldName) {
        def findRegex = LibraryQuestionRegexes.find { map ->
            map.excelExportFieldName == excelExportFieldName
        }.regex
        def findMatch = aLine =~ findRegex
        (findMatch.count > 0)
    }

    static lineContains(aLine, aLanguage, excelExportFieldName) {
        /* set language name different from default */
        languageName = aLanguage
        lineContains(aLine, excelExportFieldName)
    }

    static findFieldInLibraryText(theText, excelExportFieldName) {
        def regex = LibraryQuestionRegexes.find { map ->
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

    static findFieldInLibraryText(theText, aLanguage, excelExportFieldName) {
        /* set language name different from default */
        languageName = aLanguage
        findFieldInLibraryText(theText, excelExportFieldName)
    }


        // this doesn't belong here...
    static getLibraryQuestionTranslators() {
        // return list of translator objects
        def libraryQuestionTranslators = []
        LibraryQuestionRegexes.each { libraryQuestionMap ->
            libraryQuestionTranslators << new LibraryQuestionTranslator(libraryQuestionMap)
        }
        libraryQuestionTranslators
    }
}


