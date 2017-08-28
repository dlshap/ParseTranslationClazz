package FileManagement

import com.google.common.base.Splitter

/**
 * Created by s0041664 on 8/11/2017.
 */

class KeyPairParser {
    static parseToMap(inText) {
        // produces immutable map from any number of key-pairs in inText string

        Map<String, String> result = Splitter.on(',')
                .trimResults()
                .withKeyValueSeparator(
                Splitter.on('=')
                        .limit(2)
                        .trimResults())
                .split(inText);
        result
    }

    static parseSingleKeyValue(inText) {
        inText.split('=')
    }
}
