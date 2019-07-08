package projekt;

import java.io.*;
import java.net.*;

public class Reciever extends Thread {
    
    private Socket s = null;
    private View v;
    private String xml;
    private BufferedReader din;
    
    public Reciever(Socket s, View v){
        this.s = s;
        this.v = v;      
    }
    
    public void run() {
    
    try {
        din = new BufferedReader(new InputStreamReader(s.getInputStream()));
    } catch (IOException e) {
    System.out.println("getInputStream failed:" + e);
    System.exit(1);
    }

    while(true) {
        try {
            xml = din.readLine();
            if(xml == null) {
                v.disconnect();
            }
            v.changeText(XML.getNameMessageXML(xml));
               
        } catch (IOException e) {
            v.disconnect();
            break;
        }
         
    }
    }
}

