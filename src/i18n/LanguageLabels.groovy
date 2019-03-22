package i18n

class LanguageLabels {

    static final def propertiesFileLabels = [
            "Japanese"       : "ja",
            "English"        : "en_US",
            "French-Canadian": "fr_CA",
            "Czech"          : "cs_CZ",
            "Slovak"         : "sv",
            "Spanish"        : "es"
    ]

    static final def libraryLanguageLabels = [
            "Japanese"       : "ja_JP",
            "English"        : "en_US",
            "French-Canadian": "fr_CA",
            "Czech"          : "cs_CZ",
            "Slovak"         : "sv",
            "Spanish"        : "es",
    ]

    static getLanguageLabel(String languageName) {
        libraryLanguageLabels.get(languageName)
    }

    static getPropertiesLabel(String languageName) {
        propertiesFileLabels.get(languageName)
    }

    static getLanguageList() {
        libraryLanguageLabels.collect {it.key}
    }

    static isLanguageInList(String language) {
        propertiesFileLabels.get(language) != null
    }
}
