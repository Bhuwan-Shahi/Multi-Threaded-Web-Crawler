package org.example;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.IIOException;
import java.io.IOException;

public class WebCrawler implements Runnable {

    private static final int MAX_DEPTH = 3;
    private Thread thread;
    private String Frist_link;
    private ArrayList<String> visitedlink = new ArrayList<>();
    private int ID;

    public WebCrawler(String link, int num) {
        System.out.println("webcrawler created!!");
        Frist_link = link;
        ID = num;
        thread = new Thread(this);
        thread.start();
    }

    @Override
    public void run() {
        Crawl(1, Frist_link);
    }

    private void Crawl(int level, String url) {
        if (level <= MAX_DEPTH) {
            Document doc = requets(url);

            // Extracting email addresses using a regular expression
            if (doc != null) {
                Elements images = doc.select("img[src]");

                for (Element image : images) {
                    String imageUrl = image.attr("abs:src");
                    System.out.println("Found image URL: " + imageUrl);
                }
//                String text = doc.text();
//                List<String> emails = extractEmails(text); // Fixed typo
//
//                for (String email : emails) {
//                    System.out.println("Found email: " + email);
//                }
            }

            if (doc != null) {
                for (Element link : doc.select("a[href]")) {
                    String NextLink = link.absUrl("href");
                    if (!visitedlink.contains(NextLink)) { // Using contains to avoid case sensitivity
                        Crawl(level++, NextLink);
                    }
                }
            }
        }
    }

    private Document requets(String url) {
        try {
            Connection con = Jsoup.connect(url);
            Document doc = con.get();

            if (con.response().statusCode() == 200) {
                System.out.println("\n **Bot ID:" + ID + " Received WebPage at " + url);
                String title = doc.title();
                System.out.println(title);
                visitedlink.add(url);
                return doc;
            }
            return null;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Thread getThread() {
        return thread;
    }

    private List<String> extractEmails(String text) {
        List<String> emails = new ArrayList<>();
        Pattern emailPattern = Pattern.compile("\\b[\\w.-]+@[\\w.-]+\\.\\w{2,4}\\b");
        Matcher matcher = emailPattern.matcher(text);

        while (matcher.find()) {
            emails.add(matcher.group());
        }

        return emails;
    }
}