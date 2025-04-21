import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class WordLadder {
    public static void main(String[] args) throws FileNotFoundException {
//        String start_string = "hot", target_string = "dog";
//        ArrayList<String> wordlist = new ArrayList<>();
//        Collections.addAll(wordlist, "hot","dot","dog","lot","log","cog");
//        System.out.println("Word List: " + wordlist);

        String start_string = "world";
        String target_string = "court";
        System.out.println(start_string + " --> " + target_string);
        ArrayList<String> wordlist = processWordListFromFile();

        HashMap<String, ArrayList<String>> neighbors = makeNeighbors(start_string, wordlist);

        // BFS
        List<List<String>> allPaths = bfsWordSearchAllPaths(start_string, target_string, neighbors);
        System.out.println("the number of solutions with the minimal steps: " + allPaths.size());
        for (List<String> path : allPaths) {
            System.out.println("Path: " + path);
        }

    }

    public static int bfsWordSearch(String start_string, String target_string,
                                    HashMap<String, ArrayList<String>> neighbors) {
        HashSet<String> frontier = new HashSet<>();
        frontier.add(start_string);
        ArrayDeque<String> q = new ArrayDeque<>();
        q.addLast(start_string);
        int min_steps = 1;
        ArrayList<String> seq = new ArrayList<>();
        while (!q.isEmpty()) {
            for (int i = 0; i < q.size(); i++) {
                String word = q.removeFirst();
                seq.add(word);
                if (word.equals(target_string)) {
                    System.out.println("Seq: " + seq);
                    return min_steps;
                }
                // process all patterns of current word
                for (int j = 0; j < word.length(); j++) {
                    String pattern = word.substring(0, j) + "*" + word.substring(j + 1, word.length());
                    for (String neiWord : neighbors.get(pattern)) {
                        if (!frontier.contains(neiWord)) {
                            frontier.add(neiWord);
                            q.addLast(neiWord);
                        }
                    }
                }
                System.out.println(frontier);
            }
            min_steps++;
        }
        System.out.println("There is no solution in this case.");
        return 0;
    }

    /**
     *
     * @param start_string
     * @param target_string
     * @param neighbors
     * @return
     */
    public static List<List<String>> bfsWordSearchAllPaths(String start_string, String target_string,
                                                           HashMap<String, ArrayList<String>> neighbors) {
        List<List<String>> results = new ArrayList<>();
        Queue<List<String>> q = new ArrayDeque<>();
        Set<String> visited = new HashSet<>();
        Set<String> localVisited = new HashSet<>();
        boolean found = false;

        int min_steps = -1;
        q.add(new ArrayList<>(List.of(start_string)));

        while (!q.isEmpty() && !found) {
            int levelSize = q.size();
            localVisited.clear();

            for (int i = 0; i < levelSize; i++) {
                List<String> path = q.poll();
                String lastWord = path.get(path.size() - 1);

                if (lastWord.equals(target_string)) {
                    results.add(path);
                    found = true;
                }

                for (int j = 0; j < lastWord.length(); j++) {
                    String pattern = lastWord.substring(0, j) + "*" + lastWord.substring(j + 1);

                    ArrayList<String> neiList = neighbors.getOrDefault(pattern, new ArrayList<>());
                    for (String nei : neiList) {
                        if (!visited.contains(nei)) {
                            List<String> newPath = new ArrayList<>(path);
                            newPath.add(nei);
                            q.add(newPath);
                            localVisited.add(nei);
                        }
                    }
                }
            }
            visited.addAll(localVisited);
            min_steps++;
        }
        System.out.println("the minimum number of the steps: " + min_steps);
        return results;
    }


    /**
     * Create list of words from text file
     * @return
     */
    public static ArrayList<String> processWordListFromFile() throws FileNotFoundException {
        ArrayList<String> wordlist = new ArrayList<>();
        try {
            File myObj = new File("/Users/rellamas/MATH STUFF/25SP/CS146 - DSA/146 PAs/WordLadder/src/word.ladder.txt");
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

    /**
     *
     * @param start_string
     * @param wordlist
     * @return
     */
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
