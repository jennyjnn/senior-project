package com.jenny.medicationreminder;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.jenny.medicationreminder.Model.Medicine;
import com.ms.square.android.expandabletextview.ExpandableTextView;

public class MedInfoActivity extends AppCompatActivity {

    CardView cvAppBarMedInfo;
    TextView tvReadMore, tvToggle2, tvMedName, expandable_text;

    String medName = new String();

    FirebaseDatabase database;
    DatabaseReference infoRef;

    ExpandableTextView expanIntro, expandSideEff, expandWarning, expandPreserve;

//    VideoView vdMed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_med_info);

        Intent intent = getIntent();
        medName = intent.getStringExtra("medName");

        initInsatances();
    }

    private void initInsatances() {
        // set color for app bar
        cvAppBarMedInfo = findViewById(R.id.cvAppBarMedInfo);
        cvAppBarMedInfo.setBackgroundResource(R.drawable.bg_appbar);

        // set text for medicine name
        tvMedName = findViewById(R.id.tvMedName);
        tvMedName.setText(medName);

        // set text for expandable textview
        expanIntro = findViewById(R.id.expandIntro);
        expanIntro.setText("ยาเรกูลาร์อินซูลิน(Regular insulin  หรือ Insulin regular  หรือ Neutral insulin หรือ Soluble insulin) เป็นยาที่เป็นฮอร์โมนอินซูลินของมนุษย์ที่นักวิทยาศาสตร์ใช้เทคโนโลยีพันธุวิศวกรรมตัดต่อสารพันธุกรรมโดยนำยีน/จีน(Gene) ที่ช่วยผลิตฮอร์โมนอินซูลินของมนุษย์ไปใส่ในโครโมโซมของแบคทีเรียที่มีชื่อว่า Escherichia coli(E.coli)  ทำให้ได้แบคทีเรียสายพันธุ์ที่สามารถผลิตอินซูลินของมนุษย์ และเรียกอินซูลินชนิดนี้ว่า “เรกูลาร์อินซูลิน”   ทางคลินิก ได้นำยาเรกูลาร์ อินซูลิน  มาใช้รักษาอาการโรคเบาหวานประเภทที่ I และ II,   ภาวะเลือดเป็นกรดด้วยสาเหตุจากโรคเบาหวาน,  ภาวะร่างกายดื้อต่อฮอร์โมนอินซูลิน \nยาเรกูลาร์อินซูลินสามารถออกฤทธิ์ควบคุมน้ำตาลในเลือดได้นานประมาณ 3–6 ชั่วโมง และจัดเป็นยาที่ออกฤทธิ์ระยะสั้น(Short-acting insulin)โดยเริ่มออกฤทธิ์ภายใน 30 นาทีหลังฉีดยา   รูปแบบเภสัชภัณฑ์ของยาอินซูลินชนิดนี้ เป็นประเภทยาฉีดที่สามารถฉีดเข้าใต้ผิวหนัง  เข้ากล้ามเนื้อ หรือฉีดเข้าหลอดเลือดดำก็ได้   ยาอินซูลินจะทำให้เซลล์กล้ามเนื้อและเซลล์เนื้อเยื่อไขมัน/เซลล์ไขมัน (Adipose tissue) ของร่างกายดึงน้ำตาลกลูโคสไปใช้ให้เกิดประโยชน์  ซึ่งการใช้ยาเรกูลาร์ อินซูลินเพื่อรักษาเบาหวานนั้น ผู้ป่วยอาจต้องได้รับการฉีดยานี้ 3 ครั้งหรืออาจมากกว่า 3 ครั้ง/วัน ทั้งนี้ด้วยระยะเวลาของการออกฤทธิ์เฉลี่ยอยู่ที่ 6 ชั่วโมง \nระหว่างได้รับยาเรกูลาร์อินซูลิน ผู้ป่วยจะต้องได้รับการทดสอบน้ำตาลในเลือดเป็นระยะๆตามแพทย์สั่ง เพื่อเป็นการสอบเทียบประสิทธิภาพการออกฤทธิ์ของยาอินซูลินนี้ที่แพทย์สั่งจ่ายว่า เหมาะสมกับผู้ป่วยเบาหวานมากน้อยเพียงใด \nผู้ป่วยเบาหวานบางกลุ่มอาจไม่สามารถใช้เรกูลาร์ อินซูลิน ด้วยแพ้ยานี้/แพ้ส่วนประกอบในสูตรตำรับ หรือสภาพร่างกายผู้ป่วยมีระดับน้ำตาลในเลือดต่ำอยู่แล้ว  นอกจากนี้ยังต้องระมัดระวังการใช้เรกูลาร์ อินซูลิน ร่วมกับยารักษาเบาหวานชนิดอื่น อย่างเช่นยา  Glimepiride หรือ Metformin  ด้วยเสี่ยงต่อจะทำให้เกิดหัวใจทำงานผิดปกติ  ดังนั้นผู้ป่วยเบาหวานควรแจ้งให้ แพทย์ พยาบาล เภสัชกร ทราบโดยละเอียดว่าตนเองมียาประเภทใดที่ต้องรับประทานอยู่ก่อน  มีโรคประจำตัวอื่นใดอยู่บ้าง \nยาเรกูลาร์ อินซูลิน จัดเป็นเภสัชภัณฑ์ที่มีความไวต่ออุณหภูมิสูง จึงต้องจัดเก็บ ยานี้ในตู้เย็นที่มีอุณหภูมิ 2–8 องศาเซลเซียส(Celsius)  ่อนนำอินซูลินชนิดนี้กลับมาใช้ที่บ้าน ผู้ป่วยเบาหวาน/ญาติจะต้องทำความเข้าใจเรียนรู้วิธีการใช้ยานี้จากแพทย์/พยาบาล/เภสัชกร และต้องใช้เรกูลาร์อินซูลินตามขนาดที่แพทย์แนะนำ  นอกจากนี้ยังต้องเรียนรู้สภาวะร่างกายที่มีระดับน้ำตาลในเลือดต่ำ หรือสูงเกินไป เพื่อใช้วิธีปฐมพยาบาลตนเองได้อย่างเหมาะสมและถูกต้อง \\n*กรณีเกิดข้อผิดพลาด ที่ผู้ป่วยได้รับเรกูลาร์อินซูลิน เกินขนาดจะทำให้เกิดภาวะน้ำตาลในเลือดต่ำ  ระดับเกลือโปแตสเซียมในเลือดต่ำ   กรณีที่มีภาวะดังกล่าวไม่รุนแรงมาก ผู้ป่วยสามารถดื่มน้ำหวานหรืออมลูกกวาดที่มีส่วนประกอบของน้ำตาลก็จะช่วยบรรเทาอาการได้แล้ว  แต่กรณีที่เกิดภาวะน้ำตาลในเลือดต่ำอย่างรุนแรง ผู้ป่วยอาจอยู่ในสภาพหมดสติ  ผู้พบเห็นจะต้องรีบนำผู้ป่วยส่งโรงพยาบาลทันที/ฉุกเฉิน  โดยแพทย์จะใช้ยา Glucagon ฉีดเข้ากล้ามเนื้อเพื่อบำบัดอาการ หรือไม่ก็ให้น้ำตาลกลูโคสเข้าทางหลอดเลือดดำเพื่อทำให้ร่างกายผู้ป่วยฟื้นสภาพขาดน้ำตาลโดยเร็ว \\nองค์การอนามัยโลกได้ระบุให้ยาเรกูลาร์ อินซูลินเป็นยาจำเป็นขั้นพื้นฐานที่สถาน พยาบาลแต่ละแห่งควรมีสำรองไว้ให้บริการต่อผู้ป่วย  คณะกรรมการอาหารและยาของไทยก็ได้บรรจุยาเรกูลาร์ อินซูลินอยู่ในบัญชียาหลักแห่งชาติ  ซึ่งผู้บริโภคสามารถพบเห็นการใช้ยาเรกูลาร์อินซูลิน ทั้งในสถานพยาบาลของรัฐ และของเอกชนโดยทั่วไป");

        expandSideEff = findViewById(R.id.expandSideEff);
        expandSideEff.setText("ยาเรกูลาร์ อินซูลินสามารถก่อให้เกิดผลไม่พึงประสงค์จากยา (ผลข้างเคียง/อาการข้างเคียง)ต่อระบบอวัยวะต่างๆของร่างกาย ดังนี้ เช่น \n- ผลต่อหัวใจ: เช่น อาการบวมน้ำซึ่งมีสาเหตุจากการคั่งของเกลือโซเดียมในร่างกาย \n- ผลต่อผิวหนัง: เช่น เกิดภาวะไขมันสะสมผิดปกติในบริเวณผิวหนัง \n- ผลต่อระบบการเผาผลาญพลังงานของร่างกาย: เช่น เกิดภาวะน้ำตาลในเลือดต่ำจนอาจทำให้ผู้ป่วยหมดสติ อาจเกิดภาวะร่างกายดื้ออินซูลินโดยมีน้ำตาลในเลือดสูง \n- ผลต่อตา: เช่น เกิดความผิดปกติกับเส้นประสาทตา ส่งผลให้ตามัว \n- ผลต่อระบบประสาท: รู้สึกเจ็บปลายประสาท");

        expandWarning = findViewById(R.id.expandWarning);
        expandWarning.setText("มีข้อควรระวังการใช้ยาเรกูลาร์ อินซูลิน เช่น \n- ห้ามใช้กับผู้ที่แพ้ยาอินซูลินชนิดนี้ \n- ห้ามดื่มเครื่องดื่มประเภทแอลกอฮอล์ขณะใช้ยานี้ ด้วยจะทำให้ผู้ป่วยมีภาวะน้ำตาลในเลือดต่ำหรือไม่ก็สูงตามมา \n- ห้ามใช้ยานี้กับสตรีมีครรภ์/ตั้งครรภ์  สตรีที่อยู่ในภาวะให้นมบุตร และเด็ก โดยไม่มีคำสั่งแพทย์ \n- ห้ามปรับขนาดการใช้ยานี้ด้วยตนเอง และต้องใช้ยาตามขนาดที่แพทย์แนะนำ \n- ห้ามใช้ยาที่มีสภาพเปลี่ยนไปจากเดิม เช่น สียาเปลี่ยนไป  ตัวยาตกตะกอน \n- ใช้ยานี้ต่อเนื่องตามแพทย์สั่ง เพื่อควบคุมระดับน้ำตาลในเลือดให้เป็นปกติ \n- ควรเปลี่ยนตำแหน่งการฉีดยานี้ไม่ให้ซ้ำบริเวณเดิมเพื่อหลีกเลี่ยงผิวหนังอักเสบจากการฉีดยาซ้ำๆ \n- ตรวจสอบระดับน้ำตาลในเลือดของตนเองเป็นประจำตามคำแนะนำของ แพทย์ พยาบาล เภสัชกร \n- ควบคุมอาหาร  ออกกำลังกาย   และพักผ่อน ตามที่แพทย์ พยาบาล เภสัชกรแนะนำ \n- มาพบแพทย์/มาโรงพยาบาล เพื่อแพทย์ตรวจร่างกายตามนัดหมายทุกครั้ง \n- ห้ามแบ่งยาให้ผู้อื่นใช้ \n- ห้ามใช้ยาหมดอายุ \n- ห้ามเก็บยาหมดอายุ \n***** อนึ่ง ทุกคนต้องตระหนักถึงความปลอดภัยจากการใช้ ”ยา”ที่รวมถึงยาแผนปัจจุบันทุกชนิด(รวมยาเรกูลาร์ อินซูลินด้วย) ยาแผนโบราณ  อาหารเสริม  ผลิตภัณฑ์เสริมอาหาร  ทุกชนิด  และสมุนไพรต่างๆ เพราะยามีทั้งให้คุณและให้โทษดังนั้นเมื่อมีการใช้ยาทุกครั้ง ควรต้องปฏิบัติตามข้อปฏิบัติพื้นฐานในการใช้ยาทุกชนิด รวมทั้งควรปรึกษาเภสัชกรประจำร้านขายยาก่อนซื้อยาใช้เองเสมอ");

        expandPreserve = findViewById(R.id.expandPreserve);
        expandPreserve.setText("ควรเก็บยาเรกูลาร์อินซูลินภายใต้อุณหภูมิ 2–8 องศาเซลเซียส(Celsius) ห้ามเก็บยาในช่องแช่แข็งตู้เย็น เก็บยาให้พ้นมือเด็กและสัตว์เลี้ยง ไม่เก็บยาในห้องน้ำหรือในรถยนต์ และเก็บยาในภาชนะที่ปิดมิดชิด พ้นแสง/แสงแดด ความร้อนและความชื้น");

        // Read information of medication from firebase database
        database = FirebaseDatabase.getInstance();
        infoRef = database.getReference("Medicine");

//        Query query = infoRef.orderByChild("Med_name").startAt("#"+"Metformin");
//        query.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                if (dataSnapshot.exists()){
//                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                        Medicine medicine = snapshot.getValue(Medicine.class);
//                        tvMedName.setText(medicine.getMed_name());
//                        Log.e("Med", medicine.getMed_name());
//                    }
//                } else {
//                    Log.e("NO Med", "no");
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });



        //  set url to video
//        vdMed = findViewById(R.id.vdMed);

        String path = "https://www.youtube.com/watch?v=WgfS1sFO40k";
        Uri uri = Uri.parse(path);

//        vdMed.setVideoURI(uri);
//        vdMed.start();

    }


    public void backBtn(View view) {
        finish();
    }

}
