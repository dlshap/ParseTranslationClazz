package libraryquestions

import i18n.LanguageLabels

import static i18n.LanguageLabels.getLanguageLabel

/**
 * Created by s0041664 on 8/14/2017.
 */
class LibraryQuestionFieldParser {

    def languageName = "English"
    def libraryQuestionRegexes = []

    LibraryQuestionFieldParser(languageName) {
        this.languageName = languageName
        buildLibraryQuestionRegexes(getLanguageLabel(languageName))
    }

    def buildLibraryQuestionRegexes(languageLabel) {
        libraryQuestionRegexes = [
                [fieldName: "Question Identifier", regex: /(?s)(.*en_US.*?title.*?:)(.*?)([,\]].*)/],
                [fieldName: "Question Identifier Translated", regex: /(?s)(.*(?:localizationMap|i18n|quests.push).*/ + languageLabel + /.*?title\s*:\s*)(.*?)([,\]].*)/],
                [fieldName: "BOM Fields", regex: /(?s)(.* new ClazzAttr.*name\s*:\s*?)(.*?)([,\]].*)/],
                [fieldName: "Questions and Answers Translated", regex: /(?s)(.*(?:localizationMap|i18n|quests.push).*/ + languageLabel + /.*?txt\s*:\s*)(.*?)(,.*)/],
                [fieldName: "Help Text Translated", regex: /(?s)(.*?(?:localizationMap|i18n|quests.push).*/ + languageLabel + /.*?helpText\s*:\s*)(.*?)([,\]].*)/],
                [fieldName: "Description Text Translated", regex: /(?s)(.*/ + languageLabel + /.*?desc\s*:\s*)(.*?)(].*)/]
        ]
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
        returnVal == "null" ? null : returnVal
    }
}


