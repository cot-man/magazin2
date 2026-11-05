import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class ServicesWindow extends JFrame {

    private DefaultTableModel tableModel;

    public ServicesWindow() {
        setTitle("Услуги");
        setSize(620, 420);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);

        // Таблица
        tableModel = new DefaultTableModel(new String[]{"ID", "Название", "Цена"}, 0);
        JTable table = new JTable(tableModel);
        JScrollPane scroll = new JScrollPane(table);
        scroll.setBounds(20, 20, 570, 200);
        add(scroll);

        // Поля для добавления
        JLabel nameLabel = new JLabel("Название:");
        nameLabel.setBounds(20, 240, 80, 25);
        add(nameLabel);

        JTextField nameField = new JTextField();
        nameField.setBounds(100, 240, 200, 25);
        add(nameField);

        JLabel priceLabel = new JLabel("Цена:");
        priceLabel.setBounds(320, 240, 60, 25);
        add(priceLabel);

        JTextField priceField = new JTextField();
        priceField.setBounds(370, 240, 100, 25);
        add(priceField);

        JButton addButton = new JButton("Добавить");
        addButton.setBounds(480, 240, 110, 25);
        add(addButton);

        JButton backButton = new JButton("Вернуться к клиентам");
        backButton.setBounds(410, 320, 180, 30);
        add(backButton);

        // Обработчики
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = nameField.getText().trim();
                String priceStr = priceField.getText().trim();

                if (name.isEmpty() || priceStr.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Заполните все поля!");
                    return;
                }
                try {
                    double price = Double.parseDouble(priceStr);
                    DatabaseHelper.addService(name, price);
                    nameField.setText("");
                    priceField.setText("");
                    loadServices();
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Цена должна быть числом!");
                }
            }
        });

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new ClientsWindow().setVisible(true);
            }
        });

        loadServices();
    }

    private void loadServices() {
        tableModel.setRowCount(0);
        List<String[]> services = DatabaseHelper.getServices();
        for (String[] row : services) {
            tableModel.addRow(row);
        }
    }
}