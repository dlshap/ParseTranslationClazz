package i18n

import logging.Log
import org.apache.commons.collections4.MapIterator

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
            "Spanish"        : "es"
    ]


    static getLanguageLabel(String languageName) {
        libraryLanguageLabels.get(languageName)
    }

    static getPropertiesLabel(String languageName) {
        propertiesFileLabels.get(languageName)
    }

    static ArrayList<String> getLanguageList() {
        libraryLanguageLabels.collect { it.key }
    }

    static isLanguageInList(String language) {
        boolean result = false
        if (propertiesFileLabels.get(language) != null) {
            result = true
        } else {
            Log.writeLine "ERROR: \"$language\" is not in language list"
        }
        result
    }
}
