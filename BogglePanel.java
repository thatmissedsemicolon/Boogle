import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class BogglePanel extends JPanel {
    private BoggleBoard boggleBoard;
    private JTextField inputField;
    private JTextArea outputArea;
    private JLabel timerLabel;
    private JLabel scoreLabel;
    private int seconds;
    private int score;
    private Timer gameTimer;

    public BogglePanel() {
        // main layout
        setLayout(new BorderLayout());

        // create components
        JLabel currentWordDisplay = new JLabel("Current Word: ");
        currentWordDisplay.setForeground(Color.BLACK);

        boggleBoard = new BoggleBoard(currentWordDisplay);
        inputField = new JTextField();
        outputArea = new JTextArea();
        outputArea.setEditable(false);
        timerLabel = new JLabel("Time: 0:00");
        scoreLabel = new JLabel("Score: 0");
        seconds = 0;
        score = 0;

        // add components to layout
        add(boggleBoard, BorderLayout.CENTER);
        add(inputField, BorderLayout.SOUTH);
        add(outputArea, BorderLayout.NORTH);

        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        infoPanel.add(timerLabel);
        infoPanel.add(scoreLabel);
        add(infoPanel, BorderLayout.NORTH);

        JButton submitButton = new JButton("Submit Word");
        submitButton.addActionListener(e -> handleWordSubmission());
        JButton resetButton = new JButton("Reset Game");
        resetButton.addActionListener(e -> resetGame());

        add(submitButton, BorderLayout.EAST);
        add(resetButton, BorderLayout.WEST);

        startTimer();
    }

    // word submission logic implementation
    private void handleWordSubmission() {
        String word = inputField.getText().trim();
        if (!word.isEmpty()) {
            boolean isValidWord = boggleBoard.checkWord(word);
            if (isValidWord) {
                score += calculateWordPoints(word);
                scoreLabel.setText("Score: " + score);
            }
            outputArea.append("Word: " + word + " - Valid: " + isValidWord + "\n");
            inputField.setText("");
        }
    }

    // game timer logic
    private void startTimer() {
        seconds = 0;
        gameTimer = new Timer(1000, e -> {
            seconds++;
            int minutes = seconds / 60;
            int remainingSeconds = seconds % 60;
            timerLabel.setText("Time: " + minutes + ":" +
                    (remainingSeconds < 10 ? "0" : "") + remainingSeconds);
        });
        gameTimer.start();
    }

    // game reset - restore initial game state
    private void resetGame() {
        gameTimer.stop();
        seconds = 0;
        timerLabel.setText("Time: 0:00");
        score = 0;
        scoreLabel.setText("Score: 0");
        boggleBoard.clearUsedWords();
        boggleBoard.resetBoard();
        startTimer();
    }

    // word point calculation according to rules
    private int calculateWordPoints(String word) {
        // calculate score *
        // validate words with dictionary -- to be done later *
        return word.length();
    }
}
