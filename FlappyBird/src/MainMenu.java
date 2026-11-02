import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class MainMenu extends JPanel {
    public MainMenu(ActionListener startAction) {
        setPreferredSize(new Dimension(360, 640));
        setLayout(new GridBagLayout());
        setBackground(Color.CYAN);

        JButton startBtn = new JButton("Start Game");
        JButton exitBtn = new JButton("Exit Game");

        startBtn.addActionListener(startAction);
        exitBtn.addActionListener(e -> System.exit(0));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(10, 10, 10, 10);
        add(startBtn, gbc);

        gbc.gridy = 1;
        add(exitBtn, gbc);
    }
}