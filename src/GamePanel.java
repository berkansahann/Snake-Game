import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;

public class GamePanel extends JPanel implements ActionListener {

    static final int SCREEN_WIDTH = 600;
    static final int SCREEN_HEIGHT = 600;
    static final int UNIT_SIZE = 25;
    static final int DELAY = 75;
    int[] x;
    int[] y;
    int bodyParts;
    int applesEaten;
    int appleX;
    int appleY;
    char direction;
    boolean running = false;
    boolean gameOver = false;
    Timer timer;
    Random random;
    JButton restartButton;
    JButton startButton;

    GamePanel() {
        random = new Random();
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setBackground(Color.gray);
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());
        startGameButton();
    }

    public void startGameButton() {
        startButton = new JButton("start");
        startButton.setPreferredSize(new Dimension(100, 40));
        startButton.addActionListener(this);
        startButton.setBackground(Color.lightGray);
        startButton.setFont(new Font("Courier", Font.BOLD, 30));
        startButton.setBorder(BorderFactory.createLineBorder(Color.black, 3));
        add(startButton);
    }

    public void startGame() {
        running = true;
        direction = 'R';
        x = new int[SCREEN_WIDTH];
        y = new int[SCREEN_HEIGHT];
        applesEaten = 0;
        bodyParts = 6;
        timer = new Timer(DELAY, this);
        timer.start();
        newApple();
    }

    public void newApple() {
        appleX = random.nextInt((SCREEN_WIDTH / UNIT_SIZE)) * UNIT_SIZE;
        appleY = random.nextInt((SCREEN_HEIGHT / UNIT_SIZE)) * UNIT_SIZE;
    }

    public void move() {
        for (int i = bodyParts; i > 0; i--) {
            x[i] = x[i - 1];
            y[i] = y[i - 1];
        }

        switch (direction) {
            case 'U' -> y[0] = y[0] - UNIT_SIZE;
            case 'D' -> y[0] = y[0] + UNIT_SIZE;
            case 'L' -> x[0] = x[0] - UNIT_SIZE;
            case 'R' -> x[0] = x[0] + UNIT_SIZE;
        }
    }

    public void checkApple() {
        if ((x[0] == appleX) && (y[0] == appleY)) {
            bodyParts++;
            applesEaten++;
            newApple();
        }
    }

    public void collisions() {
        //checks if head collides with body
        for (int i = bodyParts; i > 0; i--) {
            if ((x[0] == x[i]) && (y[0] == y[i])) {
                running = false;
                gameOver = true;
                break;
            }
        }
        //checks if head touches left border
        if (x[0] < 0) {
            running = false;
            gameOver = true;
        }
        //checks if head touches right border
        if (x[0] > SCREEN_WIDTH - UNIT_SIZE) {
            running = false;
            gameOver = true;
        }
        //checks if head touches top border
        if (y[0] < 0) {
            running = false;
            gameOver = true;
        }
        //checks if head touches bottom border
        if (y[0] > SCREEN_HEIGHT - UNIT_SIZE) {
            running = false;
            gameOver = true;
        }

        if (gameOver) {
            timer.stop();
        }
        
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {
        if (running) {
            drawScore(g);
            g.setColor(Color.red);
            g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE);

            for (int i = 0; i < bodyParts; i++) {
                if (i == 0) {
                    g.setColor(new Color (231, 177, 10));
                    g.fillOval(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                } else {
                    g.setColor(new Color (220, 132, 73));
                    g.fillOval(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                }
            }
        } else if (gameOver){
            gameOver(g);
            drawScore(g);
        }
    }

    public void drawScore(Graphics g) {
        g.setColor(Color.white);
        g.setFont(new Font("", Font.BOLD, 40));
        FontMetrics metrics = getFontMetrics(g.getFont());
        g.drawString("Score: " + applesEaten, (SCREEN_WIDTH - metrics.stringWidth("Score: " + applesEaten)) / 2, g.getFont().getSize());
    }

    public void gameOver(Graphics g) {
        //Game Over Texts
        g.setColor(Color.pink);
        g.setFont(new Font("", Font.BOLD, 75));
        FontMetrics metrics1 = getFontMetrics(g.getFont());
        g.drawString("Game Over", (SCREEN_WIDTH - metrics1.stringWidth("GameOver")) / 2, SCREEN_HEIGHT / 2);
        drawScore(g);
        restartGame();
    }

    public void restartGame() {
        restartButton = new JButton("Restart");
        restartButton.setBounds((SCREEN_WIDTH / 2) - (UNIT_SIZE * 2), (SCREEN_HEIGHT - 70), 100, 40);
        restartButton.addActionListener(this);
        restartButton.setBackground(Color.lightGray);
        restartButton.setFont(new Font("Courier", Font.BOLD, 15));
        restartButton.setBorder(BorderFactory.createLineBorder(Color.black, 3));
        add(restartButton);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (running) {
            move();
            checkApple();
            collisions();
        }
        if (e.getSource() == restartButton) {
            remove(restartButton);
            gameOver = false;
            startGame();
        }
        if (e.getSource() == startButton) {
            remove(startButton);
            startGame();
        }
        repaint(); //for screen refresh
    }

    public class MyKeyAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_LEFT -> {
                    if (direction != 'R') {
                        direction = 'L';
                    }
                }
                case KeyEvent.VK_RIGHT -> {
                    if (direction != 'L') {
                        direction = 'R';
                    }
                }
                case KeyEvent.VK_UP -> {
                    if (direction != 'D') {
                        direction = 'U';
                    }
                }
                case KeyEvent.VK_DOWN -> {
                    if (direction != 'U') {
                        direction = 'D';
                    }
                }
            }
        }
    }

}
