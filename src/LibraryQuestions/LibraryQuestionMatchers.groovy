package LibraryQuestions

/**
 * Created by s0041664 on 8/14/2017.
 */
class LibraryQuestionMatchers {

    static LibQRegexes = [
        [transKeyField: "BOM Fields", regex: /(?s)(.* new ClazzAttr.*name\s*:\s*?)(.*?)([,\]].*)/],
        [transKeyField: "Question Identifier Translated", regex: /(?s)(.*ja_JP.*title.*?:)(.*?)([,\]].*)/],
        [transKeyField: "Questions and Answers Translated", regex: /(?s)(.*ja_JP.*txt.*?:)(.*?)(,.*)/],
        [transKeyField: "Help Text Translated", regex: /(?s)(.*ja_JP.*helpText.*?:\s*?)(\S*?)(\].*\].*)/],
        [transKeyField: "Description Text Translated", regex: /(?s)(.*ja_JP.*desc.*?:)(.*?)(]\s*]\s*]?\s*\).*)/]
    ]

    static lineContains(aLine, keyField) {
        def regex = LibQRegexes.find { map ->
            map.transKeyField == keyField
        }.regex
        def result = aLine =~ regex
        (result.count > 0)
    }

    static getValue(keyValue, keyField) {
        def value = LibQRegexes.find { map ->
            map.transKeyField == keyValue
        }.getAt(keyField)
        value
    }

    static getFactoryMatchingValue(theText, keyField) {
        // get value in factory text for keyField in translation file
        def regex = LibQRegexes.find { map ->
            map.transKeyField == keyField
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
        LibQRegexes.each { libQMap ->
            libraryQuestionTranslators << new LibraryQuestionTranslator(libQMap)
        }
        libraryQuestionTranslators
    }
}


