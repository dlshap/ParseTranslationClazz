package translations

import excelfilemanagement.ExcelFile
import properties.ExcelPropertyFile
import properties.ExcelPropertySheet

/**
 * Created by s0041664 on 8/15/2017.
 */
class Translations {
    // Translations: collection of Translation objects imported from Translation Spreadsheet export file
    ArrayList<Translation> translations = []
    Iterator translationIterator

    Translations() {
    }

    static createLibraryTranslationsFromExcelSheet(ExcelPropertySheet excelPropertySheet) {
        Translations translations = new Translations()
        translations.buildTranslationsFromExcelSheet(excelPropertySheet)
        translations
    }

    def buildTranslationsFromExcelSheet(ExcelPropertySheet excelPropertySheet) {
        def keyMaps = excelPropertySheet.getKeyMaps()
        buildTranslationsFromKeyMaps(keyMaps)
    }

    static createTranslationsFromExcelPropertiesFile(ExcelFile excelFile, String componentName) {
        Translations translations = new Translations()
        translations.buildTranslationsFromExcelFileForComponent(excelFile, componentName)
        translations
    }

    def buildTranslationsFromKeyMaps(keyMaps) {
        keyMaps.each {
            Translation translation = new Translation(it)
            if (translation != null)
                this.translations.add(translation)
        }
        this.translationIterator = translations.iterator()
    }

    def buildTranslationsFromExcelFileForComponent(ExcelPropertyFile excelFile, String componentName) {
        ExcelPropertySheet excelPropertySheet = excelFile.getPropertySheet(componentName)
        while (excelPropertySheet.hasNextRow()) {
            def propertyMap = excelPropertySheet.nextExcelPropertyRow().getPropertyMap()
            Translation translation = new Translation(propertyMap)
            if (translation != null)
                this.translations.add(translation)
        }
        this.translationIterator = translations.iterator()
    }


    def getTranslation(keyName, keyValue) {
        /* used for properties files (single key) */
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

