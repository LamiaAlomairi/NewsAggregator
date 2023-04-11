package org.example;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;

import java.io.*;
import java.net.*;
import java.sql.*;
import java.util.*;
public class Main {
    static Scanner scan = new Scanner(System.in);
    public static void main(String[] args) {

        createTable();

        String apiKey = "RZkIxwoG3465EERdmzJ2lsUmFBYtV3lN";
        String query = "art";
        String apiUrl = "https://api.nytimes.com/svc/search/v2/articlesearch.json?q=" + query + "&api-key=" + apiKey;

        try {
            URL url = new URL(apiUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String response = in.readLine();
            
            JSONObject jsonResponse = new JSONObject(response);
            JSONArray docsArray = jsonResponse.getJSONObject("response").getJSONArray("docs");

            for (int i = 0; i < docsArray.size(); i++) {
                JSONObject docObject = docsArray.getJSONObject(i);
                String title = docObject.getJSONObject("headline").getStr("main");
                String author = docObject.getJSONArray("byline").getJSONObject(0).getStr("original");
                String date = docObject.getStr("pub_date").substring(0, 10);
                String category = docObject.getJSONArray("section_name").getStr(0);
                String content = docObject.getStr("abstract");

                System.out.println("Title: " + title);
                System.out.println("Author: " + author);
                System.out.println("Date: " + date);
                System.out.println("Category: " + category);
                System.out.println("Content: " + content);
                System.out.println();
            }

            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

//        System.out.println("Enter News You want to search ");
//        String input = scan.next();
//        String API = "https://api.nytimes.com/svc/search/v2/articlesearch.json?q="+input+"&api-key=RZkIxwoG3465EERdmzJ2lsUmFBYtV3lN";
//
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
//            reader.close();
//        } catch (Exception ex) {
//            System.err.println(ex);
//        }

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