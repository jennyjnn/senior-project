package com.company;

import com.jaunt.*;
import com.jaunt.util.IOUtil;

import java.io.*;
import java.util.ArrayList;

public class ReplaceTag {

    public static void main(String[] args) {
        try {
            ArrayList<String> webArr = new ArrayList<String>();
            File webFile = new File("file/_name.txt");
            BufferedReader url = new BufferedReader(new FileReader(webFile));
            String webMed;
            while ((webMed = url.readLine()) != null){
                webMed = webMed.replace(":","-");
                webMed = webMed.replace("/","-");
                webArr.add(webMed);
            }
//            for (int i = 0; i < webArr.size(); i++) {
//                System.out.println(i + " : " + webArr.get(i));
//            }

            System.out.println("size : " + webArr.size());

            UserAgent userAgent = new UserAgent();
            for (int j = 0; j < webArr.size(); j++) {

                File file = new File("web/new/webEdited/" + webArr.get(j) + ".html");

                //For add dash
                //userAgent.open(new File("web/new/" + webArr.get(j) + ".html"));

                //For replace Tag
                userAgent.open(new File("web/new/webEdited/" + webArr.get(j) + ".html"));

                Element articles = userAgent.doc.findFirst("<div class=\"Article_Detail_Body_Left\">");



// Add dash (-)
//                Elements arrArticles = articles.findEach("<ul>");
//                for (Element article:arrArticles) {
//                    Elements lists = article.findEach("<li>");
//                    for (Element list:lists) {
//                        list.innerHTML("- " + list.getTextContent());
//                    }
//                }
//                IOUtil.write(file, userAgent.doc.innerHTML());
//                System.out.println(j + " : " + webArr.get(j));

// Replace Tag

                String newTag = articles.innerHTML().replace("<ul>", " ");
                newTag = newTag.replace("</ul>", " ");
                newTag = newTag.replace("<li>", "<p>");
                newTag = newTag.replace("</li>", "</p>");

                articles.innerHTML(newTag);
                IOUtil.write(file, userAgent.doc.innerHTML());
                System.out.println(j + " : " + webArr.get(j));
           }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ResponseException e) {
            e.printStackTrace();
        } catch (NotFound notFound) {
            notFound.printStackTrace();
        }

    }
}