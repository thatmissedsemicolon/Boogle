// BoggleBoard.java
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class BoggleBoard extends JPanel {
    private static final char[][] LETTERS = {
        { 'A', 'B', 'C', 'D' },
        { 'E', 'F', 'G', 'H' },
        { 'I', 'J', 'K', 'L' },
        { 'M', 'N', 'O', 'P' },
        { 'Q', 'R', 'S', 'T' },
        { 'U', 'V', 'W', 'X' },
    };

    private StringBuilder currentWord = new StringBuilder();

    // BoggleBoard constructor
    public BoggleBoard(JLabel currentWordDisplay) {
        setLayout(new GridLayout(6, 4));
        generateBoard(currentWordDisplay);
    }

    // generate board -- modify to accomodate randomized letters *
    private void generateBoard(JLabel currentWordDisplay) {
        for (int row = 0; row < 6; row++) {
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
            label.setEnabled(false);
            label.setForeground(Color.BLUE); // change color to show that a letter has been previously clicked

            // display the current word on the GUI
            this.currentWordDisplay.setText("Current Word: " + currentWord.toString());
        }
    }

    // reset board and clear words
    public void resetBoard() {
        for (Component component : getComponents()) {
            JLabel label = (JLabel) component;
            label.setForeground(Color.BLACK); // Reset the letter color
            label.setEnabled(true);
        }
        this.currentWord.setLength(0);
    }
}