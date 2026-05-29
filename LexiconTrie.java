import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.*;

/**
 * An implementation of the Lexicon interface.
 * 
 * @author Anna Feng
 * @version 2026/03/27
 */
public final class LexiconTrie implements Lexicon {

    /**
     * The LexiconNode at the root of the three.
     */
    private LexiconNode root;

    /**
     * The number of words in the LexiconTrie.
     */
    private int numWords;
    
    
    /**
     * Constructs a new lexicon with no words.
     */
    public LexiconTrie() {
        root = new LexiconNode(' ', false);
        numWords = 0;
    }

    /**
     * Constructs a new lexicon starting at a letter node.
     * The variable numWords is set to -1 to indicate no complete words
     * @param root a LexiconNode at the root of the new trie
     */
    public LexiconTrie(LexiconNode root) {
        this.root = root;
        this.numWords = -1;
    }

    
    /**
     * Adds a word to the lexicon.
     *
     * @param word the word to add to the lexicon
     * @return true if the word was added, false if already present
     */
    @Override
    public boolean addWord(String word) {
        LexiconNode currentNode = root;
        char[] characters = word.toCharArray();
        for (int i = 0; i < characters.length; i++) {
            char c = characters[i];
            if (currentNode.hasChildren() && currentNode.getChild(c) != null) {
                currentNode = currentNode.getChild(c);
            } else {
                LexiconNode newChild = new LexiconNode(c, false);
                currentNode.addChild(newChild);
                currentNode = newChild;
            }
        }
        if (currentNode.isWord()) {
            return false;
        } else {
            numWords++;
            currentNode.setWord(true);
            return true;
        }
    }


    /**
     * Adds words from a file to the lexicon.
     *
     * @param filename the file to read words from
     * @return the number of words added, or -1 if file not found
     */
    @Override
    public int addWordsFromFile(String filename) {
        int wordsAdded = 0;
        File file = new File(filename);
        try (Scanner reader = new Scanner(new FileInputStream(file))) {
            while (reader.hasNextLine()) {
                String word = reader.nextLine().toLowerCase();
                if (!containsWord(word)) {
                    this.addWord(word);
                    wordsAdded++;
                }
            }
        } catch (FileNotFoundException e) {
            wordsAdded = -1;
        }
        return wordsAdded;
    }


    /**
     * Removes a word from the lexicon.
     *
     * @param word the word to remove
     * @return true if the word was removed, false if not in lexicon
     */
    @Override
    public boolean removeWord(String word) {
        if (containsWord(word)) {
            char[] characters = word.toCharArray();
            LexiconNode currentNode = root;
            for (char c : characters) {
                currentNode = currentNode.getChild(c);
            }
            currentNode.setWord(false);
            numWords--;
            return true;
        }
        return false;
     
    }


    /**
     * Returns the number of words in the lexicon.
     *
     * @return the number of words in the lexicon
     */
    @Override
    public int numWords() {
        return numWords;
    }


    /**
     * Finds nodes of a word in the trie.
     * @param word the word to find
     * @return the node of the last character, null if not found
     */
    public LexiconNode findLastNode(String word) {
        char[] characters = word.toCharArray();
        LexiconNode currentNode = root;
        for (int i = 0; i < characters.length; i++) {
            char c = characters[i];
            if (currentNode.hasChildren() && currentNode.getChild(c) != null) {
                currentNode = currentNode.getChild(c);
            } else {
                return null;
            }
        }
        return currentNode;
    }


    /**
     * Checks if a word is in the lexicon.
     *
     * @param word the word to check
     * @return true if the word is in the lexicon, false otherwise
     */
    @Override
    public boolean containsWord(String word) {
        LexiconNode lastNode = findLastNode(word);
        if (lastNode != null) {
            return lastNode.isWord();
        }
        return false;
    }


    /**
     * Checks if a prefix is in the lexicon.
     *
     * @param prefix the prefix to check
     * @return true if the prefix is in the lexicon, false otherwise
     */
    @Override
    public boolean containsPrefix(String prefix) {
        LexiconNode lastNode = findLastNode(prefix);
        return lastNode != null;
    }


    /**
     * Recursively builds and returns a list of all the words in the lexicon alphabetically.
     *
     * @param wordList a list storing all words in the trie
     * @param current a pointer pointing to a node
     * @param prefix a prefix for all node from root to current node
     */
    public void buildWordList(ArrayList<String> wordList, LexiconNode current, String prefix) {
        if (current.hasChildren()) {
            for (LexiconNode node : current) {
                prefix += node.letter();
                if (node.isWord()) {
                    wordList.add(prefix);
                }
                buildWordList(wordList, node, prefix);
                prefix = prefix.substring(0, prefix.length() - 1);
            }
        }
    }


