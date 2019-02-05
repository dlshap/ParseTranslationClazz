package translations

import excelExports.ExcelExport
import excelfilemanagement.ExcelFile
import filemanagement.KeyFile

/**
 * Created by s0041664 on 8/15/2017.
 */
class Translations {
    // Translations: collection of Translation objects imported from Translation Spreadsheet export file
    ArrayList<Translation> translations = []
    Iterator translationIterator

    Translations(KeyFile translationExportFile) {
        //old constructor (deprecate)
        buildTranslations(translationExportFile)
    }

    def buildTranslations(KeyFile translationExportFile) {
        def keyMaps = translationExportFile.getKeyMaps()
        keyMaps.each {
            Translation translation = new Translation(it)
            if (translation != null)
                this.translations.add(translation)
        }
        this.translationIterator = translations.iterator()
    }

    static createTranslationsFromExcelExport(ExcelExport excelExport) {
        //new factory for ExcelExports (the object not the file...replaces old constructor)
        KeyFile translationExportFile = excelExport.excelExportFile
        Translations translations = new Translations(translationExportFile)
        translations
    }

    static createTranslationsFromExcelPropertiesFile(ExcelFile excelFile) {

    }

    def getTranslation(keyName, keyValue) {
        /* used for Properties files (single key) */
//        def keyMap = ["$keyName":keyValue]
//        TranslationFieldKeys translationFieldKeys = new TranslationFieldKeys(keyMap)
        def translations = getTranslations(keyName, keyValue)
        (translations == null) ? null : translations[0]
    }

    def getTranslations(keyName, value) {
        List foundTranslations = translations.findAll() { translation ->
            translation.get(keyName) == value
        }
        (foundTranslations.size() == 0) ? null : foundTranslations
    }

    def getTranslationsFromKeyFields(TranslationFieldKeys translationFieldKeys) {
        /*
        keys = map of key/value pairs that should be matched against corresponding pairs in translations
        returns all maps in translations that match on all keys
         */
        def keyMap = translationFieldKeys.getKeys()
        def translations = this.translations
        keyMap.each { k, v ->
            translations = translations.findAll() { Translation it ->
                it.get(k) == keyMap.get(k)
//                it.get(k).trim().toLowerCase() == keyMap.get(k).trim().toLowerCase()
            }
        }
        translations
    }

    def getTranslationsFromKeyFields(keyName, value) {
        List translations = translations.findAll() { keyMap ->
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

    def size() {
        translations.size()
    }
}

