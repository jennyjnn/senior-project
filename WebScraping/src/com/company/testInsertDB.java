package com.company;

import java.io.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class testInsertDB {

    public static void main(String[] args) {
        ArrayList<String> medNames = new ArrayList<String>();

        File file = new File("file/_medName.txt");
        try {
            BufferedReader medName = new BufferedReader(new FileReader(file));
            String name;
            while ((name = medName.readLine()) != null) {
                medNames.add(name);
            }
            medName.close();

            for (int i = 0; i < 2; i++) {
                File fileMed = new File("file/" + medNames.get(i) + ".txt");
                BufferedReader med = new BufferedReader(new FileReader(fileMed));
                String medInfo = new String();
                String text;
                while ((text = med.readLine()) != null){
                    medInfo += text;
                }

                HashMap<String, String> hmMedInfo = new HashMap<String, String>();
                String topic = new String();
                boolean checkText = false;
                String[] medSplit = medInfo.split(" ///// ");
                for (int j = 0; j < medSplit.length; j++) {
                    if(j == 0){
                        hmMedInfo.put("Med_name", medSplit[j]);
                    } else if (j == 2) {
                        hmMedInfo.put("Med_intro", medSplit[j]);
                    }
                    else {
                        if (j%2 != 0){
                            if (checkText = medSplit[j].contains("ผลไม่พึงประสงค์")){
                                topic = "Med_sideEffect";
                            } else if (checkText = medSplit[j].contains("ข้อควรระวัง")) {
                                topic = "Med_warning";
                            } else if (checkText = medSplit[j].contains("เก็บรักษา")){
                                topic = "Med_preserve";
                            }
                        } else {
                            if (checkText) {
                                hmMedInfo.put(topic, medSplit[j]);
                                checkText = false;
                            }
                        }
                    }
                }

                DecimalFormat idFormat = new DecimalFormat("0000");
                String med_id = "med" + idFormat.format(i);
                System.out.println("ID MED --- " + med_id);
                //medicineRef.child(med_id);
                for (Map.Entry hm:hmMedInfo.entrySet()){
                    System.out.println("Key : " + hm.getKey() + " Value : " + hm.getValue());
                    //medicineRef.child(med_id).child((String) hm.getKey()).setValue(hm.getValue());
                }

            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
