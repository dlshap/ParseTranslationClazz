import com.google.common.base.Splitter


/**
 * Created by s0041664 on 8/18/2017.
 */
class TestParse {


    static main(args) {
        def line = """
package com.rgatp.ng.dmt.model.util

import com.rgatp.ng.bom.disclosures.lifestyle.AviationExperience
import com.rgatp.ng.dmt.model.dto.Clazz
import com.rgatp.ng.dmt.model.dto.ClazzAttr

class AviationExperienceClassFactory extends LifeStyleActivityClassFactory {

    AviationExperienceClassFactory() {
        super(AviationExperience)
    }

    Clazz createClazzLibraryModel() {
        Clazz currentClazz
        ClazzAttr currentAttr
        String defaultQuestion
        String helpText
        String description
        Map<String, Map<String, String>> localizationMap

        currentClazz = new Clazz([ name: disclosureClass.simpleName, pkg: disclosureClass.package.name ])

        Clazz parent = super.createClazzLibraryModel()
        currentClazz.attrs.addAll(parent.attrs)

        description = 'Commercial, Military, Private'
        currentAttr = new ClazzAttr([name: 'aviationTypes', type: 'String[]', localizedAttributesMap:
            ["en_US": [desc: description], "ja_JP": [desc: '商用、軍隊、プライベート等']]] )
        currentClazz.attrs.push(currentAttr)
        defaultQuestion = 'Which of the following best describes your flying activities?  (Please check all that apply.)'
        localizationMap = ["en_US": [txt: defaultQuestion, title: 'Aviation Type', helpText: null],
                           "ja_JP": [txt: 'あなたの飛行活動は以下のいずれにあてはまりますか。（該当するもの全てをお選びください）',
                                     title: '飛行種類', helpText: null]]
        currentAttr.quests.push ( buildReferenceTypeQuestion(currentClazz, currentAttr, 'drt_AviationType', true, localizationMap) )

        description = 'Date when the applicant achieved their current qualification level'
        currentAttr = new ClazzAttr([name: 'qualificationDate', type: 'Date', localizedAttributesMap:
            ["en_US": [desc: description], "ja_JP": [desc: '申込者が現在の資格と同等の技術を各同した時期']]] )
        currentClazz.attrs.push(currentAttr)
        defaultQuestion = 'When did you get your license or achieve this qualification level?'
        helpText = 'Please provide the date you received your license or achieved this qualification level.'
        localizationMap = ["en_US": [txt: defaultQuestion, title: 'Qualification Date', helpText: helpText],
                           "ja_JP": [txt: '資格を取得、または資格と同等の技術を獲得したのはいつですか。',
                                     title: '資格取得日',
                                     helpText: '資格取得日、またはこの資格と同等のレベルの技術を取得した時期を記入してください。']]
        currentAttr.quests.push ( buildDateQuestion(currentClazz, currentAttr, localizationMap) )

        description = 'Crop dusting, Ferrying, Instructor'"""

        def result = Splitter.on('currentAttr = new ClazzAttr')
                .split(line);

        result.eachWithIndex { it, i ->
//            println "$i: $it"
            def findTxt = it
            def regex = /(?s)(.*ja_JP.*txt:.*?[\"'])(.*?)([\"'].*)/
            def forTranslation = findTxt =~ regex
            println forTranslation.count
            if (forTranslation.count) {
                println forTranslation[0][1]+"XXX"+forTranslation[0][3]
            }

        }


    }
}
