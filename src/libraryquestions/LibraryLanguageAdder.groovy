package libraryquestions

import i18n.LanguageLabels

class LibraryLanguageAdder {

    static findDescRegex, addDescRegex
    static localizationMapRegex, addLocalizationMapRegex
    static languageLabel

    static String addLanguage(String textBlock, String languageName) {
        buildRegexFromLanguage(languageName)
        String textBlockWithNewLanguage = textBlock
        textBlockWithNewLanguage = addLanguageToDescBlock(textBlockWithNewLanguage, languageName)
        textBlockWithNewLanguage = addLanguageToLocalizationMapBlock(textBlockWithNewLanguage, languageName)
        textBlockWithNewLanguage
    }

    static buildRegexFromLanguage(String languageName) {
        languageLabel = LanguageLabels.getLanguageLabel(languageName)
        findDescRegex = /(?s)(.*?localizedAttributesMap.*?/ + languageLabel + /\s*:.*?)(\).*)/
        addDescRegex = /(?s)(.*?localizedAttributesMap.*?\])\s*(\]\s*\).*)/
        localizationMapRegex = /(?s)(.*?(?:i18n|localizationMap|quests.push).*?/ + languageLabel + /\s*:.*)/
        addLocalizationMapRegex = /(?s)(.*?(?:i18n|localizationMap|quests.push).*?\])\s*(\].*)/
    }

    static String addLanguageToDescBlock(String textBlock, String languageName) {
        String textBlockWithNewLanguage = textBlock
        Boolean foundLanguage = textBlockContainsDescBlock(textBlockWithNewLanguage, languageName)
        if (!foundLanguage) {
            textBlockWithNewLanguage = insertNewLanguageIntoDescBlock(textBlockWithNewLanguage, languageName)
        }
        textBlockWithNewLanguage
    }

    static Boolean textBlockContainsDescBlock(String textBlock, String languageName) {
        String textBlockWithNewLanguage = textBlock
        Boolean found = ((textBlockWithNewLanguage =~ findDescRegex).size() >= 1)
        found
    }

    static String insertNewLanguageIntoDescBlock(String textBlock, String languageName) {
        String textBlockWithNewLanguage = textBlock
        def findInsertionPointForNewLanguage = textBlockWithNewLanguage =~ addDescRegex
        if (findInsertionPointForNewLanguage.size() >= 1) {
            textBlockWithNewLanguage = findInsertionPointForNewLanguage[0][1] +
                    ",\n\t\t\t $languageLabel: [desc: ' ']" +
                    findInsertionPointForNewLanguage[0][2]
        }
        textBlockWithNewLanguage
    }

    static String addLanguageToLocalizationMapBlock(String textBlock, String languageName) {
        def textBlockWithNewLanguage = textBlock
        if (!(textBlockContainsLocalizationMapBlock(textBlockWithNewLanguage, languageName)))
            textBlockWithNewLanguage = insertNewLanguageIntoLocalizationMapBlock(textBlockWithNewLanguage, languageName)
        textBlockWithNewLanguage
    }

    static Boolean textBlockContainsLocalizationMapBlock(String textBlock, String languageName) {
        def textBlockWithNewLanguage = textBlock
        def found = ((textBlockWithNewLanguage =~ localizationMapRegex).size() >= 1)
        found
    }

    static String insertNewLanguageIntoLocalizationMapBlock(String textBlock, String languageName) {
        def textBlockWithNewLanguage = textBlock
        def findInsertionPointForNewLanguage = textBlockWithNewLanguage =~ addLocalizationMapRegex
        if (findInsertionPointForNewLanguage.size() >= 1) {
            textBlockWithNewLanguage = findInsertionPointForNewLanguage[0][1] +
                    ",\n\t\t\t $languageLabel: [txt: ' ',\n\t\t\t\t\ttitle: ' ',\n\t\t\t\t\thelpText: ' '\n\t\t\t\t] " +
                    findInsertionPointForNewLanguage[0][2]
        }
        textBlockWithNewLanguage
    }

}
