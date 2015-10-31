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
 * This class includes main. See bottom.
 * Class login includes the login gui which contains a label for username and password.
 * Also buttons for login and register.
 *
 * A user verification to the server happebs when the user inputs login data and click on login button.
 * If the user exist, a new Client will be created and the MainController will be loaded.
 * @Kristian
 * date 29.10.2015.
 */
public class Login extends JFrame  {
    private Internationalization internationalization;
    private ResourceBundle messages;
    private JPanel panel;
    public Client client;
    public UserAccount userAccount;
    private String username;
    private char[] password;
    public JFrame jframe;

    /**
     * Constructor which sets the i18n object with the same language as users OS. If language is
     * different from Norwegian or US English, default will be set.
     */
    public Login() {
        super("Ez-Ludo");
        internationalization = new Internationalization(System.getProperty("user.language"), System.getProperty("user.country"));
        messages = internationalization.getLang();
        createPanel();
        jframe = this;
    }

    /**
     * The function creates the login UI.
     */

    public void createPanel() {
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
                //TODO CHECK USER TO DB
                jframe.dispose();
                client = new Client(username, password);
            }
        });
        panel.add(loginButton);

        // Register Button
        JButton registerButton = new JButton(messages.getString("loginRegister"));
        registerButton.setBounds(180, 80, 80, 25);
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JPanel userAccountPanel;
                userAccount = new UserAccount(internationalization, jframe, panel);
                userAccountPanel = userAccount.createLayout();
                remove(panel);
                setPreferredSize(new Dimension(350, 230));
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
     * Main methid. Creates look and feel.
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
        login.setPreferredSize(new Dimension(350, 150));
        login.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        login.pack();
        login.setVisible(true);
    }
}
