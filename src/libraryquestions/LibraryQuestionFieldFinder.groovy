package libraryquestions

/**
 * Created by s0041664 on 8/14/2017.
 */
class LibraryQuestionFieldFinder {

    static LibraryQuestionRegexes = [
        [libraryQuestionFieldName: "Question Identifier", regex: /(?s)(.*en_US.*?title.*?:)(.*?)([,\]].*)/],
        [libraryQuestionFieldName: "Question Identifier Translated", regex: /(?s)(.*ja_JP.*title.*?:)(.*?)([,\]].*)/],
        [libraryQuestionFieldName: "BOM Fields", regex: /(?s)(.* new ClazzAttr.*name\s*:\s*?)(.*?)([,\]].*)/],
        [libraryQuestionFieldName: "Questions and Answers Translated", regex: /(?s)(.*ja_JP.*txt.*?:)(.*?)(,.*)/],
        [libraryQuestionFieldName: "Help Text Translated", regex: /(?s)(.*ja_JP.*helpText.*?:\s*?)(\S*?)(\].*\].*)/],
        [libraryQuestionFieldName: "Description Text Translated", regex: /(?s)(.*ja_JP.*desc.*?:)(.*?)(]\s*]\s*]?\s*\).*)/]
    ]

    static lineContains(aLine, libraryQuestionFieldName) {
        def findRegex = LibraryQuestionRegexes.find { map ->
            map.libraryQuestionFieldName == libraryQuestionFieldName
        }.regex
        def findMatch = aLine =~ findRegex
        (findMatch.count > 0)
    }

    static findFieldInLibraryText(theText, libraryQuestionFieldName) {
        def regex = LibraryQuestionRegexes.find { map ->
            map.libraryQuestionFieldName == libraryQuestionFieldName
        }.regex
        def result = theText =~ regex
        def returnVal = null
        if (result.count > 0) {
            returnVal = result[0][2].trim()
            returnVal = returnVal.replaceAll(/^['"]|['"]$/, "")     // trim leading and trailing quotes
        }
        returnVal
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


