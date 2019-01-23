class TestClass {

    static main(args) {
        def test = new TestClass()
        test.start()
    }

    def libraryQuestionRegexes

    def buildLibraryQuestionRegexes(languageLabel) {
        this.libraryQuestionRegexes = [
                [fieldName: "Question Identifier", regex: /(?s)(.*en_US.*?title.*?:)(.*?)([,\]].*)/],
                [fieldName: "Question Identifier Translated", regex: /(?s)(.*/ + languageLabel + /.*title.*?:)(.*?)([,\]].*)/],
                [fieldName: "BOM Fields", regex: /(?s)(.* new ClazzAttr.*name\s*:\s*?)(.*?)([,\]].*)/],
                [fieldName: "Questions and Answers Translated", regex: /(?s)(.*/ + languageLabel + /.*txt.*?:)(.*?)(,.*)/],
                [fieldName: "Help Text Translated", regex: /(?s)(.*/ + languageLabel + /.*helpText.*?:\s*?)(\S*?)(\].*\].*)/],
                [fieldName: "Description Text Translated", regex: /(?s)(.*/ + languageLabel + /.*desc.*?:)(.*?)(]\s*]\s*]?\s*\).*)/]
        ]
    }

    def lineContains(aLine, fieldName) {
        if (libraryQuestionRegexes.find { it.fieldName == fieldName } == null)
            return false
        else {
            def findRegex = libraryQuestionRegexes.find { map ->
                map.get("fieldName") == fieldName
            }.get("regex")
            def findMatch = aLine =~ findRegex
            (findMatch.count > 0)
        }
    }

    def start() {
        this.buildLibraryQuestionRegexes("ja_JP")
//        def x = libraryQuestionRegexes.find { it.fieldName == "Question Identifiers" }
//        println x.size()
//        def x = libraryQuestionRegexes.collect { it.get("fieldName") == "Question Identifiers"}
//        println x.toString().contains("true")

        println lineContains("Testing en_US title: testing]xxx", "Question Identifiers")
    }

}
