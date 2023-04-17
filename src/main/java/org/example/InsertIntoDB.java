package org.example;

import com.google.gson.Gson;

import java.io.*;
import java.net.*;
import java.sql.*;
import java.util.*;

public class InsertIntoDB {

    String API_KEY = "&api-key=RZkIxwoG3465EERdmzJ2lsUmFBYtV3lN";
    String API_BASE_URL = "https://api.nytimes.com/svc/search/v2/articlesearch.json?q=";
    String[] CATEGORIES = {"Arts", "Automobiles", "Books", "Business", "Climate", "Crosswords & Games", "Education", "Fashion", "Food", "Health", "Home & Garden", "Insider", "Magazine", "Movies", "National", "NY Region", "Obits", "Opinion", "Politics", "Real Estate", "Science", "Sports", "Style", "Sunday Review", "Technology", "Theater", "Times Insider", "Today's Paper", "Travel", "Upshot", "US", "World"};

    public InsertIntoDB() {
        for (String category : CATEGORIES) {
            try {
                URL url = new URL(API_BASE_URL + category + API_KEY);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setRequestProperty("Accept", "application/json");

                int responseCode = conn.getResponseCode();
                if (responseCode == 429) {
                    // Wait for 1 minute before retrying the request
                    Thread.sleep(60 * 1000);
                    // Retry the request
                    conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    conn.setRequestProperty("Accept", "application/json");
                    responseCode = conn.getResponseCode();
                }

                if (conn.getResponseCode() != 200) {
                    throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
                }

                BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
                String output;
                StringBuilder json = new StringBuilder();

                while ((output = br.readLine()) != null) {
                    json.append(output);
                }

                conn.disconnect();

                Gson gson = new Gson();
                News myObj = gson.fromJson(json.toString(), News.class);

                List<Docs> articles = myObj.get_response().get_docs();

                String urlSqlServer = "jdbc:sqlserver://localhost:1433; databaseName = myDB;" +
                        "encrypt = true; trustServerCertificate = true";
                String user = "sa";
                String pass = "root";

                try (Connection con = DriverManager.getConnection(urlSqlServer, user, pass)) {
                    for (Docs article : articles) {
                        String mainHeadline = article.get_headline().get_main();
                        String contentKicker = article.get_headline().get_content();
                        String pubDate = article.get_pub_date();
                        String sectionName = article.get_category();
                        String originalHeadline = article.get_byline().get_original();

                        String sql = "INSERT INTO NewsAggregator (article_title, author, date, category, content) VALUES (?, ?, ?, ?, ?)";
                        try (PreparedStatement pstmt = con.prepareStatement(sql)) {
                            pstmt.setString(1, mainHeadline);
                            pstmt.setString(2, originalHeadline);
                            pstmt.setString(3, pubDate);
                            pstmt.setString(4, sectionName);
                            pstmt.setString(5, contentKicker);

                            int rowsInserted = pstmt.executeUpdate();
                            if (rowsInserted > 0) {
                                System.out.println("A new record was inserted successfully!");
                            }
                        } catch (SQLException ex) {
                            System.out.println(ex.getMessage());
                        }
                    }
                } catch (SQLException e) {
                    System.out.println("Connection failure.");
                    e.printStackTrace();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
