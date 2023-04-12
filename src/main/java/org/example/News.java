package org.example;
import java.util.*;
public class News {
    Response response;
    public void set_response(Response response) {
        this.response = response;
    }
    public Response get_response() {
        return response;
    }
}

class Response{
    List<Docs> docs = new ArrayList<>();
    public void set_docs(List<Docs> docs) {
        this.docs = docs;
    }
    public List<Docs> get_docs() {
        return docs;
    }
}





