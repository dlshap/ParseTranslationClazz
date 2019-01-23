package translations

import excelExports.ExcelExport
import filemanagement.KeyFile

/**
 * Created by s0041664 on 8/15/2017.
 */
class Translations {
    // Translations: collection of Translation objects imported from Translation Spreadsheet export file
    def transKeyMapList = []
    Iterator translationIterator

    Translations(KeyFile translationExportFile) {
        //old constructor (deprecate)
        buildTranslations(translationExportFile)
    }

    Translations(ExcelExport excelExport) {
        //new constructor (used by LibraryFactories)
        KeyFile translationExportFile = excelExport.excelExportFile
        buildTranslations(translationExportFile)
    }

    def buildTranslations(KeyFile translationExportFile) {
        this.transKeyMapList = translationExportFile.getKeyMaps()
        this.translationIterator = transKeyMapList.iterator()
    }

    def getTranslation(keyName, keyValue) {
        def translations = getTranslations(keyName, keyValue)
        if (translations == null)
            null
        else
        // first translation
            translations[0]
    }

    def getTranslations(TranslationFieldKeys translationFieldKeys) {
        /*
        keys = map of key/value pairs that should be matched against corresponding pairs in transKeyMapList
        returns all maps in transKeyMapList that match on all keys
         */
        def keyMap = translationFieldKeys.getKeys()
        def translations = transKeyMapList
        keyMap.each { k, v ->
            translations = translations.findAll() {
                it.get(k).trim().toLowerCase() == keyMap.get(k).trim().toLowerCase()
            }
        }
        translations
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

