package org.example;



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
    public static void main(String[] args) {
        createTable();

        System.out.println("Enter News You want to search ");
        String input = scan.next();
        String API = "https://api.nytimes.com/svc/search/v2/articlesearch.json?q="+input+"&api-key=RZkIxwoG3465EERdmzJ2lsUmFBYtV3lN";

//        try {
//            URL url = new URL(API);
//            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//            connection.setRequestMethod("GET");
//
//            InputStream responseStream = connection.getInputStream();
//            BufferedReader reader = new BufferedReader(new InputStreamReader(responseStream));
//            String line;
//
//            while ((line = reader.readLine()) != null) {
//                System.out.println(line);
//            }
//
//
//            reader.close();
//        } catch (Exception ex) {
//            System.err.println(ex);
//        }

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

        } catch (Exception e) {
            e.printStackTrace();
        }

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