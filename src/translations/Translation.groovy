package translations

import libraryquestions.LibraryTextBlock

class Translation {

    def transKeyMap = [:]

    def Translation(transKeyMap) {
        this.transKeyMap = transKeyMap
    }

    def get(keyName) {
        transKeyMap[keyName]
    }
}
