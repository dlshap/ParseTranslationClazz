package libraryquestions

/**
 * Created by s0041664 on 8/14/2017.
 */
class LibraryQuestionMatchers {

    static LibraryQuestionRegexes = [
        [excelColumnName: "Question Identifier", regex: /(?s)(.*en_US.*?title.*?:)(.*?)([,\]].*)/],
        [excelColumnName: "Question Identifier Translated", regex: /(?s)(.*ja_JP.*title.*?:)(.*?)([,\]].*)/],
        [excelColumnName: "BOM Fields", regex: /(?s)(.* new ClazzAttr.*name\s*:\s*?)(.*?)([,\]].*)/],
        [excelColumnName: "Questions and Answers Translated", regex: /(?s)(.*ja_JP.*txt.*?:)(.*?)(,.*)/],
        [excelColumnName: "Help Text Translated", regex: /(?s)(.*ja_JP.*helpText.*?:\s*?)(\S*?)(\].*\].*)/],
        [excelColumnName: "Description Text Translated", regex: /(?s)(.*ja_JP.*desc.*?:)(.*?)(]\s*]\s*]?\s*\).*)/]
    ]

    static lineContains(aLine, keyField) {
        def regex = LibraryQuestionRegexes.find { map ->
            map.excelColumnName == keyField
        }.regex
        def result = aLine =~ regex
        (result.count > 0)
    }

    static getValue(excelColumnName, matcherKey ) {
        def value = LibraryQuestionRegexes.find { map ->
            map.excelColumnName == excelColumnName
        }.getAt(matcherKey)
        value
    }

    static getFactoryMatchingValue(theText, keyField) {
        // get value in factory text for keyField in translation file
        def regex = LibraryQuestionRegexes.find { map ->
            map.excelColumnName == keyField
        }.regex
        def result = theText =~ regex
        def returnVal = null
        if (result.count > 0) {
            returnVal = result[0][2].trim()
            returnVal = returnVal.replaceAll(/^['"]|['"]$/, "")     // trim leading and trailing quotes
        }
        returnVal
    }

    static getLibraryQuestionTranslators() {
        // return list of translator objects
        def libraryQuestionTranslators = []
        LibraryQuestionRegexes.each { libraryQuestionMap ->
            libraryQuestionTranslators << new LibraryQuestionTranslator(libraryQuestionMap)
        }
        libraryQuestionTranslators
    }
}


