import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ProductsWindow extends JFrame {
    public ProductsWindow() {
        setTitle("Товары");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);

        JLabel titleLabel = new JLabel("Список товаров");
        titleLabel.setBounds(150, 30, 150, 30);
        add(titleLabel);

        JButton goToServicesButton = new JButton("Перейти к услугам");
        goToServicesButton.setBounds(120, 100, 160, 30);
        add(goToServicesButton);

        goToServicesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new ServicesWindow().setVisible(true);
            }
        });
    }
}