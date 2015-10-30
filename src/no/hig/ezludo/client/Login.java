package no.hig.ezludo.client;

import Internationalization.Internationalization;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ResourceBundle;

/**
 * Created by Kristian on 29.10.2015.
 */
public class Login extends JFrame  {
   private Internationalization internationalization;
   private ResourceBundle messages;
   private Client client;
   private JPanel panel;
   private BorderLayout border;
   private String username;
   private char[] password;

    public Login() {
        super("Ez-Ludo");
        //TODO I18N funker ikke
        internationalization = new Internationalization(System.getProperty("user.language"), System.getProperty("user.country"));
        messages = internationalization.getLang();
        panel = new JPanel();
        border = new BorderLayout();
        panel.setLayout(null);

        // Username label
        JLabel userLabel = new JLabel(messages.getString("loginUsername"));
        userLabel.setBounds(10, 10, 80, 25);
        panel.add(userLabel);

        // Username input field
        JTextField userText = new JTextField(20);
        userText.setBounds(100, 10, 160, 25);
        userText.getDocument().addDocumentListener(new DocumentListener() {
            public void changedUpdate(DocumentEvent e) {
                username = userText.getText();
            }

            public void removeUpdate(DocumentEvent e) {
                username = userText.getText();
            }

            public void insertUpdate(DocumentEvent e) {
                username = userText.getText();
            }
        });
        panel.add(userText);

        // Password label
        JLabel passwordLabel = new JLabel(messages.getString("loginPassword"));
        passwordLabel.setBounds(10, 40, 80, 25);
        panel.add(passwordLabel);

        // Password Input label
        JPasswordField passwordText = new JPasswordField(20);
        passwordText.setBounds(100,40,160,25);
        passwordText.getDocument().addDocumentListener(new DocumentListener() {
            public void changedUpdate(DocumentEvent e) {
                password = passwordText.getPassword();
            }

            public void removeUpdate(DocumentEvent e) {
                password = passwordText.getPassword();
            }

            public void insertUpdate(DocumentEvent e) {
                password = passwordText.getPassword();
            }
        });
        panel.add(passwordText);

        // Login Button
        JButton loginButton = new JButton(messages.getString("loginLogin"));
        loginButton.setBounds(10,80,80,25);
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed (ActionEvent e){
                // TODO
                // Sjekk mot server om bruker finnes med rett passord
                // IF BRUKER FINNES:
                client = new Client(username, password);
                // ELSE
                // REGISTRER
            }
        });
        panel.add(loginButton);

        // Register Button
        JButton registerButton = new JButton(messages.getString("loginRegister"));
        registerButton.setBounds(180, 80, 80, 25);
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // REGISTER
            }
        });
        panel.add(registerButton);
        this.add(panel);
    }

    public static void main(String[] args) {

        // Look and feel
        try {
            UIManager.setLookAndFeel(
                    UIManager.getSystemLookAndFeelClassName());
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
        catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        catch (InstantiationException e) {
            e.printStackTrace();
        }
        catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        Login login = new Login();
        login.setPreferredSize(new Dimension(350, 150));
        login.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        login.pack();
        login.setVisible(true);
    }
}
