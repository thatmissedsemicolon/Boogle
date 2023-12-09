// BoggleBoard.java
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class BoggleBoard extends JPanel {
    private static final int NUM_ROWS = 4;
    private static final int NUM_COLS = 4;

    private char[][] LETTERS = {
        {'A', 'B', 'C', 'D'},
        {'E', 'F', 'G', 'H'},
        {'I', 'J', 'K', 'L'},
        {'M', 'N', 'O', 'P'},
        {'Q', 'R', 'S', 'T'},
        {'U', 'V', 'W', 'X'},
        {'Y', 'Z'}
    };

    private StringBuilder currentWord = new StringBuilder();

     // BoggleBoard constructor
    public BoggleBoard(JLabel currentWordDisplay) {
        setLayout(new GridLayout(NUM_ROWS, NUM_COLS));
        generateBoard(currentWordDisplay);
    }

    // shuffles the letters (randomize)
    private void shuffleLetters() {
        List<Character> lettersList = Arrays.asList(
            'A', 'B', 'C', 'D', 
            'E', 'F', 'G', 'H', 
            'I', 'J', 'K', 'L', 
            'M', 'N', 'O', 'P',
            'Q', 'R', 'S', 'T',
            'U', 'V', 'W', 'X',
            'Y', 'Z'
        );
        Collections.shuffle(lettersList);

        int index = 0;
        for (int row = 0; row < NUM_ROWS; row++) {
            for (int col = 0; col < NUM_COLS; col++) {
                LETTERS[row][col] = lettersList.get(index++);
            }
        }
    }

    // rotates the letter clockwise
    public void rotateLettersClockwise() {
        char[][] rotatedLetters = new char[NUM_COLS][NUM_ROWS];

        for (int row = 0; row < NUM_ROWS; row++) {
            for (int col = 0; col < NUM_COLS; col++) {
                rotatedLetters[col][NUM_ROWS - 1 - row] = LETTERS[row][col];
            }
        }

        LETTERS = rotatedLetters;
        updateBoard();
    }

    // rotates the letter counter-clockwise
    public void rotateLettersCounterClockwise() {
        char[][] rotatedLetters = new char[NUM_COLS][NUM_ROWS];

        for (int row = 0; row < NUM_ROWS; row++) {
            for (int col = 0; col < NUM_COLS; col++) {
                rotatedLetters[NUM_COLS - 1 - col][row] = LETTERS[row][col];
            }
        }

        LETTERS = rotatedLetters;
        updateBoard();
    }

    // updates the board
    private void updateBoard() {
        removeAll();
        for (int row = 0; row < NUM_ROWS; row++) {
            for (int col = 0; col < NUM_COLS; col++) {
                char letter = LETTERS[row][col];
                JLabel label = new JLabel(Character.toString(letter), SwingConstants.CENTER);
                label.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                label.addMouseListener(new LetterClickListener(BoggleGame.currentWordDisplay));
                add(label);
            }
        }
        revalidate();
        repaint();
    }    

    // generates board
    public void generateBoard(JLabel currentWordDisplay) {
        shuffleLetters();
        updateBoard();
        for (Component component : getComponents()) {
            JLabel label = (JLabel) component;
            label.addMouseListener(new LetterClickListener(currentWordDisplay));
        }
    }

    // inner class -- handle mouse clicks on letters
    class LetterClickListener extends MouseAdapter {
        private JLabel currentWordDisplay;

        // constructor
        public LetterClickListener(JLabel currentWordDisplay) {
            this.currentWordDisplay = currentWordDisplay;
        }

        // mouse click event handling
        @Override
        public void mouseClicked(MouseEvent e) {
            JLabel label = (JLabel) e.getSource();

            // check if label has already been clicked (resolves/prevents double-counting of clicks)
            if (!label.isEnabled()) {
                return;
            }

            // append the licked letter to the current word
            currentWord.append(label.getText());
            label.setEnabled(false);
            label.setForeground(Color.BLUE);

            // display the current word on the GUI
            this.currentWordDisplay.setText("Current Word: " + currentWord.toString());
        }
    }

    // reset board and clear words
    public void resetBoard() {
        for (Component component : getComponents()) {
            if (component instanceof JLabel) {
                JLabel label = (JLabel) component;
                label.setForeground(Color.BLACK);
                label.setEnabled(true);
            }
        }
        currentWord.setLength(0);
    }
}
