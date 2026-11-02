import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.prefs.Preferences; // Tambahkan import ini

public class Logic implements ActionListener, KeyListener {
    int frameWidth = 360;
    int frameHeight = 640;

    int playerStartPosX = frameWidth / 2;
    int playerStartPosY = frameHeight / 2;
    int playerWidth = 34;
    int playerHeight = 24;

    int pipeStartPosX = frameWidth;
    int pipeStartPosY = 0;
    int pipeWidth = 64;
    int pipeHeight = 512;
    int pipeVelocityX = -3;

    Timer gameLoop;
    Timer pipesCooldown;
    int gravity = 1;

    View view;
    Image birdImage;
    Player player;

    Image lowerPipeImage;
    Image upperPipeImage;
    ArrayList<Pipe> pipes;

    boolean gameOver = false;
    boolean paused = false;
    double score = 0;

    // Variabel untuk Best Score
    int bestScore = 0;
    Preferences prefs;

    public Logic() {
        birdImage = new ImageIcon(getClass().getResource("/assets/bird.png")).getImage();
        lowerPipeImage = new ImageIcon(getClass().getResource("/assets/lowerPipe.png")).getImage();
        upperPipeImage = new ImageIcon(getClass().getResource("/assets/upperPipe.png")).getImage();

        player = new Player(playerStartPosX, playerStartPosY, playerWidth, playerHeight, birdImage);
        pipes = new ArrayList<>();

        // Memuat Best Score yang tersimpan sebelumnya
        prefs = Preferences.userNodeForPackage(Logic.class);
        bestScore = prefs.getInt("bestScore", 0);

        pipesCooldown = new Timer(1500, e -> placePipes());
        gameLoop = new Timer(1000 / 60, this);
    }

    public void startGame() {
        resetGameState();
        SoundPlayer.playBGM("/assets/bgm.wav");
        pipesCooldown.start();
        gameLoop.start();
    }

    public void pauseGame() {
        paused = true;
        pipesCooldown.stop();
        gameLoop.stop();
    }

    public void resumeGame() {
        paused = false;
        pipesCooldown.start();
        gameLoop.start();
    }

    public void setView(View view) {
        this.view = view;
        view.updateScore((int) score, bestScore); // Tampilkan Best Score sejak awal
    }

    public Player getPlayer() { return player; }
    public ArrayList<Pipe> getPipes() { return pipes; }
    public boolean isGameOver() { return gameOver; }
    public boolean isPaused() { return paused; }
    public int getBestScore() { return bestScore; }

    public void placePipes() {
        if (paused || gameOver) return;
        int randomPosY = (int) (pipeStartPosY - (pipeHeight / 4) - Math.random() * (pipeHeight / 2));
        int openingSpace = frameHeight / 4;

        Pipe upperPipe = new Pipe(pipeStartPosX, randomPosY, pipeWidth, pipeHeight, upperPipeImage);
        pipes.add(upperPipe);

        Pipe lowerPipe = new Pipe(pipeStartPosX, randomPosY + pipeHeight + openingSpace, pipeWidth, pipeHeight, lowerPipeImage);
        pipes.add(lowerPipe);
    }

    public void move() {
        if (gameOver || paused) return;

        player.setVelocityY(player.getVelocityY() + gravity);
        player.setPosY(player.getPosY() + player.getVelocityY());
        player.setPosY(Math.max(player.getPosY(), 0));

        if (player.getPosY() + player.getHeight() >= frameHeight) {
            gameOver = true;
        }

        for (int i = 0; i < pipes.size(); i++) {
            Pipe pipe = pipes.get(i);
            pipe.setPosX(pipe.getPosX() + pipeVelocityX);

            if (!pipe.isPassed() && player.getPosX() > pipe.getPosX() + pipe.getWidth()) {
                pipe.setPassed(true);
                score += 0.5;

                int currentIntScore = (int) score;

                // Cek dan simpan jika skor memecahkan rekor
                if (currentIntScore > bestScore) {
                    bestScore = currentIntScore;
                    prefs.putInt("bestScore", bestScore);
                }

                if (view != null) {
                    view.updateScore(currentIntScore, bestScore);
                }

                if (score % 1 == 0) {
                    SoundPlayer.playSFX("/assets/score.wav");
                }
            }

            if (collision(player, pipe)) {
                gameOver = true;
            }
        }

        if (gameOver) {
            pipesCooldown.stop();
            gameLoop.stop();
            SoundPlayer.stopBGM();
            SoundPlayer.playSFX("/assets/game_over.wav");
        }
    }

    private boolean collision(Player a, Pipe b) {
        return a.getPosX() < b.getPosX() + b.getWidth() &&
                a.getPosX() + a.getWidth() > b.getPosX() &&
                a.getPosY() < b.getPosY() + b.getHeight() &&
                a.getPosY() + a.getHeight() > b.getPosY();
    }

    public void restartGame() {
        resetGameState();
        SoundPlayer.playBGM("/assets/bgm.wav");
        pipesCooldown.start();
        gameLoop.start();
    }

    private void resetGameState() {
        player.setPosX(playerStartPosX);
        player.setPosY(playerStartPosY);
        player.setVelocityY(0);
        pipes.clear();
        score = 0;
        gameOver = false;
        paused = false;
        if (view != null) {
            view.updateScore((int) score, bestScore);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        move();
        if (view != null) view.repaint();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE && !gameOver && !paused) {
            player.setVelocityY(-10);
            SoundPlayer.playSFX("/assets/bird.wav");
        }
        if (e.getKeyCode() == KeyEvent.VK_R && gameOver) {
            restartGame();
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {}
    @Override
    public void keyReleased(KeyEvent e) {}
}