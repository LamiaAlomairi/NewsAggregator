package org.example;

import com.google.gson.Gson;

import java.io.*;
import java.net.*;
import java.util.*;

public class FetchFromAPI {
    Scanner scan = new Scanner(System.in);
    String input;
    FetchFromAPI(){
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
}
