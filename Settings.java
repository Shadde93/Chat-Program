package projekt;

import java.awt.Color;
import java.awt.event.*;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Observable;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;


public class Settings extends Observable implements ActionListener {

    private JPanel settingsPanel;
    private JTextField ipField;
    private JTextField portField;
    private JTextField usernameField;
    private JRadioButton serverButton;
    private JRadioButton clientButton;
    private JButton okButton;
    private JLabel ipLabel;
    private JLabel portLabel;
    private JLabel usernameLabel;
    private JButton colButton;
    private Color color = (Color.BLACK);//börjar med wit färg
    private JPanel colPanel;
    private JFrame frame;
    private Socket s = null;
    private ServerSocket ss;

    public Settings() {

        /* Inställnings fönster nedan ---*/
        frame = new JFrame();
        frame.setTitle("Inställningar");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setBounds(150, 150, 475, 325);  // (X, Y, sizeX, sizeY)
        settingsPanel = new JPanel();
        frame.setContentPane(settingsPanel);
        settingsPanel.setLayout(null);

        /* IP-fältet */
        ipField = new JTextField();
        ipField.setText("localhost");
        ipField.setBounds(100, 11, 250, 20);
        ipField.setBorder(BorderFactory.createLineBorder(Color.BLACK)); // skapar svarta gränser för våran TextArea
        settingsPanel.add(ipField);

        /* IP-label */
        ipLabel = new JLabel("IP:");
        ipLabel.setBounds(80, 11, 50, 20);
        settingsPanel.add(ipLabel);

        /* Port-fältet */
        portField = new JTextField();
        portField.setText("5545");
        portField.setBounds(100, 50, 150, 20);
        portField.setBorder(BorderFactory.createLineBorder(Color.BLACK)); // skapar svarta gränser för våran TextArea
        settingsPanel.add(portField);

        /* Port-label */
        portLabel = new JLabel("Port:");
        portLabel.setBounds(60, 50, 50, 20);
        settingsPanel.add(portLabel);

        /* Användarnamn-fältet */
        usernameField = new JTextField();
        usernameField.setText("Anna");
        usernameField.setBounds(100, 90, 150, 20);
        usernameField.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        settingsPanel.add(usernameField);

        /* Användarnamn-label */
        usernameLabel = new JLabel("Namn:");
        usernameLabel.setBounds(50, 90, 50, 20);
        settingsPanel.add(usernameLabel);

        /* Server knapp*/
        serverButton = new JRadioButton("Server");
        serverButton.setBounds(100, 130, 75, 23);
        serverButton.addActionListener(this);
        settingsPanel.add(serverButton);

        /* Client knapp*/
        clientButton = new JRadioButton("Client");
        clientButton.setBounds(175, 130, 75, 23);
        settingsPanel.add(clientButton);
        clientButton.addActionListener(this);

        /* Färg knapp*/
        colButton = new JButton("Färger");
        colButton.setBounds(100, 170, 120, 50);
        colButton.addActionListener(this);
        settingsPanel.add(colButton);

        /* OK- knapp*/
        okButton = new JButton("Ok");
        okButton.setBounds(230, 170, 120, 50);
        settingsPanel.add(okButton);
        okButton.setEnabled(false);
        okButton.addActionListener(this);

        //frame.setVisible(true);
        frame.setVisible(true);
        frame.setBounds(150, 150, 475, 325);

    }

    public void actionPerformed(ActionEvent e) {

        /*färg panel*/
        if (e.getActionCommand().equals("Färger")) {
            colPanel = new JPanel();
            color = JColorChooser.showDialog(null, "Välj färg", color);//(positon, titel av nya boxen, initiell värde=color).När man väljer färg sätts den i color 
            if (color == null)//om ingen färg väljs frå man svart
            {
                color = (Color.BLACK);
            }
            //kör colPanel.och din funktion med addActionListener andvänder den valda färgen
            colPanel.setVisible(true);
            colPanel.setBounds(150, 150, 1000, 500);
        }
        if (serverButton.isSelected() == true) {
            okButton.setEnabled(true);
            ipField.setEditable(false);
            ipField.setVisible(false);

        }
        if (e.getSource() == okButton) {
            okButton.setEnabled(false);
            if (serverButton.isSelected() == true) {
                createSS();
            } else {
                createS();

            }
            setChanged();
            notifyObservers(s);
            frame.setVisible(false);

        }

        if (e.getSource() == serverButton) {
            clientButton.setSelected(!serverButton.isSelected());
        }

        if (e.getSource() == clientButton) {
            serverButton.setSelected(!clientButton.isSelected());
        }

        if (clientButton.isSelected() == true) {
            okButton.setEnabled(true);
            ipField.setText("localhost");
            ipField.setEditable(true);
            ipField.setVisible(true);
        }
    }

    public String getName() {
        return usernameField.getText();
    }

    public void createSS() {
        try {
            ss = new ServerSocket(Integer.parseInt(portField
                    .getText()));
        } catch (IOException e) {
            failed(e);
        }

        try {
            s = ss.accept();  // blocking operation
        } catch (IOException e) {
            failed(e);
        }

        System.out
                .println("Connection Established To Client: " + s.getInetAddress());

    }

    private void createS() {
        try {
            s = new Socket(ipField.getText(),
                    Integer.parseInt(portField.getText()));
        } catch (Exception e) {
            failed(e);
        }
        System.out
                .println("Connection Established To Server: " + s.getInetAddress());
    }

    private void failed(Exception e) {
        JOptionPane.showMessageDialog(frame,
                "Connection Failed:\n" + e.getMessage(), "Connection Failure",
                JOptionPane.ERROR_MESSAGE);
        s = null;
    }

}
