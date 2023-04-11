package org.example;

import java.sql.*;
import java.util.*;
public class Main {
    static Scanner scan = new Scanner(System.in);
    public static void main(String[] args) {

        createTable();

        System.out.println("Enter News You want to search ");
        String input = scan.next();
        String API = "https://api.nytimes.com/svc/search/v2/articlesearch.json?q="+input+"&api-key=RZkIxwoG3465EERdmzJ2lsUmFBYtV3lN";

        int pageNumber = 0;
        List<String> articles = new ArrayList<>();
        ObjectMapper mapper = new ObjectMapper();

        while (true) {
            URL url = new URL(API);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            InputStream responseStream = connection.getInputStream();
            JsonNode jsonNode = mapper.readTree(responseStream);
            JsonNode articlesNode = jsonNode.get("response").get("docs");

            for (JsonNode articleNode : articlesNode) {
                Article article = new Article();
                article.setTitle(articleNode.get("headline").get("main").asText());
                article.setAbstractText(articleNode.get("abstract").asText());
                article.setUrl(articleNode.get("web_url").asText());
                article.setPublishedDate(articleNode.get("pub_date").asText());
                articles.add(article);
            }

            int totalPages = jsonNode.get("response").get("meta").get("hits").asInt() / 10;
            if (pageNumber >= totalPages) {
                break;
            }
            pageNumber++;
        }

        System.out.println("Total articles retrieved: " + articles.size());

    }

    static void createTable(){
        String url = "jdbc:sqlserver://localhost:1433; databaseName = myDB;" +
                "encrypt = true; trustServerCertificate = true";
        String user = "sa";
        String pass = "root";
        Connection con = null;
        try {
            Driver driver = (Driver) Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver").newInstance();
            DriverManager.registerDriver(driver);
            con = DriverManager.getConnection(url, user, pass);

            Statement st = con.createStatement();

            String sql_newsAggregator= "IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_NAME = 'NewsAggregator') "
                    + "CREATE TABLE NewsAggregator("
                    + "    article_title VARCHAR(20),"
                    + "    author VARCHAR(20),"
                    + "    date DATE,"
                    + "    category VARCHAR(20),"
                    + "    content VARCHAR(20)"
                    + ");";
            st.executeUpdate(sql_newsAggregator);

            con.close();
        } catch (Exception ex) {
            System.err.println(ex);
        }
    }

}