package no.hig.ezludo.client;

import Internationalization.Internationalization;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ResourceBundle;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

/**
 * This class includes main. See bottom.
 * Class login includes the login gui which contains a label for username and password.
 * Also buttons for login and register.
 *
 * A user verification to the server happebs when the user inputs login data and click on login button.
 * If the user exist, a new Client will be created and the MainController will be loaded.
 * @author Kristian, Per-Kristian
 * date 29.10.2015.
 */
public class Login extends JFrame  {
    private Internationalization internationalization;
    private ResourceBundle messages;
    private JPanel panel;
    public UserAccount userAccount;
    private String email;
    private String IP;
    private char[] password = "".toCharArray();
    public JFrame jframe;
    private Socket socket;
    private static Preferences prefs;
    private JTextField userText;
    private JPasswordField passwordText;
    private JTextField IPText;
    private static final byte[] keyValue = "krb$[i@9adkk0l0}".getBytes();
    private static final String ALGORITHM = "AES";

    /**
     * Constructor which sets the i18n object with the same language as users OS. If language is
     * different from Norwegian or US English, default will be set.
     */
    public Login() {
        super("Ez-Ludo");
        // Get preferences, and see if it contains an email value
        prefs = Preferences.userNodeForPackage(getClass());

        internationalization = new Internationalization(System.getProperty("user.language"), System.getProperty("user.country"));
        messages = internationalization.getLang();
        createPanel();
        jframe = this;
        this.setIconImage(new ImageIcon(getClass().getResource("/res/board.png")).getImage());
    }

    /**
     * The function creates the login UI.
     */

    public void createPanel() {
        panel = new JPanel();
        panel.setLayout(null);

        // User label
        JLabel userLabel = new JLabel(messages.getString("loginUsername"));
        userLabel.setBounds(10, 10, 80, 25);
        panel.add(userLabel);

        // User input field
        userText = new JTextField(20);
        fillEmailField();
        userText.setBounds(100, 10, 160, 25);
        userText.getDocument().addDocumentListener(new DocumentListener() {
            public void changedUpdate(DocumentEvent e) {
                email = userText.getText();
            }

            public void removeUpdate(DocumentEvent e) {
                email = userText.getText();
            }

            public void insertUpdate(DocumentEvent e) {
                email = userText.getText();
            }
        });
        panel.add(userText);

        // Password label
        JLabel passwordLabel = new JLabel(messages.getString("loginPassword"));
        passwordLabel.setBounds(10, 40, 80, 25);
        panel.add(passwordLabel);

        // PasswordField
        passwordText = new JPasswordField(20);
        fillPasswordField();

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

        // IP label
        JLabel IPLabel = new JLabel(messages.getString("loginIP"));
        IPLabel.setBounds(10, 70, 80, 25);
        panel.add(IPLabel);

        // User input field
        IPText = new JTextField(20);
        fillIPAddressField();
        IPText.setBounds(100, 70, 160, 25);
        IPText.getDocument().addDocumentListener(new DocumentListener() {
            public void changedUpdate(DocumentEvent e) {
                IP = IPText.getText();
            }

            public void removeUpdate(DocumentEvent e) {
                IP = IPText.getText();
            }

            public void insertUpdate(DocumentEvent e) {
                IP = IPText.getText();
            }
        });
        panel.add(IPText);

        // Login Button
        JButton loginButton = new JButton(messages.getString("loginLogin"));
        loginButton.setBounds(10,110,80,25);
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed (ActionEvent e) {
                try {
                    String[] result = performLogin();
                    if (!result[0].equals("fail")) {
                        saveCredentials();
                        jframe.dispose();
                        System.out.println(result[0]);
                        System.out.println(result[2]);
                        MainController.startScene(result);
                    } else {
                     //TODO: Handle rejected login
                     System.out.println("Login failed");
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    }
            }
        });
        panel.add(loginButton);

