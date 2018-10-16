package com.company;

import com.jaunt.util.IOUtil;

import java.io.*;
import java.util.ArrayList;

public class FilterFile {
    public static void main(String[] args) {
        ArrayList<String> medName = new ArrayList<String>();
        File webFile = new File("file/_name.txt");
        try {
            BufferedReader url = new BufferedReader(new FileReader(webFile));
            String webMed;
            while ((webMed = url.readLine()) != null){
                webMed = webMed.replace(":","-");
                webMed = webMed.replace("/","-");
                medName.add(webMed);
            }

//            File name_Med = new File("file/_medName.txt");
//            for (int i=0; i < medName.size(); i++) {
//                IOUtil.append(name_Med,medName.get(i) + "\n");
//                System.out.println(i);
//            }

            for (int i=0; i < medName.size(); i++) {
                File newFile = new File("file/" + medName.get(i)+ ".txt");
                String text = IOUtil.read(newFile);
                text = text.replace("(อ่านเพิ่มเติมได้ในเว็บ haamor.com บทความเรื่อง ข้อปฏิบัติพื้นฐานในการใช้ยาทุกชนิด)", "");
                IOUtil.write(newFile, text.trim());
                System.out.println(i + " : " + medName.get(i));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
