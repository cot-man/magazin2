import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AuthWindow extends JFrame {

    public AuthWindow() {
        // При старте программы создаём таблицы и админа, если их ещё нет
        DatabaseHelper.initDatabase();
        if (!DatabaseHelper.userExists("admin")) {
            DatabaseHelper.registerUser("admin", "123");
        }

        setTitle("Авторизация");
        setSize(350, 260);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);

        JLabel userLabel = new JLabel("Логин:");
        userLabel.setBounds(20, 20, 80, 25);
        add(userLabel);

        JTextField userText = new JTextField(20);
        userText.setBounds(110, 20, 200, 25);
        add(userText);

        JLabel passwordLabel = new JLabel("Пароль:");
        passwordLabel.setBounds(20, 60, 80, 25);
        add(passwordLabel);

        JPasswordField passwordText = new JPasswordField(20);
        passwordText.setBounds(110, 60, 200, 25);
        add(passwordText);

        JButton loginButton = new JButton("Войти");
        loginButton.setBounds(40, 120, 120, 30);
        add(loginButton);

        JButton registerButton = new JButton("Регистрация");
        registerButton.setBounds(180, 120, 120, 30);
        add(registerButton);

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String login = userText.getText().trim();
                String password = new String(passwordText.getPassword());

                if (DatabaseHelper.checkPassword(login, password)) {
                    dispose();
                    new ClientsWindow().setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(null,
                            "Неверный логин или пароль!",
                            "Ошибка входа",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String login = userText.getText().trim();
                String password = new String(passwordText.getPassword());

                if (login.isEmpty() || password.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Введите логин и пароль!");
                    return;
                }
                if (DatabaseHelper.userExists(login)) {
                    JOptionPane.showMessageDialog(null, "Такой пользователь уже есть!");
                    return;
                }
                DatabaseHelper.registerUser(login, password);
                JOptionPane.showMessageDialog(null, "Пользователь " + login + " зарегистрирован!");
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new AuthWindow().setVisible(true));
    }
}