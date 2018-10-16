package com.company;

import com.jaunt.Element;
import com.jaunt.NotFound;
import com.jaunt.ResponseException;
import com.jaunt.UserAgent;
import com.jaunt.util.IOUtil;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class callWeb {
    public static void main(String[] args) {
        try {

            ArrayList<String> webMed = new ArrayList<String>();
            File urlFile = new File("file/_url.txt");
            BufferedReader url = new BufferedReader(new FileReader(urlFile));
            String urlMed;
            while ((urlMed = url.readLine()) != null){
                webMed.add(urlMed);
            }
            for (int i = 1021; i < 1426; i++) {
                UserAgent userAgent = new UserAgent();
                userAgent.visit(webMed.get(i));
                //userAgent.visit("http://haamor.com/th/%E0%B9%80%E0%B8%AD%E0%B8%AA%E0%B9%80%E0%B8%AD%E0%B8%AD%E0%B8%B2%E0%B8%A3%E0%B9%8C%E0%B9%84%E0%B8%AD/");
                String web = userAgent.doc.innerHTML().trim();
                String name = userAgent.doc.findFirst("<h1>").getTextContent().trim();
                name = name.replace(":","-");
                name = name.replace("/","-");
                File file = new File("web/new/" + name + ".html");
                IOUtil.write(file, web);
                System.out.println(i + " : " + name);
            }
        } catch (ResponseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NotFound notFound) {
            notFound.printStackTrace();
        }

    }
}
