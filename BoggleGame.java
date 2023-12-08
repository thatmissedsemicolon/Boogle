import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class BoggleGame extends JFrame {

    private static final int GAME_DURATION_SECONDS = 180; // 3-minute game
    private int remainingSeconds;

    private Timer gameTimer;
    private BoggleBoard boggleBoard;
    private JLabel currentWordDisplay;
    private JTextField inputField;

    public BoggleGame() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Boggle Game");

        // initialize GUI componentss
        currentWordDisplay = new JLabel("Current Word: ");
        currentWordDisplay.setForeground(Color.BLACK);
        add(currentWordDisplay, BorderLayout.SOUTH);

        boggleBoard = new BoggleBoard(currentWordDisplay);
        inputField = new JTextField();

        // create letter click listener
        BoggleBoard.LetterClickListener letterClickListener = boggleBoard.new LetterClickListener(currentWordDisplay);

        // establish "letter click" listener for letter labels on board
        for (Component component : boggleBoard.getComponents()) {
            if (component instanceof JLabel) {
                JLabel label = (JLabel) component;
                label.addMouseListener(letterClickListener);
            }
        }

        // layout setup
        add(boggleBoard, BorderLayout.CENTER);

        // "Submit Word" and "Game Reset" buttons
        JButton submitButton = new JButton("Submit Word");
        submitButton.addActionListener(e -> handleWordSubmission(currentWordDisplay.getText()));

        JButton resetButton = new JButton("Reset Game");
        resetButton.addActionListener(e -> resetGame());

        // button panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(submitButton);
        buttonPanel.add(resetButton);

        add(buttonPanel, BorderLayout.NORTH);

        // timer label
        remainingSeconds = GAME_DURATION_SECONDS;
        JLabel timerLabel = new JLabel("Time: " + formatTime(remainingSeconds));
        timerLabel.setForeground(Color.RED);
        add(timerLabel, BorderLayout.EAST);

        // game timer
        gameTimer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                remainingSeconds--;
                timerLabel.setText("Time: " + formatTime(remainingSeconds));
                if (remainingSeconds <= 0) {
                    gameTimer.stop();
                    handleGameEnd();
                }
            }
        });
        gameTimer.start();

        // set up frame
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private String formatTime(int seconds) {
        // remaining time format: minutes:seconds
        int minutes = seconds / 60;
        int remainingSeconds = seconds % 60;
        return String.format("%d:%02d", minutes, remainingSeconds);
    }

    private void handleWordSubmission(String currentWord) {
        // word submission logic *
        // call boggleBoard.validateWord(currentWord) and update the score
        // include boggle dictionary in tar file -- enforce command line argument rules
        // i.e. java BoggleGame.jar <dictionary>

        // Clear the input field after submitting a word
        inputField.setText("");
    }

    private void resetGame() {
        boggleBoard.resetBoard();
        currentWordDisplay.setText("Current Word: ");
        remainingSeconds = GAME_DURATION_SECONDS;
    }

    private void handleGameEnd() {
        // display game over message
        JOptionPane.showMessageDialog(this, "Game Over! Time's up.");

        // calculate and display the final score (modify this part based on your scoring
        // logic)
        int finalScore = calculateFinalScore();
        JOptionPane.showMessageDialog(this, "Final Score: " + finalScore);

        // reset game
        resetGame();
    }

    private int calculateFinalScore() {
        // calculate final score *
        // consider the number of valid words, word lengths, etc according to boggle
        // rules
        return 0; // (placeholder -- replace with actual implementation)
    }

    // main method to begin the game
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new BoggleGame());
    }
}
