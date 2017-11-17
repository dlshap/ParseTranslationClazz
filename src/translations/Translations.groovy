package translations

import filemanagement.TranslationFile

/**
 * Created by s0041664 on 8/15/2017.
 */
class Translations {

    TranslationFile translationFile
    def transKeyMapList = []
    Iterator translationIterator

    def Translations(transFile) {
        transKeyMapList = transFile.getKeyMaps()
        translationIterator = transKeyMapList.iterator()
    }

    def getTranslation(keyName, keyValue) {
        def translations = getTranslations(keyName, keyValue)
        if (translations == null)
            null
        else
            // first translation
            translations[0]
    }

    def getTranslations(keyName, value) {
        List translations = transKeyMapList.findAll() { keyMap ->
            keyMap[keyName] == value
        }
        translations
    }

    def hasNext() {
        if (translationIterator != null)
            translationIterator.hasNext()
        else
            null
    }

    def next() {
        if (translationIterator != null)
            translationIterator.next()
        else
            null
    }

    def rewind() {
        translationIterator = transKeyMapList.iterator()
    }

    def size() {
        transKeyMapList.size()
    }
}

class Translation {

    def transKeyMap = [:]

    def Translation(transKeyMap) {
        this.transKeyMap = transKeyMap
    }

    def get(keyName) {
        transKeyMap[keyName]
    }
}