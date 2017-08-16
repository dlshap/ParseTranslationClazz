package LibraryQuestions

/**
 * Created by s0041664 on 8/14/2017.
 */
class LibraryQuestionTranslator {
    /*
    Map format:
        [transKeyField: "BOM Fields", regex: "(.*currentAttr.*name:.*?[\"'])(.*?)([\"'].*)"],...
    */
    def libQMap = [:]

    def LibraryQuestionTranslator(libQMap) {
        this.libQMap = libQMap
    }

    def getValue(keyname) {
        libQMap[keyname]
    }

    def translate(aLine, translationValue) {
        def regex = this.getValue("regex")
        def result = aLine =~ regex
        if (result.count ==0)
            aLine
        else {
            result[0][1] + translationValue + result[0][3]
        }
    }
}
