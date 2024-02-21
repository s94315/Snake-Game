import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;

// Main class for the Snake Game
public class SnakeGame extends JPanel implements ActionListener, KeyListener {
    // Inner class representing a tile in the game grid
    private class Tile {
        int x;
        int y;

        // Constructor to initialize the position of the tile
        Tile(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }  

    int boardWidth; // Width of the game board
    int boardHeight; // Height of the game board
    int tileSize = 25; // Size of each tile

    // Variables for the snake
    Tile snakeHead; // Represents the head of the snake
    ArrayList<Tile> snakeBody; // Represents the body of the snake

    // Variable for the food
    Tile food; // Represents the food for the snake
    Random random; // Random number generator for food placement

    // Variables for game logic
    int velocityX; // X-axis velocity of the snake
    int velocityY; // Y-axis velocity of the snake
    Timer gameLoop; // Timer for the game loop

    boolean gameOver = false; // Flag to indicate if the game is over

    // Constructor for the SnakeGame class
    SnakeGame(int boardWidth, int boardHeight) {
        // Initialize the dimensions of the game board
        this.boardWidth = boardWidth;
        this.boardHeight = boardHeight;

        // Set the preferred size of the panel
        setPreferredSize(new Dimension(this.boardWidth, this.boardHeight));

        // Set the background color of the panel
        setBackground(Color.black);

        // Add key listener to handle user input
        addKeyListener(this);
        setFocusable(true);

        // Initialize the snake head and body
        snakeHead = new Tile(4, 4);
        snakeBody = new ArrayList<Tile>();

        // Initialize the food and random number generator
        food = new Tile(10, 10);
        random = new Random();
        placeFood();

        // Set initial velocity of the snake
        velocityX = 1;
        velocityY = 0;
        
        // Initialize game timer
        gameLoop = new Timer(80, this); // Timer interval is 80 milliseconds
        gameLoop.start(); // Start the game loop
    }	

    // Method to paint the components of the game
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g); // Call the draw method to render game elements
    }

    int highScore = 0; // Variable to store the high score

    // Method to draw game elements
    public void draw(Graphics g) {
        // Draw the food
        g.setColor(Color.red);
        g.fillOval(food.x*tileSize, food.y*tileSize, tileSize, tileSize);

        // Draw the snake head
        g.setColor(Color.green);
        g.fill3DRect(snakeHead.x*tileSize, snakeHead.y*tileSize, tileSize, tileSize, true);
        
        // Draw the snake body
        for (int i = 0; i < snakeBody.size(); i++) {
            Tile snakePart = snakeBody.get(i);
            g.setColor(new Color(30, 144, 255)); // Dodger Blue
            g.fill3DRect(snakePart.x*tileSize, snakePart.y*tileSize, tileSize, tileSize, true);
        }

        // Draw the score
        g.setColor(Color.white);
        g.setFont(new Font("Arial", Font.BOLD, 16));
        g.drawString("Score: " + snakeBody.size(), 10, 20);
        g.drawString("High Score: " + highScore, 10, 40);

        // Draw "Game Over" message if the game is over
        if (gameOver) {
            g.setFont(new Font("Arial", Font.BOLD, 32));
            FontMetrics metrics = g.getFontMetrics();
            String gameOverText = "GAME OVER";
            int x = (boardWidth - metrics.stringWidth(gameOverText)) / 2;
            int y = (boardHeight - metrics.getHeight()) / 2 + metrics.getAscent();
            g.drawString(gameOverText, x, y);
        }
    }

    // Method to place the food at a random position on the board
    public void placeFood(){
        food.x = random.nextInt(boardWidth/tileSize);
        food.y = random.nextInt(boardHeight/tileSize);
    }

    // Method to move the snake
    public void move() {
        // Eat food if the snake collides with it
        if (collision(snakeHead, food)) {
            snakeBody.add(new Tile(food.x, food.y));
            placeFood();
            if (snakeBody.size() > highScore) {
                highScore = snakeBody.size();
            }
        }

        // Move the snake body
        for (int i = snakeBody.size()-1; i >= 0; i--) {
            Tile snakePart = snakeBody.get(i);
            if (i == 0) { // Move the first part of the body to the position of the head
                snakePart.x = snakeHead.x;
                snakePart.y = snakeHead.y;
            }
            else {
                Tile prevSnakePart = snakeBody.get(i-1);
                snakePart.x = prevSnakePart.x;
                snakePart.y = prevSnakePart.y;
            }
        }

        // Move the snake head
        snakeHead.x += velocityX;
        snakeHead.y += velocityY;

        // Check for game over conditions
        for (int i = 0; i < snakeBody.size(); i++) {
            Tile snakePart = snakeBody.get(i);

            // Collide with snake body
            if (collision(snakeHead, snakePart)) {
                gameOver = true;
            }
        }

        // Check if the snake hits the borders of the game board
        if (snakeHead.x*tileSize < 0 || snakeHead.x*tileSize > boardWidth || 
            snakeHead.y*tileSize < 0 || snakeHead.y*tileSize > boardHeight ) {
            gameOver = true;
        }
    }

    // Method to check for collision between two tiles
    public boolean collision(Tile tile1, Tile tile2) {
        return tile1.x == tile2.x && tile1.y == tile2.y;
    }

    // Method called on each game tick by the timer
    @Override
    public void actionPerformed(ActionEvent e) {
        move(); // Move the snake
        repaint(); // Repaint the panel to update the game display
        if (gameOver) {
            gameLoop.stop(); // Stop the game loop if the game is over
        }
    }  

    // Method to handle key presses
    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_UP && velocityY != 1) {
            velocityX = 0;
            velocityY = -1;
        }
        else if (e.getKeyCode() == KeyEvent.VK_DOWN && velocityY != -1) {
            velocityX = 0;
            velocityY = 1;
        }
        else if (e.getKeyCode() == KeyEvent.VK_LEFT && velocityX != 1) {
            velocityX = -1;
            velocityY = 0;
        }
        else if (e.getKeyCode() == KeyEvent.VK_RIGHT && velocityX != -1) {
            velocityX = 1;
            velocityY = 0;
        }
        else if (e.getKeyCode() == KeyEvent.VK_SPACE && gameOver) {
            restartGame(); // Restart the game if space is pressed and the game is over
        }
    }
    
    // Method to restart the game
    public void restartGame() {
        // Reset all game variables
        snakeHead = new Tile(5, 5);
        snakeBody.clear();
        placeFood();
        velocityX = 1;
        velocityY = 0;
        gameOver = false;
        gameLoop.start(); // Restart the game loop
        repaint(); // Repaint the panel to update the game display
    }

    // Not needed
    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyReleased(KeyEvent e) {}

    // Main method to run the game
    public static void main(String[] args) {
        // Create a JFrame to hold the game panel
        JFrame frame = new JFrame("Snake Game");
        SnakeGame game = new SnakeGame(600, 600); // Create an instance of the SnakeGame class
        frame.add(game); // Add the game panel to the frame
        frame.pack(); // Pack the frame to fit the preferred size of the game panel
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Set default close operation
        frame.setLocationRelativeTo(null); // Center the frame on the screen
        frame.setVisible(true); // Make the frame visible
    }
}
