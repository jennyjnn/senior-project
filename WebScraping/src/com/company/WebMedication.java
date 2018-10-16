package com.company;

import com.jaunt.*;
import com.jaunt.util.IOUtil;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class WebMedication {
    public static void main(String[] args) {
        UserAgent userAgent = new UserAgent();
        try {
            int count = 1;

            File nameMedFile = new File("file/_รายชื่อยา.txt");
            BufferedReader medName = new BufferedReader(new FileReader(nameMedFile));
            String medNameStr;

            ArrayList<String> medicationName = new ArrayList<String>();

            while ((medNameStr = medName.readLine()) != null){
                medicationName.add(medNameStr);
            }

                userAgent.open(new File("web/วิกิยา.html"));

            Element medication = userAgent.doc.findFirst("<div class=\"Article_Section_Left\">");
            Elements medicationEach = medication.findEach("<li>");
            File file = new File("file/_urls.txt");


            for (Element med:medicationEach) {
                Elements medEach = med.findEach("<a>");
                for (Element medi:medEach) {
                    String mName = medi.getTextContent().trim();
                    for (String name:medicationName){
                        if (mName.equals(name.trim())){
                            String url = medi.getAt("href").trim();
                            IOUtil.append(file,url + "\n");
                            System.out.println(count++);
                        }
                    }
                }
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
