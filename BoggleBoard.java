// BoggleBoard.java
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashSet;
import java.util.Set;

public class BoggleBoard extends JPanel {
    // test board -- modify to randomize letters *
    private static final char[][] LETTERS = {
        { 'A', 'B', 'C', 'D' },
        { 'E', 'F', 'G', 'H' },
        { 'I', 'J', 'K', 'L' },
        { 'M', 'N', 'O', 'P' }
    };
    

    private Set<String> usedWords = new HashSet<>();
    private StringBuilder currentWord = new StringBuilder();

    // BoggleBoard constructor
    public BoggleBoard(JLabel currentWordDisplay) {
        setLayout(new GridLayout(4, 4));
        generateBoard(currentWordDisplay);
    }

    // generate board -- modify to accomodate randomized letters *
    private void generateBoard(JLabel currentWordDisplay) {
        for (int row = 0; row < 4; row++) {
            for (int col = 0; col < 4; col++) {
                char letter = LETTERS[row][col];
                JLabel label = new JLabel(Character.toString(letter), SwingConstants.CENTER);
                label.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                label.addMouseListener(new LetterClickListener(currentWordDisplay));
                add(label);
            }
        }
    }

    // inner class -- handle mouse clicks on letters
    public class LetterClickListener extends MouseAdapter {
        private JLabel currentWordDisplay;

        // constructor
        public LetterClickListener(JLabel currentWordDisplay) {
            this.currentWordDisplay = currentWordDisplay;
        }

        @Override
        // mouse click event handling
        public void mouseClicked(MouseEvent e) {
            JLabel label = (JLabel) e.getSource();

            // check if label has already been clicked (resolves/prevents double-counting of clicks)
            if (!label.isEnabled()) {
                return;
            }
            // append the licked letter to the current word
            currentWord.append(label.getText());
            label.setEnabled(false); // disable the label to prevent further clicks -- reinforces game rules
            label.setForeground(Color.BLUE); // change color to show that a letter has been previously clicked

            // revise *
            // display the current word on the GUI
            currentWordDisplay.setText("Current Word: " + currentWord.toString());
        }
    }

    // check word validity (prevent counting of already used words, etc.)
    public boolean checkWord(String word) {
        if (usedWords.contains(word)) {
            return false;
        }

        if (word.length() < 3) {
            return false;
        }

        if (!validWord(word)) {
            return false;
        }

        usedWords.add(word);
        resetCurrentWord();
        return true;
    }

    private boolean validWord(String word) {

        // word validation logic based on Boggle rules
        return true; // (placeholder -- replace with actual implementation)
    }

    // clear set of already used words
    public void clearUsedWords() {
        usedWords.clear();
    }

    // reset board and clear words
    public void resetBoard() {
        for (Component component : getComponents()) {
            JLabel label = (JLabel) component;
            label.setForeground(Color.BLACK); // Reset the letter color
        }
        resetCurrentWord();
        // adjust so that letter goes back to black once a word is submitted *
    }

    // reset current word -- call after word is submitted to let user create a new word
    private void resetCurrentWord() {
        currentWord.setLength(0); // clear StringBuilder
    }
}