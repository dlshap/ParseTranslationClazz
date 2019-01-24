package translations

import libraryquestions.LibraryTextBlock

class Translation {

    def transKeyMap = [:]
    TranslationFieldKeys translationFieldKeys

    def Translation(transKeyMap, translationFieldKeys) {
        this.transKeyMap = transKeyMap
        this.translationFieldKeys = translationFieldKeys
    }

    def get(keyName) {
        transKeyMap[keyName]
    }
}
