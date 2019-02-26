package i18n

class LanguageLabels {

    static final def libraryLanguageLabels = [
            "Japanese"       : "ja_JP",
            "English"        : "en_US",
            "French-Canadian": "fr_CA",
            "Czech"          : "cs_CZ"
    ]

    static getLanguageLabel(String languageName) {
        libraryLanguageLabels.get(languageName)
    }
}
