package projekt;

import java.awt.Color;
import java.awt.event.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyledDocument;
import javax.swing.*;
import java.net.*;
import java.util.Observable;
import java.util.Observer;
import javax.swing.text.StyleConstants;

public class View extends JFrame implements ActionListener, Observer {

    private JPanel viewPanel;
    private JTextArea textAreaWriting;   //TextArea = man kan skriva
    private JButton sendButton;
    private JButton setButton;
    private Settings settings;
    private JTextPane textAreaView; //TextPane = man kan bara kolla
    private Reciever reciever;
    private Socket s;
    private StyledDocument document;
    private Style style;
    private XML xml;


    public View() {
        
        // Den gråa rutan som öppnas.
        setTitle("Chatt");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(150, 150, 500, 400);  // (X, Y, sizeX, sizeY)
        viewPanel = new JPanel();
        setContentPane(viewPanel);
        viewPanel.setLayout(null); //sprider ut allt

        /* --- Nedan följer koden till skrivrutan. ------ */
        textAreaWriting = new JTextArea();
        textAreaWriting.setText("Skriv här...");
        textAreaWriting.setBounds(10, 245, 325, 66);
        textAreaWriting.setBorder(BorderFactory.createLineBorder(Color.BLACK)); // skapar svarta gränser för våran TextArea
        viewPanel.add(textAreaWriting);
        

        /* --- Nedan följer koden till läsrutan. ------ */
        textAreaView = new JTextPane();
        textAreaView.setText("");
        textAreaView.setBounds(10, 11, 325, 190);
        textAreaView.setEditable(false);//kan ej skriva på rutan
        textAreaView.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        viewPanel.add(textAreaView);
        document = textAreaView.getStyledDocument();

        /* --- Nedan följer knapp rutorna -- */
        sendButton = new JButton("Skicka");
        sendButton.setBounds(345, 245, 120, 66);
        viewPanel.add(sendButton);
        sendButton.addActionListener(this);
        sendButton.setEnabled(false); // kan ej trycka på knappen förräns connection sätter den till "True" 

        setButton = new JButton("Inställningar");
        setButton.setBounds(345, 11, 120, 40);
        setButton.addActionListener(this);//skapar en lyssnare på instälningsknappen
        viewPanel.add(setButton);

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == sendButton && !textAreaWriting.getText().equals("") && xml != null) {
            changeText(xml.convert(textAreaWriting.getText()));
            textAreaWriting.setText(null);
        }

        /* går till framet i settng*/
        if (e.getActionCommand().equals("Inställningar")) {
            if (s == null) {
                settings = new Settings();
                settings.addObserver(this);
            } else {
                try {
                    s.close();
                } catch (Exception a) {

                }
            }
        }

    }

    public void changeText(String mess) {
     
        try {
            if (mess != null) {
                document.insertString(document.getLength(), mess + "\n", style);
            }
        } catch (BadLocationException e) {
            System.out.println("error" + e.getMessage());
        }

    }

    public void disconnect() {
        changeText("Urkopplad");
        try {
            reciever.interrupt();
            s.close();
            sendButton.setEnabled(false);
            s = null;

        } catch (Exception a) {
            System.out.println(a.getMessage());
        }
    }

    @Override
    public void update(Observable Settings, Object s) {
        if (s != null) {
            this.s = (Socket) s;
            sendButton.setEnabled(true);
            setButton.setText("Frånkoppla");
            reciever = new Reciever((Socket) s, this);
            xml = new XML((Socket) s, settings.getName());
            reciever.start();
        }
    }

    public static void main(String[] args) {
        new View();
        
    }
}
