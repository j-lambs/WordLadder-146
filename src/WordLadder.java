import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Array;
import java.util.*;

public class WordLadder {
    public static void main(String[] args) throws FileNotFoundException {
//        String start = "vines";
//        String end = "minds";

        String start_string = "hit", target_string = "cog";
        ArrayList<String> wordlist = new ArrayList<>();
        Collections.addAll(wordlist, "hot","dot","dog","lot","log","cog");
        System.out.println(wordlist);

        HashMap<String, ArrayList<String>> neighbors = makeNeighbors(start_string, wordlist);

        // BFS
        System.out.println(bfsWordSearch(start_string, target_string, neighbors));
    }

    public static int bfsWordSearch(String start_string, String target_string,
                                    HashMap<String, ArrayList<String>> neighbors) {
        HashSet<String> frontier = new HashSet<>();
        frontier.add(start_string);
        ArrayDeque<String> q = new ArrayDeque<>();
        q.addLast(start_string);
        int min_steps = 1;
        while (true) {
            for (int i = 0; i < q.size(); i++) {
                String word = q.removeFirst();
                if (word.equals(target_string)) {
                    return min_steps;
                }
                for (int j = 0; j < word.length(); j++) {
                    String pattern = word.substring(0, j) + "*" + word.substring(j + 1, word.length());
                    for (String neiWord : neighbors.get(pattern)) {
                        if (!frontier.contains(neiWord)) {
                            frontier.add(neiWord);
                            q.addLast(neiWord);
                        }
                    }
                }
            }
            min_steps++;
        }
    }

    /**
     * Create list of words from text file
     * @return
     */
    public static ArrayList<String> processWordListFromFile() {
        ArrayList<String> wordlist = new ArrayList<>();
        try {
            File myObj = new File("word.ladder.txt");
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                String word = myReader.nextLine();
                wordlist.add(word);
            }
            myReader.close();
        }
        catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        return wordlist;
    }

    public static HashMap<String, ArrayList<String>> makeNeighbors(String start_string,
                                                                   ArrayList<String> wordlist) {
        // make pattern neighbor hashmap
        HashMap<String, ArrayList<String>> neighbors = new HashMap<>();
        wordlist.add(start_string);
        for (String word : wordlist) {
            for (int j = 0; j < word.length(); j++) {
                String pattern = word.substring(0, j) + "*" + word.substring(j + 1, word.length());
                neighbors.computeIfAbsent(pattern, k -> new ArrayList<>()).add(word);
            }
        }
        return neighbors;
    }
}
