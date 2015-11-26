package no.hig.ezludo.client;

import Internationalization.Internationalization;
import java.io.*;
import java.net.Socket;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class contains the user-registration gui included userdata validation.
 * @author Kristian
 * date 30.10.2015.
 */
public class UserAccount {
    private static Logger logger;
    private JPanel panel;
    private JPanel loginPanel;
    private JFrame jFrame;

    //Userdata
    private String username;
    private char[] password;
    private char[] passwordRepeat ;
    private String email;

    //Number of errors and errormessages from registration
    private int errorsNumb;
    private String[] errors;

     // Variables for communication with server
    PrintWriter output;
    BufferedReader input;
    private Socket loginClient;
    private String IP;

    //Constructor which gets UI-data and I18N objects from Login-class.
    public UserAccount(JFrame jFrame, JPanel loginPanel) {
        logger = Logger.getAnonymousLogger();
        this.jFrame = jFrame;
        this.loginPanel = loginPanel;
        loginClient = null;
        errors = new String[25];
        errorsNumb=0;

    }
    /**
     * This function creates the registration gui.
     * Labels for password, repeat password, username and email.
     * All labels with documentListener which update the userdata
     * variables on change.
     *
     * All buttons got a action listener which return the user to
     * the login screen.
     *
     * All validation-checkers adds a error message to String[] errors
     * which contains all errors found at det user data validation methods.
     */
    public JPanel createLayout() {
        panel = new JPanel();
        panel.setLayout(null);

        //Username label
        JLabel userLabel = new JLabel(Login.getTranslation().getString("username"));
        userLabel.setBounds(10, 10, 80, 25);
        panel.add(userLabel);

        //Username input field
        JTextField userText = new JTextField(20);
        userText.setBounds(100, 10, 160, 25);
        userText.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void changedUpdate(DocumentEvent e) {
                username = userText.getText();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                username = userText.getText();
            }

            @Override
            public void insertUpdate(DocumentEvent e) {
                username = userText.getText();
            }
        });
        panel.add(userText);

        //E-mail label
        JLabel emailLabel = new JLabel(Login.getTranslation().getString("loginEmail"));
        emailLabel.setBounds(10, 40, 80, 25);
        panel.add(emailLabel);


        //E-mail input field
        JTextField emailText = new JTextField(20);
        emailText.setBounds(100, 40, 160, 25);
        emailText.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void changedUpdate(DocumentEvent e) {
                email = emailText.getText();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                email = emailText.getText();
            }

            @Override
            public void insertUpdate(DocumentEvent e) {
                email = emailText.getText();
            }
        });
        panel.add(emailText);

        //Password label
        JLabel passwordLabel = new JLabel(Login.getTranslation().getString("loginPassword"));
        passwordLabel.setBounds(10, 70, 80, 25);
        panel.add(passwordLabel);

        //Password input field
        JPasswordField passwordText = new JPasswordField(20);
        passwordText.setBounds(100,70,160,25);
        passwordText.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void changedUpdate(DocumentEvent e) {
                password = passwordText.getPassword();
            }
            @Override
            public void removeUpdate(DocumentEvent e) {
                password = passwordText.getPassword();
            }
            @Override
            public void insertUpdate(DocumentEvent e) {
                password = passwordText.getPassword();
            }
        });
        panel.add(passwordText);

        //Password repeat label
        JLabel passwordLabelRepeat = new JLabel(Login.getTranslation().getString("loginPasswordRepeat"));
        passwordLabelRepeat.setBounds(10, 100, 80, 25);
        panel.add(passwordLabelRepeat);

        //Password repeat input field
        JPasswordField passwordRepeatText = new JPasswordField(20);
        passwordRepeatText.setBounds(100,100,160,25);
        passwordRepeatText.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void changedUpdate(DocumentEvent e) {
                passwordRepeat = passwordRepeatText.getPassword();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                passwordRepeat = passwordRepeatText.getPassword();
            }

            @Override
            public void insertUpdate(DocumentEvent e) {
                passwordRepeat = passwordRepeatText.getPassword();
            }
        });
        panel.add(passwordRepeatText);

         //IP label
        JLabel ipLabel = new JLabel(Login.getTranslation().getString("loginIP"));
        ipLabel.setBounds(10, 130, 80, 25);
        panel.add(ipLabel);

        //IP input field
        JTextField ipTextField = new JTextField(20);
        ipTextField.setBounds(100, 130, 160, 25);
        ipTextField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void changedUpdate(DocumentEvent e) { IP =  ipTextField.getText(); }
            @Override
            public void removeUpdate(DocumentEvent e) {
                IP =  ipTextField.getText();
            }
            @Override
            public void insertUpdate(DocumentEvent e) { IP =  ipTextField.getText(); }
        });
        panel.add( ipTextField);


         //Back button: Return to Login
        JButton backButton = new JButton(Login.getTranslation().getString("back"));
        backButton.setBounds(10,190,80,25);
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                toLogin();
            }
        });
        panel.add(backButton);


        // Register button
        // Checks userdata and connects to server for registering
        JButton registerButton = new JButton(Login.getTranslation().getString("register"));
        registerButton.setBounds(180, 190, 80, 25);
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                    String output = "";
                    for (int i = 0; i <= 24; i++) {
                        errors[i] = null;
                    }

                     //Checks if userdata is valid and returns user to login screen
                     // If invalid userdata: Errormessage according to error
                    if (usernameChecker(username) && emailChecker(email) && pwdChecker(password, passwordRepeat) && validIP(IP)) {
                        Constants.setServerIP(IP);
                        String passwordToHash = String.valueOf(password);
                        String hashedPassword = getSHA256(passwordToHash, email);
                        register(username, email, hashedPassword);
                        toLogin();
                    } else {
                        for (int i = 1; i <= errorsNumb; i++) {
                            if (errors[i] != null)
                                output = output + "\n" + errors[i];
                        }
                        JOptionPane.showMessageDialog(null, output, Login.getTranslation().getString("errormsg"), JOptionPane.WARNING_MESSAGE);
                    }
            }

        });
        panel.add(registerButton);

        return panel;
    }

    /**
     * The password checker checks if the password and the repeated password is identical.
     * It also check the length where minimus iss 8 characters
     * @param a Users password
     * @param b Users repeated password
     */

    public boolean pwdChecker(char[] a, char[] b) {
        int length;
        if (a == null || b == null) {
            errorsNumb++;
            errors[errorsNumb] = Login.getTranslation().getString("registerNoPassword");
            return false;
        }
        if (a.length != b.length) {
            errorsNumb++;
            errors[errorsNumb] = Login.getTranslation().getString("registerPasswordNoMatch");
            return false;
        }
        else
            length = a.length;
        if (length < 8) {
            errorsNumb++;
            errors[errorsNumb] = Login.getTranslation().getString("registerPasswordTooShort");
            return false;
        }
        for (int i = 0; i < length; i++){
            if (a[i] != b[i]) {
                errorsNumb++;
                errors[errorsNumb] = Login.getTranslation().getString("registerPasswordNoMatch");
                return false;
            }
        }

        return true;
    }

    /**
     * the email checker contains a regex which check if a email is valid.
     * Or if the email is empty
     * @param email Users email
     */

    public boolean emailChecker(String email) {
        if (email == null) {
            errorsNumb++;
            errors[errorsNumb] = Login.getTranslation().getString("registerNoEmail");
            return false;
        }
        String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
        java.util.regex.Matcher m = p.matcher(email);
        if(!m.matches()) {
            errorsNumb++;
            errors[errorsNumb] = Login.getTranslation().getString("registerEmailNotValid");
        }
        return m.matches();
    }

    /**
     * The username checker checks if the username is set and has max length
     * of 15 characters and min 3 characters.
     * Also check if its empty
     * @param username User username
     */
    public boolean usernameChecker (String username) {
        if (username == null) {
            errorsNumb++;
            errors[errorsNumb] = Login.getTranslation().getString("registerNoUsername");
            return false;
        }
        if (username.length() < 3 || username.length() > 15) {
            if (username.length() < 3) {
                errorsNumb++;
                errors[errorsNumb] = Login.getTranslation().getString("registerShortUsername");
            }
            if (username.length() > 15) {
                errorsNumb++;
                errors[errorsNumb] = Login.getTranslation().getString("registerLongUsername");
            }
            return false;
        }
        else
            return true;
    }

    /**
     * SHA-256 function. Hashed the password with SHA256 with the email as salt
     * Source: http://howtodoinjava.com/2013/07/22/how-to-generate-secure-password-hash-md5-sha-pbkdf2-bcrypt-examples/
     * @param passwordToHash Users password
     * @param salt Users email
     */
    //
    private static String getSHA256(String passwordToHash, String salt)
    {
        String generatedPassword = null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(salt.getBytes());
            byte[] bytes = md.digest(passwordToHash.getBytes());
            StringBuilder sb = new StringBuilder();
            for(int i=0; i< bytes.length ;i++)
            {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            generatedPassword = sb.toString();
        }
        catch (NoSuchAlgorithmException e)
        {
            logger.log(Level.SEVERE, "an exception was thrown", e);
        }
        return generatedPassword;
    }
    /**
     * register connect to the server for registration of a new user.
     * User data will be added to the database.
     * @param username Users name
     * @param email Users email
     * @param hashedPassword Users SHA456 hased password
     *
     * */
    public void register(String username, String email, String hashedPassword) {
        try {
            loginClient = new Socket(Constants.getServerIP(), Constants.getLoginPortNumber());
            output = new PrintWriter(new OutputStreamWriter(loginClient.getOutputStream()));
            input = new BufferedReader(new InputStreamReader(loginClient.getInputStream()));

            output.printf("REGISTER|%s|%s|%s%n", email, hashedPassword, username);
            output.flush();
            String feedBack = input.readLine();
            System.out.println(feedBack);
            input.close();
            output.close();
            loginClient.close();
            if (!feedBack.startsWith("REGISTRATION OK")) {
                System.out.print("fail");
                JOptionPane.showMessageDialog(null, Login.getTranslation().getString("newUserCreatedFail"), Login.getTranslation().getString("newUser"), JOptionPane.WARNING_MESSAGE);
            }
            else
                JOptionPane.showMessageDialog(null, Login.getTranslation().getString("newUserCreated"), Login.getTranslation().getString("newUser"), JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, Login.getTranslation().getString("newUserCreatedFail"), Login.getTranslation().getString("newUser"), JOptionPane.WARNING_MESSAGE);
            logger.log(Level.SEVERE, "an exception was thrown", ex);
        }

    }
    /**
     * Checks if IP is valid format.
     * Source https://stackoverflow.com/questions/4581877/validating-ipv4-string-in-java
     * @param ip Input IP
     */

    public boolean validIP (String ip) {
            if ( ip == null || ip.isEmpty() ) {
                errorsNumb++;
                errors[errorsNumb] = Login.getTranslation().getString("registerIPEmpty");
                return false;
            }
            String[] parts = ip.split( "\\." );
            if ( parts.length != 4 ) {
                errorsNumb++;
                errors[errorsNumb] = Login.getTranslation().getString("registerIPShort");
                return false;
            }
            for ( String s : parts ) {
                int i = Integer.parseInt( s );
                if ( (i < 0) || (i > 255) ) {
                    errorsNumb++;
                    errors[errorsNumb] = Login.getTranslation().getString("registerIPWrongNumb");
                    return false;
                }
            }
            if ( ip.endsWith(".") ) {
                errorsNumb++;
                errors[errorsNumb] = Login.getTranslation().getString("registerIPEndDot");
                return false;
            }
            return true;
    }

    /**
     * Return the user to login screen.
     * Its removes the current registration panel and set it back to login
     */
    public void toLogin() {
        jFrame.remove(panel);
        jFrame.setPreferredSize(new Dimension(350, 190));
        jFrame.add(loginPanel);
        jFrame.pack();
    }
}
