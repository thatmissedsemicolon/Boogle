// BoggleBoard.java
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BoggleBoard extends JPanel {
    private static final int NUM_ROWS = 4;
    private static final int NUM_COLS = 4;

    private char[][] LETTERS;   // English letters
    private String[][] SLETERS;   // Spanish letters
    private int selectedRow = -1;
    private int selectedCol = -1;

    private StringBuilder currentWord = new StringBuilder();

    // BoggleBoard constructor
    public BoggleBoard(JLabel currentWordDisplay, String lang) {
        setLayout(new GridLayout(NUM_ROWS, NUM_COLS));

        if (lang.equalsIgnoreCase("Spanish")) {
            SLETERS = new String[][]{
                {"A", "B", "C", "D"},
                {"E", "F", "G", "H"},
                {"I", "J", "K", "L"},
                {"M", "N", "O", "P"},
                {"Q", "R", "S", "T"},
                {"U", "V", "W", "X"},
                {"Y", "Z", "Ñ"},
            };

            LETTERS = convertToChar(SLETERS);
        } else {
            LETTERS = new char[][]{
                {'A', 'B', 'C', 'D'},
                {'E', 'F', 'G', 'H'},
                {'I', 'J', 'K', 'L'},
                {'M', 'N', 'O', 'P'},
                {'Q', 'R', 'S', 'T'},
                {'U', 'V', 'W', 'X'},
                {'Y', 'Z'}
            };
        }

        generateBoard(currentWordDisplay);
    }

    // converts String to Char
    private char[][] convertToChar(String[][] letters) {
        char[][] convertedLetters = new char[letters.length][];
        for (int i = 0; i < letters.length; i++) {
            convertedLetters[i] = new char[letters[i].length];
            for (int j = 0; j < letters[i].length; j++) {
                convertedLetters[i][j] = letters[i][j].charAt(0);
            }
        }
        
        return convertedLetters;
    }

    // shuffles the letters (randomize)
    private void shuffleLetters() {
        Set<Character> uniqueCharacters = new HashSet<>();
    
        // Collect unique characters from LETTERS array
        for (char[] row : LETTERS) {
            for (char letter : row) {
                uniqueCharacters.add(letter);
            }
        }
    
        // Create a list with each character appearing exactly once
        List<Character> charactersList = new ArrayList<>(uniqueCharacters);
    
        // Shuffle the list
        Collections.shuffle(charactersList);
    
        // Assign shuffled characters back to LETTERS array
        int index = 0;
        for (int row = 0; row < NUM_ROWS; row++) {
            for (int col = 0; col < NUM_COLS; col++) {
                LETTERS[row][col] = charactersList.get(index++);
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

            char selectedLetter = label.getText().charAt(0);

            if (isValidSelection(selectedLetter, label)) {
                currentWord.append(label.getText());
                label.setEnabled(false);
                label.setForeground(Color.BLUE);
                this.currentWordDisplay.setText("Current Word: " + currentWord.toString());

                selectedRow = getRowOfLabel(label);
                selectedCol = getColOfLabel(label);
            }
        }

        private boolean isValidSelection(char selectedLetter, JLabel label) {
            int currentRow = getRowOfLabel(label);
            int currentCol = getColOfLabel(label);

            if (selectedRow == -1 || selectedCol == -1) {
                return true;
            } 

            return isAdjacent(selectedRow, selectedCol, currentRow, currentCol);
        }

        private boolean isAdjacent(int row1, int col1, int row2, int col2) {
            int rowDiff = Math.abs(row1 - row2);
            int colDiff = Math.abs(col1 - col2);
    
            return (rowDiff <= 1 && colDiff <= 1) && (rowDiff + colDiff > 0);
        }

        private int getRowOfLabel(JLabel label) {
            Component[] components = getComponents();
            for (int i = 0; i < components.length; i++) {
                if (components[i] == label) {
                    return i / NUM_COLS;
                }
            }
            return -1;
        }

        private int getColOfLabel(JLabel label) {
            Component[] components = getComponents();
            for (int i = 0; i < components.length; i++) {
                if (components[i] == label) {
                    return i % NUM_COLS;
                }
            }
            return -1;
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
        selectedRow = -1;
        selectedCol = -1;
    }    
}