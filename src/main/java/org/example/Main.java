package org.example;



import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.jsonparser.JsonParser;
import com.google.gson.Gson;

import java.io.*;
import java.net.*;
import java.sql.*;
import java.util.*;
public class Main {
    static Scanner scan = new Scanner(System.in);
    static String print_headline;
    static String content;
    static boolean loop = true;
    private static Connection con = null;
    static String input;
    private static final String API_KEY = "your-api-key-here";
    private static final String API_BASE_URL = "https://api.nytimes.com/svc/search/v2/articlesearch.json";
    private static final String[] CATEGORIES = {"Arts", "Automobiles", "Books", "Business", "Climate", "Crosswords & Games", "Education", "Fashion", "Food", "Health", "Home & Garden", "Insider", "Magazine", "Movies", "National", "NY Region", "Obits", "Opinion", "Politics", "Real Estate", "Science", "Sports", "Style", "Sunday Review", "Technology", "Theater", "Times Insider", "Today's Paper", "Travel", "Upshot", "US", "World"};

    public static void main(String[] args) {
        createTable();
        //data();
        try{
            while(loop){
                System.out.println("Select action:");
                System.out.println("1. Search about News ");
                System.out.println("2. Insert New data into database ");
                System.out.println("0. Exit");
                int option = scan.nextInt();

                // Handle user input
                switch (option) {
                    case 1:
                        FetchFromAPI f = new FetchFromAPI();
                        //searchData();
                        break;
                    case 2:
                        InsertIntoDB i = new InsertIntoDB();
                        //insertIntoDB();
                        break;
                    case 0:
                        loop = false;
                        break;
                    default:
                        System.out.println("Invalid option. Please try again.");
                        break;
                }
            }
        }
        catch(Exception e){
            System.out.println("Error: " + e.getMessage());
        }


    }

    static void searchData(){
        System.out.println("Enter News You want to search ");
        input = scan.next();
        String API = "https://api.nytimes.com/svc/search/v2/articlesearch.json?q="+input+"&api-key=RZkIxwoG3465EERdmzJ2lsUmFBYtV3lN";

        try {
            URL url = new URL(API);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");

            if (conn.getResponseCode() != 200) {
                throw new RuntimeException("HTTP error code : " + conn.getResponseCode());
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

            // Use myObj for further processing
            // Assuming myObj is a valid News object obtained from the NYTimes API

            List<Docs> articles = myObj.get_response().get_docs();

            for (Docs article : articles) {
                String mainHeadline = article.get_headline().get_main();
                String contentKicker = article.get_headline().get_content();
                String pubDate = article.get_pub_date();
                String sectionName = article.get_category();
                String originalHeadline= article.get_byline().get_original();

                // Do something with the extracted properties, such as adding them to a database or printing them to the console
                System.out.println("Main Headline: " + mainHeadline);
                System.out.println("Original Headline: " + originalHeadline);
                System.out.println("Publication Date: " + pubDate);
                System.out.println("Section Name: " + sectionName);
                System.out.println("Content Kicker: " + contentKicker);
                System.out.println("_________________________________________________");
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    static void data(){
//        for (String category : CATEGORIES) {
//            try {
//                URL url = new URL(API_BASE_URL + "?fq=section_name:" + category + "&api-key=" + API_KEY);
//                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//                conn.setRequestMethod("GET");
//                conn.setRequestProperty("Accept", "application/json");
//
//                if (conn.getResponseCode() != 200) {
//                    throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
//                }
//
//                ObjectMapper mapper = new ObjectMapper();
//                JsonNode response = mapper.readTree(conn.getInputStream());
//                JsonNode docs = response.path("response").path("docs");
//
//                for (JsonNode doc : docs) {
//                    // Extract the relevant fields from the article
//                    String articleTitle = doc.path("headline").path("main").asText();
//                    String articleURL = doc.path("web_url").asText();
//                    String articleURL = doc.path("web_url").asText();
//                    // ...
//
//
//                    // Insert the data into the database
//                    insertIntoDB(category, articleTitle, articleURL);
//                }
//
//                conn.disconnect();
//
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//    }

    static void insertIntoDB() {
        String API_KEY = "your-api-key-here";
        String API_BASE_URL = "https://api.nytimes.com/svc/search/v2/articlesearch.json";
        String[] CATEGORIES = {"Arts", "Automobiles", "Books", "Business", "Climate", "Crosswords & Games", "Education",
                "Fashion", "Food", "Health", "Home & Garden", "Insider", "Magazine", "Movies", "National", "NY Region", "Obits",
                "Opinion", "Politics", "Real Estate", "Science", "Sports", "Style", "Sunday Review", "Technology", "Theater",
                "Times Insider", "Today's Paper", "Travel", "Upshot", "US", "World"};

        try {
            System.out.println("Enter News You want to search ");
            String input = scan.next();
            //String apiUrl = "https://api.nytimes.com/svc/search/v2/articlesearch.json?q=" + input + "&api-key=RZkIxwoG3465EERdmzJ2lsUmFBYtV3lN";
            String apiUrl="https://api.nytimes.com/svc/search/v2/articlesearch.json?q=election&api-key=RZkIxwoG3465EERdmzJ2lsUmFBYtV3lN";
            //String apiUrl="https://api.nytimes.com/svc/search/v2/articlesearch.json?api-key=RZkIxwoG3465EERdmzJ2lsUmFBYtV3lN";
//            Arts/Automobiles/Books/Business/Climate/Crosswords&Games/Education/Fashion/Food/Health
//            Home&Garden/Insider/Magazine/Movies/National/NYRegion/Obits/Opinion/Politics
//            RealEstate/Science/Sports/Style/SundayReview/Technology/Theater/TimesInsider
//            Today’sPaper/Travel/Upshot/US/World
            URL url = new URL(apiUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");

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

            String urlSqlServer = "jdbc:sqlserver://localhost:1433;" + "databaseName=myDB;" + "encrypt=true;" + "trustServerCertificate=true;";
            String userSqlServer = "sa";
            String passwordSqlServer = "root";
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");

            try (Connection con = DriverManager.getConnection(urlSqlServer, userSqlServer, passwordSqlServer)) {
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


    static void createTable(){
        String url = "jdbc:sqlserver://localhost:1433; databaseName = myDB;" +
                "encrypt = true; trustServerCertificate = true";
        String user = "sa";
        String pass = "root";
        //Connection con = null;
        try {
            Driver driver = (Driver) Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver").newInstance();
            DriverManager.registerDriver(driver);
            con = DriverManager.getConnection(url, user, pass);

            Statement st = con.createStatement();

            String sql_newsAggregator= "IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_NAME = 'NewsAggregator') "
                    + "CREATE TABLE NewsAggregator("
                    + "    article_title VARCHAR(200),"
                    + "    author VARCHAR(200),"
                    + "    date VARCHAR(200),"
                    + "    category VARCHAR(200),"
                    + "    content VARCHAR(200)"
                    + ");";
            st.executeUpdate(sql_newsAggregator);

            //con.close();
        } catch (Exception ex) {
            System.err.println(ex);
        }
    }

}