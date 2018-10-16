package com.company;

import com.jaunt.Element;
import com.jaunt.NotFound;
import com.jaunt.ResponseException;
import com.jaunt.UserAgent;

import java.io.File;
import java.io.IOException;

public class JauntEx1 {
    public static void main (String[] args) {
        try {
            UserAgent userAgent = new UserAgent();
            //open HTML from a String.

//            userAgent.open(new File("web/เมแมนทีน.html"));
//            Element h2 = userAgent.doc.findFirst("<h2>");
//            System.out.println(h2.innerText());

            userAgent.openContent("<html><body>WebPage <div>Hobbies:<p>beer<p>skiing</div> Copyright 2013</body></html>");
            Element body = userAgent.doc.findFirst("<body>");
            Element div = body.findFirst("<div>");

            System.out.println("body's childtext: " + body.getChildText());   //join child text of body element
            System.out.println("-----------");
            System.out.println("all body's text: " + body.getTextContent());  //join all text within body element
            System.out.println("-----------");
            System.out.println("div's child text: " + div.getChildText());    //join child text of div element
            System.out.println("-----------");
            System.out.println("all div's text: " + div.getTextContent());    //join all text within the div element
        } catch (ResponseException e) {
            e.printStackTrace();
        } catch (NotFound notFound) {
            notFound.printStackTrace();
        }
    }
}
