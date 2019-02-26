package libraryquestions

class LibraryLanguageAdder {

    static final descRegex = /xx/
    static final localizationMapRegex = /xx/

    static addLanguage(String textBlock, String languageName) {
        def textBlockWithNewLanguage = textBlock
        textBlockWithNewLanguage = addLanguageToDescBlock(textBlockWithNewLanguage, languageName)
        textBlockWithNewLanguage = addLanguageToLocalizationMap(textBlockWithNewLanguage, languageName)
        textBlockWithNewLanguage
    }

    static addLanguageToDescBlock(String textBlock, String languageName) {
        def textBlockWithNewLanguage = textBlock
        if (!(textBlockContainsDescBlock(textBlockWithNewLanguage, languageName)))
            textBlockWithNewLanguage = insertNewLanguageIntoDescBlock(textBlockWithNewLanguage, languageName)
        textBlockWithNewLanguage
    }

    static textBlockContainsDescBlock(String textBlock, String languageName) {
        def textBlockWithNewLanguage = textBlock

        textBlockWithNewLanguage
    }

    static insertNewLanguageIntoDescBlock(String textBlock, String languageName) {
        def textBlockWithNewLanguage = textBlock
        textBlockWithNewLanguage
    }

    static addLanguageToLocalizationMap(String textBlock, String languageName) {
        def textBlockWithNewLanguage = textBlock
        textBlockWithNewLanguage
    }
}
