import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class ClientsWindow extends JFrame {

    private DefaultTableModel tableModel;

    public ClientsWindow() {
        setTitle("Клиенты");
        setSize(620, 420);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);

        // Таблица
        tableModel = new DefaultTableModel(new String[]{"ID", "Имя", "Телефон", "Email"}, 0);
        JTable table = new JTable(tableModel);
        JScrollPane scroll = new JScrollPane(table);
        scroll.setBounds(20, 20, 570, 200);
        add(scroll);

        // Поля для добавления
        JLabel nameLabel = new JLabel("Имя:");
        nameLabel.setBounds(20, 240, 60, 25);
        add(nameLabel);

        JTextField nameField = new JTextField();
        nameField.setBounds(80, 240, 150, 25);
        add(nameField);

        JLabel phoneLabel = new JLabel("Телефон:");
        phoneLabel.setBounds(240, 240, 70, 25);
        add(phoneLabel);

        JTextField phoneField = new JTextField();
        phoneField.setBounds(310, 240, 120, 25);
        add(phoneField);

        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setBounds(20, 280, 60, 25);
        add(emailLabel);

        JTextField emailField = new JTextField();
        emailField.setBounds(80, 280, 200, 25);
        add(emailField);

        JButton addButton = new JButton("Добавить");
        addButton.setBounds(310, 280, 120, 25);
        add(addButton);

        JButton toServicesButton = new JButton("Перейти к услугам");
        toServicesButton.setBounds(420, 330, 170, 30);
        add(toServicesButton);

        // Обработчики
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = nameField.getText().trim();
                String phone = phoneField.getText().trim();
                String email = emailField.getText().trim();

                if (name.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Введите имя клиента!");
                    return;
                }
                DatabaseHelper.addClient(name, phone, email);
                nameField.setText("");
                phoneField.setText("");
                emailField.setText("");
                loadClients();
            }
        });

        toServicesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new ServicesWindow().setVisible(true);
            }
        });

        loadClients();
    }

    private void loadClients() {
        tableModel.setRowCount(0);
        List<String[]> clients = DatabaseHelper.getClients();
        for (String[] row : clients) {
            tableModel.addRow(row);
        }
    }
}