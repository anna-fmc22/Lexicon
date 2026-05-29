import java.util.Set;

/**
 * @author Rafael Almeida
 * @version 2025/03/13
 */
public class Main {

    /**
     * Delimiter used to separate words in the output.
     */
    static final String OUTPUT_DELIMITER = " ";

    /**
     * Contents command usage description.
     */
    private static final String CONTENTS_USAGE = "contents <file>";

    /**
     * Remove command usage description.
     */
    private static final String REMOVE_USAGE = "remove <file> <word1> <word2> ... <wordN>";

    /**
     * Suggest command usage description.
     */
    private static final String SUGGEST_USAGE = "suggest <file> <dist> <word>";

    /**
     * Match command usage description.
     */
    private static final String MATCH_USAGE = "match <file> <pattern>";

    /**
     * Contains command usage description.
     */
    private static final String CONTAINS_USAGE = "contains <file> <word1> <word2> ... <wordN>";

    /**
     * Invalid usage prefix.
     */
    private static final String INVALID_USAGE_PREFIX = "Invalid usage. Correct format:";

    /**
     * Main method for the command-line interface to the LexiconTrie class.
     *
     * @param args
     *             Command-line arguments.
     */
    public static void main(String[] args) {
        Lexicon lex = new LexiconTrie();

        if (args.length == 0) {
            printCommandList();
            return;
        }

        String command = args[0].toLowerCase();
        // the below values include the command itself
        final int contentsArgCount = 2;
        final int removeArgCount = 3;
        final int suggestArgCount = 4;
        final int matchArgCount = 3;
        final int containsArgCount = 3;
        switch (command) {
            case "contents":
                if (args.length != contentsArgCount) {
                    System.out.printf("%s %s%n", INVALID_USAGE_PREFIX,
                            CONTENTS_USAGE);
                    return;
                }
                handleContents(lex, args[1]);
                break;

            case "remove":
                if (args.length < removeArgCount) {
                    System.out.printf("%s %s%n", INVALID_USAGE_PREFIX,
                            REMOVE_USAGE);
                    return;
                }
                handleRemove(lex, args);
                break;

            case "suggest":
                if (args.length != suggestArgCount) {
                    System.out.printf("%s %s%n", INVALID_USAGE_PREFIX,
                            SUGGEST_USAGE);
                    return;
                }
                handleSuggest(lex, args);
                break;

            case "match":
                if (args.length != matchArgCount) {
                    System.out.printf("%s %s%n", INVALID_USAGE_PREFIX,
                            MATCH_USAGE);
                    return;
                }
                handleMatch(lex, args);
                break;

            case "contains":
                if (args.length < containsArgCount) {
                    System.out.printf("%s %s%n", INVALID_USAGE_PREFIX,
                            CONTAINS_USAGE);
                    return;
                }

                handleContains(lex, args);
                break;

            default:
                System.out.printf("Unknown command: %s%n", command);
                printCommandList();
                break;
        }
    }

    private static void printCommandList() {
        System.out.println("Available commands:");
        System.out.println(String.format(
                "  %s: Print the total number of words in the file and all words separated by space.",
                CONTENTS_USAGE));
        System.out.println(String.format(
                "  %s: Remove specified words from the lexicon and print remaining words.",
                REMOVE_USAGE));
        System.out
                .println(String.format(
                        "  %s: Print all suggestions for <word> at the given distance.",
                        SUGGEST_USAGE));
        System.out.println(String.format(
                "  %s: Print all words in the lexicon that match <pattern>",
                MATCH_USAGE));
        System.out.println(String.format(
                "  %s: Outputs true or false for each word whether they are in the lexicon.",
                CONTAINS_USAGE));
    }

    private static void handleContents(Lexicon lex, String fileName) {
        lex.addWordsFromFile(fileName);

        StringBuilder output = new StringBuilder();
        for (String word : lex) {
            output.append(word).append(OUTPUT_DELIMITER);
        }

        System.out.println(output.toString().trim());
    }

    private static void handleRemove(Lexicon lex, String[] args) {
        String fileName = args[1];
        lex.addWordsFromFile(fileName);

        StringBuilder output = new StringBuilder();
        for (int i = 2; i < args.length; i++) {
            output.append(lex.removeWord(args[i])).append(OUTPUT_DELIMITER);
        }

        System.out.println(output.toString().trim());

        StringBuilder words = new StringBuilder();
        for (String word : lex) {
            words.append(word).append(OUTPUT_DELIMITER);
        }

        System.out.println(words.toString().trim());
    }

    private static void handleSuggest(Lexicon lex, String[] args) {
        String fileName = args[1];
        final int distanceIndex = 2;
        // Assumes input is well-formed.
        int distance = Integer.parseInt(args[distanceIndex]);
        final int wordIndex = 3;
        String word = args[wordIndex];

        lex.addWordsFromFile(fileName);

        StringBuilder words = new StringBuilder();
        Set<String> suggestions = lex.suggestCorrections(word, distance);
        for (String suggestion : suggestions) {
            words.append(suggestion).append(OUTPUT_DELIMITER);
        }

        System.out.println(words.toString().trim());
    }

    private static void handleMatch(Lexicon lex, String[] args) {
        String fileName = args[1];
        String pattern = args[2];

        lex.addWordsFromFile(fileName);

        Set<String> matches = lex.matchRegex(pattern);

        StringBuilder output = new StringBuilder();
        for (String match : matches) {
            output.append(match).append(OUTPUT_DELIMITER);
        }

        System.out.println(output.toString().trim());
    }

    private static void handleContains(Lexicon lex, String[] args) {
        String fileName = args[1];
        lex.addWordsFromFile(fileName);

        StringBuilder output = new StringBuilder();
        for (int i = 2; i < args.length; i++) {
            output.append(lex.containsWord(args[i])).append(OUTPUT_DELIMITER);
        }

        System.out.println(output.toString().trim());
    }
}