        // Register Button
        JButton registerButton = new JButton(messages.getString("loginRegister"));
        registerButton.setBounds(180, 110, 80, 25);
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JPanel userAccountPanel;
                userAccount = new UserAccount(internationalization, jframe, panel);
                userAccountPanel = userAccount.createLayout();
                remove(panel);
                setPreferredSize(new Dimension(350, 260));
                add(userAccountPanel);
                pack();
            }
        });
        panel.add(registerButton);
        createLayout(panel);
    }

    /**
     * Sets the panel created above to the JFrame
     */
    public void createLayout(JPanel panel) {
        this.add(panel);
        this.setVisible(true);
    }

    /**
     * This method fills the email field with a locally stored email address. If there's no email stored, it sets
     * the email field to be blank.
     */
    public void fillEmailField() {
        email = prefs.get("email", "");
        userText.setText(email);
    }

    /**
     * This method fills the password field with a locally stored password. The password is encrypted, so it gets
     * decrypted before the password field is filled.
     */
    public void fillPasswordField() {
        try {
            String encryptedPassword = prefs.get("password", "");

            if (!encryptedPassword.equals("")) {
                String decryptedPassword = decrypt(encryptedPassword);
                passwordText.setText(decryptedPassword);
                password = decryptedPassword.toCharArray();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * This method fills the IP address field with a locally stored IP address. If there's no IP address saved, the
     * field is filled with an empty string.
     */
    public void fillIPAddressField() {
        String ipAddress = prefs.get("IP", "");
        IPText.setText(ipAddress);
        IP = ipAddress;
    }

    public void saveCredentials() throws Exception {
        prefs.put("email", email);
        String passwordToEncrypt = new String(password);
        prefs.put("password", encrypt(passwordToEncrypt));
        prefs.put("IP", IP);
    }
    /**
     * This method encrypts data using AES. It returns the encrypted data. Code gotten from code2learn.com, and
     * modified to suit our needs.
     * @param rawData data to be encrypted
     * @return the encrypted data
     * @throws Exception
     * @see <html><a href="http://www.code2learn.com/2011/06/encryption-and-decryption-of-data-using.html">code2learn
     * </a></html>
     */
    public String encrypt(String rawData) throws Exception {
        Key key = generateKey();
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] encVal = cipher.doFinal(rawData.getBytes());
        return new BASE64Encoder().encode(encVal);
    }

    /**
     * This method decrypts AES-encrypted data. It returns the decrypted data. Code gotten from code2learn.com, and
     * modified to suit our needs.
     * @param encryptedData the data to be decrypted
     * @return decrypted data
     * @throws Exception
     * @see <html><a href="http://www.code2learn.com/2011/06/encryption-and-decryption-of-data-using.html">code2learn
     * </a></html>
     */
    public String decrypt(String encryptedData) throws Exception {
        Key key = generateKey();
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, key);
        byte[] decodedValue = new BASE64Decoder().decodeBuffer(encryptedData);
        byte[] decValue = cipher.doFinal(decodedValue);
        return new String(decValue);
    }

    /**
     * This method generates a key for use in encrypting / decrypting the password when stored locally.
     * Code gotten from code2learn.com, and modified to suit our needs.
     * @return the key
     * @throws Exception
     * @see <html><a href="http://www.code2learn.com/2011/06/encryption-and-decryption-of-data-using.html">code2learn
     * </a></html>
     */
    private static Key generateKey() throws Exception {
        Key key = new SecretKeySpec(keyValue, ALGORITHM);
        return key;
    }

    /**
     * This class is called when the user presses the login button. It communicates with the server to see if the
     * entered username and password are valid. If they are, a new Client object is created (thus launching the Lobby
     * window), and the function returns true. If the login wasn't successful, the function returns false.
     * @return true or false depending on successful login attempt
     */
    public String[] performLogin() throws NoSuchAlgorithmException {
        try {
            if (password != null) {
                String passwordString = String.valueOf(password);
                String hashedPassword = getSHA256(passwordString, email);

                Constants.serverIP = IP;
                socket = new Socket(Constants.serverIP, Constants.loginPortNumber);
                PrintWriter output = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
                BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                // Send the login statement to the server
                output.println("LOGIN|" + email + "|" + hashedPassword);
                output.flush();

                // TODO: figure out if reading the input without looping on it works even if the DB is slow
                // Reads the respons from the server and closes connection
                String response = input.readLine();
                output.close();
                input.close();
                socket.close();

                // If the response starts with "LOGIN OK", create a new client object and send along the key received
                if (response.startsWith("LOGIN OK")) {
                    System.out.println("performLogin: " + response);
                    String key = response.split("\\|")[1];
                    String[] args = {email, new String(password), key};
                    return args;
                }
        }

        } catch(IOException exception) {
            exception.printStackTrace();
        }
        // Return "fail" message if login fails
        return new String[]{"fail"};
    }

    /**
     * SHA-256 function.
     * Source: http://howtodoinjava.com/2013/07/22/how-to-generate-secure-password-hash-md5-sha-pbkdf2-bcrypt-examples/
     */
    //
    public static String getSHA256(String passwordToHash, String salt)
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
    /**
     * Main method. Creates look and feel.
     * And creates a new login object which is placed in center of screen.
     */
    public static void main(String[] args) {
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

        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        Login login = new Login();
        login.setLocation(dim.width/2-login.getSize().width/2, dim.height/2-login.getSize().height/2);
        login.setPreferredSize(new Dimension(350, 180));
        login.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        login.pack();
        login.setVisible(true);
    }


}
