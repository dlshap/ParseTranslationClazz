package translations
/**
 * Created by s0041664 on 8/15/2017.
 */
class Translations {
    // Translations: collection of Translation.groovy objects imported from Translation.groovy Spreadsheet export file
    def transKeyMapList = []
    Iterator translationIterator

    def Translations(translationExportFile) {
        transKeyMapList = translationExportFile.getKeyMaps()
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

    def getTranslation(TranslationFieldKeys keys) {

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