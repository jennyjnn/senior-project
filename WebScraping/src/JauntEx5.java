import com.jaunt.Element;
import com.jaunt.JauntException;
import com.jaunt.UserAgent;
import com.jaunt.util.IOUtil;

import java.io.File;
import java.io.IOException;

public class JauntEx5 {
    public static void main (String[] args) {
        try{
            File file = new File("example/images.htm");

            UserAgent userAgent = new UserAgent();
            userAgent.open(new File("example/images.htm"));  //open the HTML (or XML) from a file

            Element div = userAgent.doc.findFirst("<div class=images>");  //find first div who's class matches 'images'
            System.out.println("div as HTML:\n" + div.outerHTML());
            System.out.println("div's content as HTML:\n" + div.innerHTML());
            System.out.println("div as XML:\n" + div.outerXML(2));             //specify that indentation is 2 spaces
            System.out.println("div's content as XML:\n" + div.innerXML(2));   //specify that indentation is 2 spaces

            //make some changes
            div.innerHTML("<img src='presto.gif'><br>Presto!");          //replace div's content with different elements.
            IOUtil.write(file, userAgent.doc.innerHTML());
            System.out.println("Altered document as HTML:\n" + userAgent.doc.innerHTML());  //print the altered document.
        }
        catch(JauntException e){
            System.err.println(e);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
