package org.example;

public class News {
    String article_title;
    String author;
    String date;
    String category;
    String content;

    //getter and setter***********
    public void set_article_title(String article_title) {
        this.article_title = article_title;
    }
    public String get_article_title() {
        return article_title;
    }

    public void set_author(String author) {
        this.author = author;
    }
    public String get_author() {
        return author;
    }

    public void set_date(String date) {
        this.date = date;
    }
    public String get_date() {
        return date;
    }

    public void set_category(String category) {
        this.category = category;
    }
    public String get_category() {
        return category;
    }

    public void set_content(String content) {
        this.content = content;
    }
    public String get_content() {
        return content;
    }
}


