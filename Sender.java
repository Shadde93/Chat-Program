package projekt;

import java.io.*;
import java.net.*;

public class Sender extends Thread {
    
    private String xml;
    private PrintWriter dout;
    
    public Sender(Socket s, String xml){
    this.xml = xml;
    
    try{                
        dout = new PrintWriter(s.getOutputStream(), true);
    }catch (IOException e){
	System.out.println("error PrintWriter:"+ e.getMessage());
    }
    }
 
    public void run(){
        dout.println(xml);
    }
}