    /**
     * Returns an iterator over the words in the lexicon.
     *
     * @return an iterator over the words in the lexicon
     */
    @Override
    public Iterator<String> iterator() {
        ArrayList<String> wordList = new ArrayList<>();
        buildWordList(wordList, root, "");
        Iterator<String> iterator = new Iterator<>() {
            private int idx = 0;
            @Override
            public boolean hasNext() {
                return idx < wordList.size();
            }

            @Override
            public String next() {
                String next = wordList.get(idx);
                idx++;
                return next;
            }
        };
        return iterator;

        }


    /**
     * Suggests corrections for a word based on edit distance.
     *
     * @param target      the target word to suggest corrections for
     * @param maxDistance the maximum edit distance allowed
     * @return a set of suggested corrections
     */
    @Override
    public Set<String> suggestCorrections(String target, int maxDistance) {
       Set<String> suggestions = new HashSet<>();
       String prefix = "";
       suggestWord(target, maxDistance, suggestions, prefix);
       return suggestions;
    }

    /**
     * Suggests corrections for a word based on edit distance.
     *
     * @param target      the target word to suggest corrections for
     * @param maxDistance the maximum edit distance allowed
     * @param suggestions the set that suggestions are stored in
     * @param prefix the prefix made up of all nodes from root too current node
     */
    public void suggestWord(String target, int maxDistance, Set<String> suggestions, String prefix) {
        if (maxDistance >= 0 && maxDistance <= target.length()) {
            if (maxDistance == 0) {
                if (containsWord(target)) {
                    suggestions.add(prefix + target);
                }
            } else {
                if (root.hasChildren()) {
                    for (LexiconNode node : root) {
                        LexiconTrie subTrie = new LexiconTrie(node);
                        if (node.letter() != target.charAt(0)) {
                            subTrie.suggestWord(target.substring(1), maxDistance - 1, suggestions, prefix + node.letter());
                        } else {
                            subTrie.suggestWord(target.substring(1), maxDistance, suggestions, prefix + node.letter());
                        }
                    }
                }
                suggestWord(target, maxDistance - 1, suggestions, prefix);
            }
        }
    }
   

    /**
     * Matches words against a regular expression pattern.
     *
     * @param pattern the regex pattern to match
     * @return a set of words that match the pattern
     */
    @Override
    public Set<String> matchRegex(String pattern) {
        Set<String> matchWordList = new HashSet<>();
        String prefix = "";
        findRegex(pattern, matchWordList, prefix);
        return matchWordList;
    }

    /**
     * Suggests corrections for a word based on edit distance.
     *
     * @param pattern     the pattern that needs to be matched
     * @param matchWordList a list to store all match words
     * @param prefix  the prefix of all nodes from root to current node
     */
    public void findRegex(String pattern, Set<String> matchWordList, String prefix) {
        if (pattern.isEmpty()) {
            if (root.isWord()) {
                matchWordList.add(prefix);
            }
        } else {
            char target = pattern.charAt(0);
            switch (target) {
                case '_' -> {
                    for (LexiconNode node : root) {
                        LexiconTrie subTrie = new LexiconTrie(node);
                        subTrie.findRegex(pattern.substring(1), matchWordList, prefix + node.letter());
                    }
                }
                case '?' -> {
                    findRegex(pattern.substring(1), matchWordList, prefix);
                    for (LexiconNode node : root) {
                        LexiconTrie subTrie = new LexiconTrie(node);
                        subTrie.findRegex(pattern.substring(1), matchWordList, prefix + node.letter());
                    }
                }
                case '*' -> {
                    this.findRegex(pattern.substring(1), matchWordList, prefix);
                    for (LexiconNode node : root) {
                        LexiconTrie subTrie = new LexiconTrie(node);
                        subTrie.findRegex(pattern.substring(1), matchWordList, prefix + node.letter());
                        subTrie.findRegex(pattern, matchWordList, prefix + node.letter());
                    }
                }
                default -> {
                    for (LexiconNode node : root) {
                        if (node.letter() == target) {
                            LexiconTrie subTrie = new LexiconTrie(node);
                            subTrie.findRegex(pattern.substring(1), matchWordList, prefix + node.letter());
                        }
                    }
                }
            }
        }
    }
}
