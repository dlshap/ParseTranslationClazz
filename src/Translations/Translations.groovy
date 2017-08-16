package Translations

/**
 * Created by s0041664 on 8/15/2017.
 */
class Translations {
    def transKeyMaps = []

    def Translations(keyFile) {
        transKeyMaps = keyFile.getKeyMaps()
    }

    def getTranslation(keyName, keyValue) {
        def translation = null
        def transKeyMap = transKeyMaps.find { map ->
            map[keyName] == keyValue
        }
        // if successful getting a map, create a translation object containing the map
        if (transKeyMap) {
            translation = new Translation(transKeyMap)
        }
        translation
    }
}