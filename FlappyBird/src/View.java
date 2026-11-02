import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class View extends JPanel {
    int width = 360;
    int height = 640;
    private Logic logic;
    private JLabel scoreLabel;
    private JLabel bestScoreLabel; // Label baru untuk best score
    private Image backgroundImage;

    // Tombol Navigasi di dalam Game
    private JButton pauseBtn;
    private JButton resumeBtn;
    private JButton menuBtnGameOver;
    private JButton menuBtnPause;

    public View(Logic logic, Runnable onReturnToMenu) {
        this.logic = logic;
        setPreferredSize(new Dimension(width, height));
        setBackground(Color.cyan);
        setFocusable(true);
        addKeyListener(logic);
        setLayout(null);

        backgroundImage = new ImageIcon(getClass().getResource("/assets/background.png")).getImage();

        // Label Skor Saat Ini
        scoreLabel = new JLabel("Score: 0");
        scoreLabel.setFont(new Font("Arial", Font.BOLD, 20));
        scoreLabel.setForeground(Color.BLACK);
        scoreLabel.setBounds(10, 10, 120, 30);
        add(scoreLabel);

        // Label Best Score
        bestScoreLabel = new JLabel("Best: " + logic.getBestScore());
        bestScoreLabel.setFont(new Font("Arial", Font.BOLD, 16));
        bestScoreLabel.setForeground(Color.BLACK);
        bestScoreLabel.setBounds(10, 35, 120, 30);
        add(bestScoreLabel);

        // Tombol Pause (Selalu ada di pojok kanan atas saat main)
        pauseBtn = new JButton("⏸ Pause");
        pauseBtn.setBounds(250, 10, 95, 30);
        pauseBtn.addActionListener(e -> {
            if (!logic.isGameOver() && !logic.isPaused()) {
                logic.pauseGame();
                pauseBtn.setVisible(false);
                resumeBtn.setVisible(true);
                menuBtnPause.setVisible(true);
                repaint();
            }
        });
        add(pauseBtn);

        // Tombol Resume (Muncul saat dipause)
        resumeBtn = new JButton("▶ Lanjutkan");
        resumeBtn.setBounds(110, 240, 140, 40);
        resumeBtn.setVisible(false);
        resumeBtn.addActionListener(e -> {
            if (logic.isPaused()) {
                logic.resumeGame();
                resumeBtn.setVisible(false);
                menuBtnPause.setVisible(false);
                pauseBtn.setVisible(true);
                requestFocusInWindow();
            }
        });
        add(resumeBtn);

        // Tombol Menu saat dipause
        menuBtnPause = new JButton("🏠 Main Menu");
        menuBtnPause.setBounds(110, 295, 140, 40);
        menuBtnPause.setVisible(false);
        menuBtnPause.addActionListener(e -> {
            SoundPlayer.stopBGM();
            onReturnToMenu.run();
        });
        add(menuBtnPause);

        // Tombol Menu saat Game Over
        menuBtnGameOver = new JButton("🏠 Main Menu");
        menuBtnGameOver.setBounds(110, 370, 140, 40);
        menuBtnGameOver.setVisible(false);
        menuBtnGameOver.addActionListener(e -> {
            SoundPlayer.stopBGM();
            onReturnToMenu.run();
        });
        add(menuBtnGameOver);
    }

    // Fungsi update UI menerima parameter tambahan bestScore
    public void updateScore(int score, int bestScore) {
        scoreLabel.setText("Score: " + score);
        bestScoreLabel.setText("Best: " + bestScore);
    }

    public void checkStateUI() {
        boolean isOver = logic.isGameOver();
        boolean isPaused = logic.isPaused();

        pauseBtn.setVisible(!isOver && !isPaused);
        resumeBtn.setVisible(isPaused);
        menuBtnPause.setVisible(isPaused);
        menuBtnGameOver.setVisible(isOver);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
        checkStateUI();
    }

    public void draw(Graphics g) {
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, width, height, null);
        }

        Player player = logic.getPlayer();
        if (player != null) {
            g.drawImage(player.getImage(), player.getPosX(), player.getPosY(),
                    player.getWidth(), player.getHeight(), null);
        }

        ArrayList<Pipe> pipes = logic.getPipes();
        if (pipes != null) {
            for (Pipe pipe : pipes) {
                g.drawImage(pipe.getImage(), pipe.getPosX(), pipe.getPosY(),
                        pipe.getWidth(), pipe.getHeight(), null);
            }
        }

        // Tampilan Saat Pause
        if (logic.isPaused() && !logic.isGameOver()) {
            g.setColor(new Color(0, 0, 0, 150));
            g.fillRect(0, 0, width, height);
            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, 36));
            g.drawString("PAUSED", 110, 190);
        }

        // Tampilan Saat Game Over
        if (logic.isGameOver()) {
            g.setColor(new Color(0, 0, 0, 150));
            g.fillRect(0, 0, width, height);
            g.setColor(Color.RED);
            g.setFont(new Font("Arial", Font.BOLD, 36));
            g.drawString("GAME OVER", 75, 230);
            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.PLAIN, 16));
            g.drawString("Tekan 'R' untuk Restart", 100, 280);
        }
    }
}