package libraryquestions

import i18n.LanguageLabels

class LibraryLanguageAdder {

    static descRegex, addDescRegex
    static localizationMapRegex, addLocalizationMapRegex
    static languageLabel

    static String addLanguage(String textBlock, String languageName) {
        buildRegexFromLanguage(languageName)
        def textBlockWithNewLanguage = textBlock
        textBlockWithNewLanguage = addLanguageToDescBlock(textBlockWithNewLanguage, languageName)
        textBlockWithNewLanguage = addLanguageToLocalizationMapBlock(textBlockWithNewLanguage, languageName)
        textBlockWithNewLanguage
    }

    static buildRegexFromLanguage(String languageName) {
        languageLabel = LanguageLabels.getLanguageLabel(languageName)
        descRegex = /(?s)(.*?localizedAttributesMap.*?/ + languageLabel + /\s*:.*?)(\).*)/
        addDescRegex = /(?s)(.*?localizedAttributesMap.*?\])(\).*?currentClazz.*)/
        localizationMapRegex = /(?s)(.*?(?:i18n|localizationMap|quests.push).*?/ + languageLabel + /\s*:.*)/
        addLocalizationMapRegex = /(?s)(.*?(?:i18n|localizationMap|quests.push).*?\])(\s*\].*)/
    }

    static String addLanguageToDescBlock(String textBlock, String languageName) {
        def textBlockWithNewLanguage = textBlock
        if (!(textBlockContainsDescBlock(textBlockWithNewLanguage, languageName)))
            textBlockWithNewLanguage = insertNewLanguageIntoDescBlock(textBlockWithNewLanguage, languageName)
        textBlockWithNewLanguage
    }

    static Boolean textBlockContainsDescBlock(String textBlock, String languageName) {
        def textBlockWithNewLanguage = textBlock
        def found = ((textBlockWithNewLanguage =~ descRegex).size() >= 1)
        found
    }

    static String insertNewLanguageIntoDescBlock(String textBlock, String languageName) {
        def textBlockWithNewLanguage = textBlock
        def findInsertionPointForNewLanguage = textBlockWithNewLanguage =~ addDescRegex
        if (findInsertionPointForNewLanguage.size() >= 1) {
            textBlockWithNewLanguage = findInsertionPointForNewLanguage[0][1] +
                    ",\n\t\t\t $languageLabel: [desc: ' ']\n" +
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
                    ",\n\t\t\t $languageLabel: [txt: ' ',\n\t\t\t\t\ttitle: ' ',\n\t\t\t\t\thelpText: ' '\n\t\t\t\t]\n" +
                    findInsertionPointForNewLanguage[0][2]
        }
        textBlockWithNewLanguage
    }

}
