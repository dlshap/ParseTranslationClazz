package libraryquestions

class LibraryColumns {

    static final libraryTranslatedColumns = [
            "Question Identifier" : "Question Identifier Translated",
            "Questions and Answers" : "Questions and Answers Translated",
            "Help Text" : "Help Text Translated",
            "Description Text" : "Description Text Translated"
    ]

    static final libraryKeyColumns = [
            "Question Identifier",
            "BOM Fields"
    ]

    static getLibraryTranslatedColumnName(String libraryColumnName) {
        libraryTranslatedColumns[libraryColumnName]
    }
}
