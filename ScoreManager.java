import java.io.*;
import java.util.*;

public class ScoreManager {
    private static final String SCORE_FILE = "scores.txt";

    // create and updates the score in the file
    public static void updateScore(String userName, int newScore) {
        Map<String, Integer> scores = readScores();
        scores.put(userName, newScore);
        writeScores(scores);
    }

    // reads all the scores from the file
    public static Map<String, Integer> readScores() {
        Map<String, Integer> scores = new HashMap<>();
        File file = new File(SCORE_FILE);

        try (BufferedReader reader = new BufferedReader(new FileReader(SCORE_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 2) {
                    scores.put(parts[0], Integer.parseInt(parts[1]));
                }
            }
        } catch (FileNotFoundException e) {
            createNewScoreFile(file);
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
        }
        return scores;
    }

    // creates the scores.txt file
    private static void createNewScoreFile(File file) {
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // write user score and name to the file
    public static void writeScores(Map<String, Integer> scores) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(SCORE_FILE))) {
            for (Map.Entry<String, Integer> entry : scores.entrySet()) {
                writer.println(entry.getKey() + "," + entry.getValue());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
