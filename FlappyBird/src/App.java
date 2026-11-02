import javax.swing.*;
import java.awt.*;

public class App {
    public static void main(String[] args) {
        JFrame frame = new JFrame("FlappyBird");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(360, 640);
        frame.setResizable(false);

        CardLayout cardLayout = new CardLayout();
        JPanel mainPanel = new JPanel(cardLayout);

        Logic logika = new Logic();

        // Membuat fungsi callback untuk kembali ke Main Menu dari dalam game
        Runnable returnToMenuAction = () -> {
            logika.pauseGame();
            cardLayout.show(mainPanel, "Menu");
            SoundPlayer.playBGM("/assets/main_menu.wav");
        };

        View tampilan = new View(logika, returnToMenuAction);
        logika.setView(tampilan);

        MainMenu menu = new MainMenu(e -> {
            SoundPlayer.stopBGM(); // Hentikan musik menu saat mulai
            cardLayout.show(mainPanel, "Game");
            tampilan.requestFocusInWindow();
            logika.startGame();
        });

        mainPanel.add(menu, "Menu");
        mainPanel.add(tampilan, "Game");

        frame.add(mainPanel);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        // Putar musik Main Menu saat aplikasi pertama kali berjalan
        SoundPlayer.playBGM("/assets/main_menu.wav");
    }
}