package model.item;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class Book extends Item {
    private String subject;
    private String author;
    private String title;
    private Locale language;
    private String ISBN;
    private Map<String, String> extendedFields;

    @Override
    public Map<String, String> getExtendedFields() {
        if (this.extendedFields == null) {
            extendedFields = new HashMap<>();
        }

        this.extendedFields.put("subject", this.subject);
        this.extendedFields.put("author", this.author);
        this.extendedFields.put("title", this.title);
        this.extendedFields.put("language", this.language.getLanguage());
        this.extendedFields.put("ISBN", this.ISBN);

        return this.extendedFields;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLanguage() {
        return this.language.getLanguage();
    }

    public void setLanguage(String language) {
        this.language = new Locale(language);
    }

    public String getISBN() {
        return ISBN;
    }

    public void setISBN(String ISBN) {
        this.ISBN = ISBN;
    }
}