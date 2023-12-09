import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collections;
import java.util.Map;

public class BoggleGame extends JFrame {
    private String[] usedWordsArray = new String[100];
    private int usedWordsCount = 0;
    private int score = 0;
    private static final int GAME_DURATION_SECONDS = 180; // 3-minute game
    private int remainingSeconds;
    private static String userName = "";

    private Timer gameTimer;
    private BoggleBoard boggleBoard;

    public static JLabel currentWordDisplay;
    private JLabel scoreLabel = new JLabel();
    private JTextField inputField;
    private static JLabel highestScorerLabel = new JLabel();
    private static JButton playButton, rulesButton, exitButton;
    private static JFrame menuFrame;

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
        submitButton.addActionListener(e -> handleWordSubmission(currentWordDisplay.getText(), "BoggleDictionary_CSW21.txt"));

        JButton resetButton = new JButton("Reset Game");
        resetButton.addActionListener(e -> resetGame());

        // "Submit Word" and "Game Reset" buttons
        JButton rotateButton = new JButton("Rotate Word");
        rotateButton.addActionListener(e -> boggleBoard.rotateLettersClockwise());

        // button panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(submitButton);
        buttonPanel.add(resetButton);
        buttonPanel.add(rotateButton);

        add(buttonPanel, BorderLayout.NORTH);

        // timer label
        this.remainingSeconds = GAME_DURATION_SECONDS;
        JLabel timerLabel = new JLabel("Time: " + formatTime(remainingSeconds));
        timerLabel.setForeground(Color.RED);
        add(timerLabel, BorderLayout.EAST);

        this.scoreLabel.setText("Score: " + score);
        this.scoreLabel.setForeground(Color.BLACK);
        add(scoreLabel, BorderLayout.WEST);

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
        this.gameTimer.start();

        // set up frame
        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    private String formatTime(int seconds) {
        // remaining time format: minutes:seconds
        int minutes = seconds / 60;
        int remainingSeconds = seconds % 60;
        return String.format("%d:%02d", minutes, remainingSeconds);
    }

    private void handleWordSubmission(String currentWord, String dictionaryFileName) {
        // Check if the word exists in the dictionary file

        if (currentWord.startsWith("Current Word: ")) {
            currentWord = currentWord.substring("Current Word: ".length()).trim();
        }

        boolean wordExists = isWordInDictionary(currentWord, dictionaryFileName);
        boolean wordAlreadyUsed = isWordUsed(currentWord);

        if (wordExists && !wordAlreadyUsed) {
            // Increase the score by the length of the word
            int wordLength = currentWord.length(); // Extract the word length
            this.score += wordLength;
            this.scoreLabel.setText("Score: " + score);

            // Store the word in the usedWords stack
            this.usedWordsArray[usedWordsCount++] = currentWord;

            this.boggleBoard.resetBoard();
            currentWordDisplay.setText("Current Word: ");
        } else {
            if (wordAlreadyUsed) {
                JOptionPane.showMessageDialog(this, "Word already used!");
            } else {
                JOptionPane.showMessageDialog(this, "Invalid word!");
            }

            this.boggleBoard.resetBoard();
            currentWordDisplay.setText("Current Word: ");
        }

        // Clear the input field after submitting a word
        this.inputField.setText("");
    }

    // checks for the word if its already used
    private boolean isWordUsed(String word) {
        // Check if the word is already in the usedWordsArray
        for (int i = 0; i < usedWordsCount; i++) {
            if (this.usedWordsArray[i] != null && this.usedWordsArray[i].equalsIgnoreCase(word)) {
                return true;
            }
        }
        return false;
    }

