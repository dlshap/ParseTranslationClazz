package filemanagement

import com.google.common.base.Splitter

/**
 * Created by s0041664 on 8/11/2017.
 */

class KeyPairParser {
    static parseToMap(String inText) {
        // produces immutable map from any number of key-pairs in inText string

        if (inText.count("=") > 1)
            parseMultipleKeysToMap(inText)
        else
            parseSingleKeyToMap(inText)
    }

    static parseMultipleKeysToMap(String inText) {

        Map<String, String> result = Splitter.on(',')
                .trimResults()
                .withKeyValueSeparator(
                Splitter.on('=')
                        .limit(2)
                        .trimResults())
                .split(inText);
        result
    }

    static parseSingleKeyToMap(inText) {

        Map<String, String> result = [:]
        def keyValue = Splitter.on("=")
                .trimResults()
                .split(inText);
        result.put(keyValue[0], keyValue[1])
        result
    }
}
