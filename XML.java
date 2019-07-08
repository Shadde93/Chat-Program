
package projekt;
import java.awt.*;
import java.net.*;
import java.util.*;

public class XML {

        private String xml;
        private String name = "Anna";
        private Socket s;
        private Sender sender;
        private Color color = Color.black;

public XML(Socket s, String name){
    this.name = name;
    this.s = s;
    
}   

public void setName(String name){
    this.name = name;
}

public String convert (String message){
		String hex = "#" + Integer.toHexString(color.getRGB()).substring(2);
		xml ="<message name=" + name + ">" + "<text color=" + hex + ">"
				 + message +"</text>"  + "</message>";
    sender = new Sender(s, xml);// istället för att skapa nytt objekt helatiden, skapa ett eget
    sender.start();
    return getNameMessageXML(xml);
}
public static String getNameMessageXML(String xmlText){
        Scanner scan = new Scanner(xmlText).useDelimiter("[<>]|name=|color=");
        String name = "";
        String message = "";
        String scanNext = "";
        
        while (scan.hasNext()){
            scanNext = scan.next();
            if ((scanNext.trim()).equals("message")) {
              name = scan.next();  
            }
            else if ((scanNext.trim()).equals("text")){
                scan.next();
                message = scan.next(); 
            }
            
            
        } 
   
    return name + ": " + message;
    
}

}
