import java.util.Iterator;
import java.util.Set;

/**
 * An interface for a lexicon, or word list. The main difference between a
 * lexicon and a conventional dictionary is that a lexicon does not provide any
 * mechanism for storing word definitions; a string is simply part of the
 * lexicon or it isn't. The lexicon supports lookups of both prefix strings and
 * complete words. For simplicity, all strings stored in the lexicon consist of
 * lowercase letters only. You should not change this interface.
 * 
 * @author Sean Barker
 */
public interface Lexicon extends Iterable<String> {

    /**
     * Adds the specified word to the lexicon. Should run in time proportional to
     * the length of the word being added. Returns whether the lexicon was
     * modified by adding the word.
     * 
     * @param word
     *          The lowercase word to add to the lexicon.
     * @return True if the word was added and false if the word was already part
     *         of the lexicon.
     */
    public boolean addWord(String word);

    /**
     * Reads the words contained in the specified file and adds them to the
     * lexicon. The format of the given file is expected to be one word per line
     * of the file. All words should be converted to lowercase before adding.
     * Returns the number of new words added, or -1 if the file could not be read.
     * 
     * @param filename
     *          The name of the file to read.
     * @return The number of new words added, or -1 if the file could not be read.
     */
    public int addWordsFromFile(String filename);

    /**
     * Attempts to remove the specified word from the lexicon. If the word appears
     * in the lexicon, it is removed and true is returned. If the word does not
     * appear in the lexicon, the lexicon is unchanged and false is returned.
     * Should run in time proportional to the length of the word being removed. It
     * is implementation-dependent whether unneeded prefixes as a result of
     * removing the word are also removed from the lexicon.
     * 
     * @param word
     *          The lowercase word to remove from the lexicon.
     * @return Whether the word was removed.
     */
    public boolean removeWord(String word);

    /**
     * Returns the number of words contained in the lexicon. Should run in
     * constant time.
     * 
     * @return The number of words in the lexicon.
     */
    public int numWords();

    /**
     * Checks whether the given word exists in the lexicon. Should run in time
     * proportional to the length of the word being looked up.
     * 
     * @param word
     *          The lowercase word to lookup in the lexicon.
     * @return Whether the given word exists in the lexicon.
     */
    public boolean containsWord(String word);

    /**
     * Checks whether any words in the lexicon begin with the specified prefix. A
     * word is defined to be a prefix of itself, and the empty string is defined
     * to be a prefix of everything. Should run in time proportional to the length
     * of the prefix.
     * 
     * @param prefix
     *          The lowercase prefix to lookup in the lexicon.
     * @return Whether the given prefix exists in the lexicon.
     */
    public boolean containsPrefix(String prefix);

    /**
     * Returns an iterator over all words contained in the lexicon. The iterator
     * should return words in the lexicon in alphabetical order.
     */
    @Override
    public Iterator<String> iterator();

    /**
     * Returns a set of words in the lexicon that are suggested corrections for a
     * given (possibly misspelled) word. Suggestions will include all words in the
     * lexicon that are at most maxDistance distance from the target word, where
     * the distance between two words is defined as the number of character
     * positions in which the words differ. Should run in time proportional to the
     * length of the target word.
     * 
     * @param target
     *          The target word to be corrected.
     * @param maxDistance
     *          The maximum word distance of suggested corrections.
     * @return A set of all suggested corrections within maxDistance of the target
     *         word.
     */
    public Set<String> suggestCorrections(String target, int maxDistance);

    /**
     * Returns a set of all words in the lexicon that match the given regular
     * expression pattern. The regular expression pattern may contain only letters
     * and wildcard characters '*', '?', and '_'.
     * 
     * @param pattern
     *          The regular expression pattern to match.
     * @return A set of all words in the lexicon matching the pattern.
     */
    public Set<String> matchRegex(String pattern);

}
