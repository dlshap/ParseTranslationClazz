package translations

import libraryquestions.LibraryTextBlock

class Translation {

    def transKeyMap = [:]
    TranslationFieldKeys translationFieldKeys   // keys used to pick this translation (used for library question error messages)

    Translation(transKeyMap) {
        this.transKeyMap = transKeyMap
    }

    def get(keyName) {
        def value = transKeyMap.get(keyName)
        value == "" ? null : value
    }
}
