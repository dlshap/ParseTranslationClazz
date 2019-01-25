package translations

import libraryquestions.LibraryTextBlock

class Translation {

    def transKeyMap = [:]
//    TranslationFieldKeys translationFieldKeys

    Translation(transKeyMap) {
        this.transKeyMap = transKeyMap
    }

//    def Translation(transKeyMap, translationFieldKeys) {
//        this.transKeyMap = transKeyMap
//        this.translationFieldKeys = translationFieldKeys
//    }

    def get(keyName) {
        def value = transKeyMap.get(keyName)
        value == "" ? null : value
    }
}