    // checks for the word in the Boggle Dictionary 
    private boolean isWordInDictionary(String word, String dictionaryFileName) {
        boolean wordExists = false;

        try (BufferedReader br = new BufferedReader(new FileReader(dictionaryFileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.equalsIgnoreCase(word)) {
                    wordExists = true;
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return wordExists;
    }

    // reset the game
    private void resetGame() {
        this.boggleBoard.resetBoard();
        currentWordDisplay.setText("Current Word: ");
        this.remainingSeconds = GAME_DURATION_SECONDS;
        this.score = 0;
    
        // Reset the score level
        this.scoreLabel.setText("Score: " + score);
    
        // Reset used words array and count
        this.usedWordsArray = new String[100];
        this.usedWordsCount = 0;

        // Generates the new letters on the board
        boggleBoard.generateBoard(currentWordDisplay);
    }

    // when game ends
    private void handleGameEnd() {
        ScoreManager.updateScore(userName, score);

        int playAgain = JOptionPane.showConfirmDialog
            (null,
                "Final Score: " + score + "\nPlay again?",
                "Game Over! Time's up.", JOptionPane.YES_NO_OPTION
            );
            
        if (playAgain == JOptionPane.YES_OPTION) {
            SwingUtilities.invokeLater(() -> new BoggleGame());
        }
        else {
            System.exit(0);
        }
    }

    // gets user name
    private static void getUserName() {
        while (userName.isEmpty()) {
            userName = JOptionPane.showInputDialog(null, "Please enter your name:");
            if (userName == null) {
                SwingUtilities.invokeLater(() -> startMenu());
                break;
            } else if (userName.trim().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Please enter a valid name.");
                userName = "";
            }
        }
    
        // Start the game if a valid name is provided
        if (!userName.isEmpty()) {
            SwingUtilities.invokeLater(() -> new BoggleGame());
        }
    }    

    private static class playButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            menuFrame.dispose();
            getUserName();
        }
    }

    private static class rulesButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            displayRules(menuFrame);
        }
    }

    private static class exitButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            System.exit(0);
        }
    }

    private static void initializeButtons() {
        playButton = new JButton("Play");
        rulesButton = new JButton("Rules");
        exitButton = new JButton("Exit");
    }

    private static void setButtonColors() {
        playButton.setBackground(Color.GREEN);
        rulesButton.setBackground(Color.RED);
        exitButton.setBackground(Color.YELLOW);
    }

    private static void modifyButtonFonts() {
        playButton.setFont(new Font("Arial", Font.PLAIN, 30));
        rulesButton.setFont(new Font("Arial", Font.PLAIN, 30));
        exitButton.setFont(new Font("Arial", Font.PLAIN, 30));
    }

    private static void addActionListeners() {
        playButton.addActionListener(new playButtonListener());
        rulesButton.addActionListener(new rulesButtonListener());
        exitButton.addActionListener(new exitButtonListener());
    }

    private static void addButtons() {
        menuFrame.add(playButton, gbc());
        menuFrame.add(rulesButton, gbc());
        menuFrame.add(exitButton, gbc());
    }

    private static GridBagConstraints gbc() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);
        return gbc;
    }

    // displays rules
    private static void displayRules(JFrame mainMenuFrame) {
        String rulesText = "RULES:\n\n(from https://www.wordplays.com/boggle)\n\n" +
                "Boggle is a word game that requires 16 letter cubes similar to dice, but with letters. " +
                "The Boggle grid has a dome so when you shake the game the dice are not lost and can easily fall into the grid. " +
                "A sand timer accompanies the game, so players have 3 minutes to find as many words as possible. " +
                "You must have a minimum of two players to play.\n\n" +
    
                "The goal of the game is to have the highest point total. To gain points, players must create words from the randomly assorted letters in the cube grid. " +
                "The longer the word, the higher the point value of the word, according to Boggle rules.\n\n" +
    
                "Boggle is a game involving the creation of as many words as possible from special dice in a specified time limit, and involves two players or more.\n" +
                "- Boggle generally features sixteen cubes or dice that have a single letter on each face, with the exception of ‘Q’, which is combined with the letter ‘U’ as ‘Qu’; as well as a container and lid, designed to hold the dice in place.\n" +
                "- The typical aim of Boggle is to find words within the letter arrangements that sit next to each other on the dice, which are randomized each round by shaking the container.\n" +
                "- Each Boggle player writes the words they find on their own piece of paper, and they are read aloud when time is up, which is usually three minutes on the timer.\n" +
                "- Points are scored in Boggle according to the word length – generally those three to four letters in length score one point, with words eight or more letters in length scoring eleven points, while words found by multiple players are generally considered null and have a point value of zero.\n" +
                "- While each player reads the words they discovered, the other players check their lists for the same words, crossing them off if they match. The points are allocated to each player if they have a unique word.";
    
        JTextArea textArea = new JTextArea(rulesText);
        textArea.setEditable(false);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
    
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(500, 400));
        JOptionPane.showMessageDialog(menuFrame, scrollPane, "Boggle Game Rules", JOptionPane.INFORMATION_MESSAGE);
    }

    // gets the score and sets it to the menu
    private static void setScore() {
        Map<String, Integer> allScores = ScoreManager.readScores();
        if (!allScores.isEmpty()) {
            int highestScore = Collections.max(allScores.values());
            String highestScorer = allScores.entrySet().stream()
                    .filter(entry -> entry.getValue() == highestScore)
                    .findFirst()
                    .map(Map.Entry::getKey)
                    .orElse("");
            highestScorerLabel.setText("High Score: " + highestScorer + ", " + highestScore);
        } else {
            highestScorerLabel.setText("No scores available.");
        }
    }    

    // intialize start menu
    private static void startMenu() {
        initializeButtons();
        setButtonColors();
        modifyButtonFonts();
        setScore();
       
        menuFrame = new JFrame("Boggle Menu");
        menuFrame.setLayout(new GridBagLayout());
        menuFrame.add(highestScorerLabel, gbc());
        
        addActionListeners();
        addButtons();
        
        menuFrame.setSize(600, 400);
        menuFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        menuFrame.setLocationRelativeTo(null);
        menuFrame.setVisible(true);
    }

    // main method to begin the game
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> startMenu());
    }
}
