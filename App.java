import javax.swing.*;

public class App {
    public static void main(String[] args) throws Exception {
        // Set the dimensions of the game board
        int boardWidth = 600;
        int boardHeight = boardWidth;

        // Create a new JFrame to hold the game
        JFrame frame = new JFrame("Snake");
        frame.setVisible(true); // Make the frame visible
        frame.setSize(boardWidth, boardHeight); // Set the size of the frame
        frame.setLocationRelativeTo(null); // Center the frame on the screen
        frame.setResizable(false); // Disable resizing of the frame
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Set default close operation

        // Create an instance of the SnakeGame class
        SnakeGame snakeGame = new SnakeGame(boardWidth, boardHeight);
        frame.add(snakeGame); // Add the SnakeGame panel to the frame
        frame.pack(); // Pack the frame to fit the preferred size of the game panel
        snakeGame.requestFocus(); // Request focus for keyboard input to the game panel
    }
}
