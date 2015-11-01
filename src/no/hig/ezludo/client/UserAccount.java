package no.hig.ezludo.client;

import Internationalization.Internationalization;
import no.hig.ezludo.server.Server;

import java.io.*;
import java.net.Socket;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.security.spec.InvalidKeySpecException;
import java.util.ResourceBundle;

/**
 * This class includes the user-registration gui included userdata validation.
 * @author Kristian
 * date 30.10.2015.
 */
public class UserAccount {
    private ResourceBundle messages;
    private JPanel panel;
    private JPanel loginPanel;
    private JFrame jFrame;
    private String username;
    private char[] password;
    private char[] passwordRepeat ;
    private String email;
    private int errorsNumb=0;
    private String[] errors = new String[20];
    //Server reg
    PrintWriter output;
    BufferedReader input;
    private Socket loginClient = null;
    private final static int loginPort = 6969;


    /**
     * Constructor which gets UI-data and I18N objects from Login-class.
     */
    public UserAccount(Internationalization internationalization, JFrame jFrame, JPanel loginPanel) {
        messages = internationalization.getLang();
        this.jFrame = jFrame;
        this.loginPanel = loginPanel;
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

        // Password Repeat label
        JLabel passwordLabelRepeat = new JLabel(messages.getString("loginPasswordRepeat"));
        passwordLabelRepeat.setBounds(10, 100, 80, 25);
        panel.add(passwordLabelRepeat);

        // Password Repeat Input label
        JPasswordField passwordRepeatText = new JPasswordField(20);
        passwordRepeatText.setBounds(100,100,160,25);
        passwordRepeatText.getDocument().addDocumentListener(new DocumentListener() {
            public void changedUpdate(DocumentEvent e) {
                passwordRepeat = passwordRepeatText.getPassword();
            }

            public void removeUpdate(DocumentEvent e) {
                passwordRepeat = passwordRepeatText.getPassword();
            }

            public void insertUpdate(DocumentEvent e) {
                passwordRepeat = passwordRepeatText.getPassword();
            }
        });
        panel.add(passwordRepeatText);

        // Back Button
        JButton backButton = new JButton(messages.getString("back"));
        backButton.setBounds(10,160,80,25);
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                toLogin();
            }
        });
        panel.add(backButton);

        // Register Button
        JButton registerButton = new JButton(messages.getString("register"));
        registerButton.setBounds(180, 160, 80, 25);
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String output = "";
                    for (int i = 0; i <= 19; i++) {
                        errors[i] = null;
                    }

                    if (usernameChecker(username) && emailChecker(email) && passwordChecker(password, passwordRepeat)) {

                        String passwordToHash = String.valueOf(password);
                        String salt = getSalt();
                        String hashedPassword = getSHA256(passwordToHash, salt);

                        // TODO Register user in db
                        register(username, email, hashedPassword);
                        JOptionPane.showMessageDialog(null, messages.getString("newUserCreated"), messages.getString("newUser"), JOptionPane.INFORMATION_MESSAGE);
                        toLogin();
                    } else {
                        for (int i = 1; i <= errorsNumb; i++) {
                            if (errors[i] != null)
                                output = output + "\n" + errors[i];
                        }
                        JOptionPane.showMessageDialog(null, output, messages.getString("errormsg"), JOptionPane.WARNING_MESSAGE);
                    }
                }catch(Exception ex) {

                }
            }

        });
        panel.add(registerButton);

        return panel;
    }

    /**
     * The password checker checks if the password and the repeated password is identical.
     * It also check the length.
     */

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

    /**
     * the email checker contains a regex which check if a email is valid.
     * Or if the email is empty
     */

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

    /**
     * The username checker checks if the username is set and has max length
     * of 15 characters and min 3 characters.
     * Also check if its empty
     */

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

    /**
     * Return the user to login screen.
     * Its removes the current registration panel and set it back to login
     */

    public void toLogin() {
        jFrame.remove(panel);
        jFrame.setPreferredSize(new Dimension(350, 150));
        jFrame.add(loginPanel);
        jFrame.pack();

    }

    /**
     * SHA-256 function.
     * Source: http://howtodoinjava.com/2013/07/22/how-to-generate-secure-password-hash-md5-sha-pbkdf2-bcrypt-examples/
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
            e.printStackTrace();
        }
        return generatedPassword;
    }

    //Add salt
    private static String getSalt() throws NoSuchAlgorithmException {
            SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
            byte[] salt = new byte[16];
            sr.nextBytes(salt);
            return salt.toString();

    }

    public void register(String username, String email, String hashedPassword) {
        try {
            loginClient = new Socket("127.0.0.1", loginPort);
            output = new PrintWriter(new OutputStreamWriter(loginClient.getOutputStream()));
            input = new BufferedReader(new InputStreamReader(loginClient.getInputStream()));

            output.printf("REGISTER|%s|%s|%s\n", email, hashedPassword, username);
            output.flush();
            String feedBack = input.readLine();
            System.out.println(feedBack);
            input.close();
            output.close();
            loginClient.close();
            if (!feedBack.startsWith("REGISTRATION OK"))
                System.out.println("fail");
        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }
}
