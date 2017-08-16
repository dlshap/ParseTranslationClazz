package Translations

/**
 * Created by s0041664 on 8/15/2017.
 */
class Translation {

    def transKeyMap = [:]

    def Translation(transKeyMap) {
        this.transKeyMap = transKeyMap
    }

    def getTranslationValue(keyName) {
        transKeyMap[keyName]
    }
}
