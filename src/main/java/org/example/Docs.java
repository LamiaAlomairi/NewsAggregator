package org.example;

import java.util.ArrayList;
import java.util.List;

public class Docs {
        Headline headline;
        public void set_headline(Headline headline) {
            this.headline = headline;
        }
        public Headline get_headline() {
            return headline;
        }


        Byline byline;
        public void set_byline(Byline byline) {
            this.byline = byline;
        }
        public Byline get_byline() {
            return byline;
        }



        String pub_date;   // pub_date
        public void set_pub_date(String pub_date) {
            this.pub_date = pub_date;
        }
        public String get_pub_date() {
            return pub_date;
        }



        String section_name;   // section_name
        public void set_category(String section_name) {
            this.section_name = section_name;
        }
        public String get_category() {
            return section_name;
        }



}
