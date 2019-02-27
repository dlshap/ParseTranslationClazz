package i18n

import java.text.MessageFormat

class Messages {

    /* need to expand to other languages (non-static Message class?) */

    private static final String BUNDLE_NAME = "i18n.message"
    private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME)

    static String getString(String key) {
        try {
            RESOURCE_BUNDLE.getString(key)
        } catch (MissingResourceException e) {
            "! Missing a message for: $key !"
        }
    }

    static String getString(String key, Object... params) {
        try {
            MessageFormat.format(RESOURCE_BUNDLE.getString(key), params)
        } catch (MissingResourceException e) {
            "! Missing a message for: $key !"
        }
    }
}
