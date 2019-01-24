package libraryquestions

/**
 * Created by s0041664 on 8/14/2017.
 */
class LibraryQuestionFieldFinder {

    def languageName = "English"
    def libraryQuestionRegexes = []

    LibraryQuestionFieldFinder(languageName) {
        this.languageName = languageName
        buildLibraryQuestionRegexes(libraryLanguageLabels[languageName])
    }

    def libraryLanguageLabels = [
            "Japanese": "ja_JP",
            "English" : "en_US"
    ]

    def buildLibraryQuestionRegexes(languageLabel) {
        libraryQuestionRegexes = [
                [fieldName: "Question Identifier", regex: /(?s)(.*en_US.*?title.*?:)(.*?)([,\]].*)/],
                [fieldName: "Question Identifier Translated", regex: /(?s)(.*/ + languageLabel + /.*title.*?:)(.*?)([,\]].*)/],
                [fieldName: "BOM Fields", regex: /(?s)(.* new ClazzAttr.*name\s*:\s*?)(.*?)([,\]].*)/],
                [fieldName: "Questions and Answers Translated", regex: /(?s)(.*/ + languageLabel + /.*txt.*?:)(.*?)(,.*)/],
                [fieldName: "Help Text Translated", regex: /(?s)(.*/ + languageLabel + /.*helpText.*?:\s*?)(\S*?)(\].*\].*)/],
                [fieldName: "Description Text Translated", regex: /(?s)(.*/ + languageLabel + /.*desc.*?:)(.*?)(]\s*]\s*]?\s*\).*)/]
        ]
    }

    def lineContains(aLine, fieldName) {
        if (libraryQuestionRegexes.find { it.fieldName == fieldName } == null)
            return false
        else {
            def findRegex = libraryQuestionRegexes.find { map ->
                map.get("fieldName") == fieldName
            }.get("regex")
            def findMatch = aLine =~ findRegex
            (findMatch.count > 0)
        }
    }

    def findFieldInLibraryText(theText, fieldName) {
        def returnVal = null
        if (libraryQuestionRegexes.find { it.fieldName == fieldName } != null) {
            def regex = libraryQuestionRegexes.find { map ->
                map.fieldName == fieldName
            }.regex
            def result = theText =~ regex
            if (result.count > 0) {
                returnVal = result[0][2].trim()
                returnVal = returnVal.replaceAll(/^['"]|['"]$/, "")     // trim leading and trailing quotes
            }
        }
        returnVal
    }
}


