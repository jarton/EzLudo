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
 * Created by Kristian on 30.10.2015.
 */
public class UserAccount {
    private Internationalization internationalization;
    private ResourceBundle messages;
    private JPanel panel;
    public Login login;
    private String username;
    private char[] password;
    private char[] passwordRepeat ;
    private String email;
    private int errorsNumb=0;
    private String[] errors = new String[20];


    public UserAccount(Internationalization internationalization) {
        messages = internationalization.getLang();
    }

    public JPanel createLayout() {
        panel = new JPanel();
        panel.setLayout(null);

        /*
        // Checkerlabel
        JLabel checkerLabel = new JLabel("Checkertext");
        checkerLabel.setForeground(Color.red);
        checkerLabel.setBounds(10, 130, 300, 25);
        panel.add(checkerLabel);
        */

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

        // E-Mail label
        JLabel emailLabel = new JLabel(messages.getString("loginEmail"));
        emailLabel.setBounds(10, 40, 80, 25);
        panel.add(emailLabel);

        // E-Mail input field
        JTextField emailText = new JTextField(20);
        emailText.setBounds(100, 40, 160, 25);
        emailText.getDocument().addDocumentListener(new DocumentListener() {
            public void changedUpdate(DocumentEvent e) {
                email = emailText.getText();
            }

            public void removeUpdate(DocumentEvent e) {
                email = emailText.getText();
            }

            public void insertUpdate(DocumentEvent e) {
                email = emailText.getText();
            }
        });
        panel.add(emailText);

        // Password label
        JLabel passwordLabel = new JLabel(messages.getString("loginPassword"));
        passwordLabel.setBounds(10, 70, 80, 25);
        panel.add(passwordLabel);

        // Password Input label
        JPasswordField passwordText = new JPasswordField(20);
        passwordText.setBounds(100,70,160,25);
        passwordText.getDocument().addDocumentListener(new DocumentListener() {
            public void changedUpdate(DocumentEvent e) {
                int len = passwordText.getPassword().length;
                password = new char[len];
                password = passwordText.getPassword();

            }

            public void removeUpdate(DocumentEvent e) {
                int len = passwordText.getPassword().length;
                password = new char[len];
                password = passwordText.getPassword();
            }

            public void insertUpdate(DocumentEvent e) {
                int len = passwordText.getPassword().length;
                password = new char[len];
                password = passwordText.getPassword();
            }
        });
        panel.add(passwordText);

        // Password Repeat label
        JLabel passwordLabelRepeat = new JLabel(messages.getString("loginPasswordRepeat"));
        passwordLabelRepeat.setBounds(10, 100, 80, 25);
        panel.add(passwordLabelRepeat);

        // Password Repeat Input label
        JPasswordField passwordRepeatText = new JPasswordField(20);
        passwordRepeatText.setBounds(100,100,160,25);
        passwordRepeatText.getDocument().addDocumentListener(new DocumentListener() {
            public void changedUpdate(DocumentEvent e) { passwordRepeat = passwordRepeatText.getPassword(); }

            public void removeUpdate(DocumentEvent e) {
                passwordRepeat = passwordRepeatText.getPassword();
            }

            public void insertUpdate(DocumentEvent e) { passwordRepeat = passwordRepeatText.getPassword(); }
        });
        panel.add(passwordRepeatText);

        // Back Button
        JButton backButton = new JButton(messages.getString("back"));
        backButton.setBounds(10,160,80,25);
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        panel.add(backButton);

        // Register Button
        JButton registerButton = new JButton(messages.getString("register"));
        registerButton.setBounds(180, 160, 80, 25);
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String output="";
                for (int i = 0; i<=19; i++) {
                    errors[i] = null;
                }

                if (usernameChecker(username) && emailChecker(email) && passwordChecker(password, passwordRepeat)) {
                }
                else {
                    for (int i=1; i<=errorsNumb; i++) {
                        if (errors[i] != null)
                        output=output+"\n"+errors[i];
                    }
                    JOptionPane.showMessageDialog(null, output, messages.getString("errormsg"), JOptionPane.WARNING_MESSAGE);
                }
            }

        });
        panel.add(registerButton);

        return panel;
    }

    public boolean passwordChecker(char[] a, char[] b) {
        int length;

        if (a == null || b == null) {
            errorsNumb++;
            errors[errorsNumb] = messages.getString("registerNoPassword");
            return false;
        }


        if (a.length != b.length) {
            errorsNumb++;
            errors[errorsNumb] = messages.getString("registerPasswordNoMatch");
            return false;
        }
        else
            length = a.length;
        if (length < 8) {
            errorsNumb++;
            errors[errorsNumb] = messages.getString("registerPasswordTooShort");
            return false;
        }
        for (int i = 0; i < length; i++){
            if (a[i] != b[i]) {
                errorsNumb++;
                errors[errorsNumb] = messages.getString("registerPasswordNoMatch");
                return false;
            }
        }

        return true;
    }

    public boolean emailChecker(String email) {
        if (email == null) {
            errorsNumb++;
            errors[errorsNumb] = messages.getString("registerNoEmail");
            return false;
        }
        String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
        java.util.regex.Matcher m = p.matcher(email);
        if(!m.matches()) {
            errorsNumb++;
            errors[errorsNumb] = messages.getString("registerEmailNotValid");
        }
        return m.matches();
    }


    public boolean usernameChecker (String username) {
        if (username == null) {
            errorsNumb++;
            errors[errorsNumb] = messages.getString("registerNoUsername");
            return false;
        }
        if (username.length() < 3 || username.length() > 15) {
            if (username.length() < 3) {
                errorsNumb++;
                errors[errorsNumb] = messages.getString("registerShortUsername");
            }
            if (username.length() > 15) {
                errorsNumb++;
                errors[errorsNumb] = messages.getString("registerLongUsername");
            }
            return false;
        }
        else
            return true;
    }
}
