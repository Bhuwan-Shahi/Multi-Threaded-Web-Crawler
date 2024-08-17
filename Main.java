package org.example;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        ArrayList <WebCrawler> bots = new ArrayList<>();
        bots.add(new WebCrawler("https://www.nbcnews.com/",1));
        bots.add(new WebCrawler("https://kathmandupost.com/",2));
        bots.add(new WebCrawler("https://apnews.com/",3));

        for (WebCrawler w : bots){
            try{
                w.getThread().join();
            }catch (InterruptedException e){
                e.printStackTrace();
                Thread.currentThread().interrupt();
            }
        }
    }
}