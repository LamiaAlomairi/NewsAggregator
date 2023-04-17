package org.example;

import java.util.*;

public class Response {
    List<Docs> docs = new ArrayList<>();
    public void set_docs(List<Docs> docs) {
        this.docs = docs;
    }
    public List<Docs> get_docs() {
        return docs;
    }
}
