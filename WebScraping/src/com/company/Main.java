package com.company;

import com.jaunt.*;
import com.jaunt.util.IOUtil;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Main {

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

            UserAgent userAgent = new UserAgent();


            for (int j = 0; j < webArr.size(); j++) {

                File file = new File("file/" + webArr.get(j) + ".txt");

                userAgent.open(new File("web/new/webEdited/" + webArr.get(j) + ".html"));
                String medName = userAgent.doc.findFirst("<h1>").getTextContent().trim();
                IOUtil.write(file, medName);

                Element articles = userAgent.doc.findFirst("<div class=\"Article_Detail_Body_Left\">");
                Elements arrArticles = articles.findEach("<h2|p>");

                for (Element article:arrArticles) {
                    String articleTag = article.outerHTML().substring(0,2);
                    Boolean checkTagHead = articleTag.equals("<h");
                    if (checkTagHead) {
                        IOUtil.append(file, " ///// " + article.getTextContent().trim() + " ///// \n");
                        //System.out.println(" ---- " + article + " ---- ");
                    } else {
                        IOUtil.append(file, article.getTextContent().trim() + " \n");
                        //System.out.println(article);
                    }
                    System.out.println(j + " : " + medName);
                }
            }
        } catch (ResponseException e) {
            e.printStackTrace();
        } catch (NotFound notFound) {
            notFound.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
