package LibraryQuestions

/**
 * Created by s0041664 on 8/14/2017.
 */
class LibraryQuestionMatchers {

    static LibQRegexes = [
            [transKeyField: "BOM Fields", regex: "(.*currentAttr.*name:.*?[\"'])(.*?)([\"'].*)"],
            [transKeyField: "Question Identifier Translated", regex: "(.*ja_JP.*title:.*?[\"'])(.*?)([\"'].*)"],
            [transKeyField: "Questions and Answers Translated", regex: "(.*ja_JP.*txt:.*?[\"'])(.*?)([\"'].*)"],
            [transKeyField: "Help Text Translated", regex: "(.*ja_JP.*helpText:.*?[\"'])(.*?)([\"'].*)"],
            [transKeyField: "Description Text Translated", regex: "(.*ja_JP.*desc:.*?[\"'])(.*?)([\"'].*)"]
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
    }

    static getFactoryMatchingValue(aLine, keyField) {
        // line to search for regex value for factory key
        def regex = LibQRegexes.find { map ->
            map.transKeyField == keyField
        }.regex
        def result = aLine =~ regex
        if (result.count > 0)
            result[0][2]
        else
            null
    }

    static getLibraryQuestionTranslators() {
        // return list of translator objects
        def libraryQuestionTranslators = []
        for (Map libQMap : LibQRegexes) {
            libraryQuestionTranslators << new LibraryQuestionTranslator(libQMap)
        }
        libraryQuestionTranslators
    }
}


