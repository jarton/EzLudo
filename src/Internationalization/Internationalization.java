package Internationalization;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Created by Kristian on 02.10.2015.
 * @author Kristian
 * date 02.10.2015
 * Class for internationalization. getLang() returns the message bundle
 */
public class Internationalization {
    String language;
    String country;

    public Internationalization(String language, String country) {
        this.language = language;
        this.country = country;
    }

    public ResourceBundle getLang() {
        Locale currentLocale;
        ResourceBundle messages;
        currentLocale = new Locale(language, country);
        messages = ResourceBundle.getBundle("MessagesBundle", currentLocale);
        return messages;
    }
}

