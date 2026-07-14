import java.util.Scanner;
import java.util.Set;

/**
 * Program to test the functionality of the Lexicon implementation. Provides a
 * simple text-based interface to operate on the lexicon and exercise its
 * capabilities. You should not need to change this class.
 * 
 * @author Sean Barker
 */
public class TestLexicon {

    // the lexicon object to operate on
    private static Lexicon lex = new LexiconTrie();

    // whether we're finished executing commands on the lexicon
    private static boolean done = false;

    /**
     * Runs a command loop allowing the user to enter commands until finished.
     */
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);

        System.out.println("Welcome to the Lexicon Tester! "
            + "Press <return> for list of commands.");
        while (!done) {
            System.out.print("\nEnter command: ");
            String[] tokens = scan.nextLine().split(" ");
            if (!invokeCmd(tokens)) {
                System.out.println("Unrecognized command \"" + tokens[0]
                    + "\".  Hit <return> for list of commands.");
            }
        }

        scan.close();
    }

    /*
     * All code below this point has to do with providing the various commands of
     * the interface. There are a bunch of syntactic features used here that we
     * haven't seen before -- don't get bogged down in the details.
     */

    private static abstract class Command {

        String abbrev, name, usage, help;

        int numArgs;

        public Command(String abbrev, String name, String usage, String help, int numArgs) {
            this.abbrev = abbrev;
            this.name = name;
            this.usage = usage;
            this.help = help;
            this.numArgs = numArgs;
        }

        abstract public void performOp(String[] args);

        @Override
        public String toString() {
            StringBuffer sb = new StringBuffer(abbrev + "  " + name);
            while (sb.length() < 14) { // pad to length 14
                sb.append(" ");
            }
            sb.append(usage);
            while (sb.length() < 32) { // pad to length 40
                sb.append(" ");
            }
            return sb.append(help).toString();
        }
    }

    private static final Command[] COMMANDS = {
            new Command("a", "add", "<word>", "Add word to lexicon", 1) {

                @Override
                public void performOp(String[] commandTokens) {
                    testAdd(commandTokens);
                }
            }, new Command("c", "contains", "<str>", "Search lexicon for word/prefix", 1) {

                @Override
                public void performOp(String[] commandTokens) {
                    testContains(commandTokens);
                }
            }, new Command("r", "remove", "<word>", "Remove word from lexicon", 1) {

                @Override
                public void performOp(String[] commandTokens) {
                    testRemove(commandTokens);
                }
            }, new Command("f", "file", "<filename>", "Add words from filename to lexicon", 1) {

                @Override
                public void performOp(String[] commandTokens) {
                    testReadFile(commandTokens);
                }
            }, new Command("p", "print", "", "Print all words in lexicon ", 0) {

                @Override
                public void performOp(String[] commandTokens) {
                    testIterator(commandTokens);
                }
            }, new Command("s", "suggest", "<target> <dist>", "Find suggestions for target within dist", 2) {

                @Override
                public void performOp(String[] commandTokens) {
                    testSuggestions(commandTokens);
                }
            }, new Command("m", "match", "<regex>", "Find matches for pattern", 1) {

                @Override
                public void performOp(String[] commandTokens) {
                    testRegex(commandTokens);
                }
            }, new Command("q", "quit", "", "Quit the program", 0) {

                @Override
                public void performOp(String[] commandTokens) {
                    done = true;
                    System.out.println("Bye!");
                }
            } };

    /**
     * Looks for a matching command in the list of available options and if found,
     * executes the operation for that command.
     */
    private static boolean invokeCmd(String[] tokens) {
        String userCmd = tokens[0];
        if (userCmd.equals("")) {
            System.out.print("Command listing: ");
            System.out.println("[abbrv] [cmd] [args] [description]");
            for (Command cmd : COMMANDS) {
                System.out.println(cmd);
            }
            return true;
        }

        for (Command cmd : COMMANDS) {
            if (userCmd.startsWith(cmd.abbrev) || userCmd.equals(cmd.name)) {
                if (cmd.numArgs != tokens.length - 1) {
                    System.out.println("The " + cmd.name + " command expects "
                        + cmd.numArgs + " arguments.");
                } else {
                    cmd.performOp(tokens);
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Adds the given word to the lexicon.
     */
    private static void testAdd(String[] commandTokens) {
        String word = commandTokens[1].toLowerCase();
        if (lex.addWord(word)) {
            System.out.println("\"" + word + "\" added to lexicon.");
        } else {
            System.out.println("\"" + word + "\" already was in lexicon.");
            System.out.println("Lexicon now contains " + lex.numWords() + " words.");
        }
    }

    /**
     * Removes the given word from the lexicon.
     */
    private static void testRemove(String[] commandTokens) {
        String word = commandTokens[1].toLowerCase();
        if (lex.removeWord(word)) {
            System.out.println("\"" + word + "\" removed from lexicon.");
        } else {
            System.out.println("\"" + word + "\" wasn't in lexicon.");
        }
        System.out.println("Lexicon now contains " + lex.numWords() + " words.");
    }

    /**
     * Reports whether the given String is a prefix and a word within the lexicon.
     */
    private static void testContains(String[] commandTokens) {
        String str = commandTokens[1].toLowerCase();
        boolean contains = lex.containsPrefix(str);
        System.out.println("Prefix \"" + str + "\" " + (contains ? "IS" : "is NOT")
            + " contained in lexicon.");
        contains = lex.containsWord(str);
        System.out.println("Word \"" + str + "\" " + (contains ? "IS" : "is NOT")
            + " contained in lexicon.");
    }

    /**
     * Adds all words from the named file to the lexicon.
     */
    private static void testReadFile(String[] commandTokens) {
        String filename = commandTokens[1];
        int count = lex.addWordsFromFile(filename);
        if (count == -1) {
            System.out.println("Failed to read file \"" + filename + "\".");
        } else {
            System.out
            .println("Read " + count + " words from file \"" + filename + "\".");
            System.out.println("Lexicon now contains " + lex.numWords() + " words.");
        }
    }

    /**
     * Prints all the words in the lexicon using the lexicon's iterator
     * functionality.
     */
    private static void testIterator(String[] unused) {
        System.out.println(
            "Lexicon contains " + lex.numWords() + " words.  Here they are:");
        System.out.println("--------------------------------------------");
        for (String word : lex) {
            System.out.println(word);
        }
    }

    /**
     * Prints out all the words from the lexicon that are within max distance of
     * the target String. The args parameter is expected to be [target, dist].
     */
    private static void testSuggestions(String[] commandTokens) {
        String target = commandTokens[1].toLowerCase();
        String distStr = commandTokens[2];
        int maxDistance = Integer.parseInt(distStr);

        System.out.println("Words that are within distance " + maxDistance
            + " of \"" + target + "\"");
        System.out.println("--------------------------------------------");
        Set<String> corrections = lex.suggestCorrections(target, maxDistance);
        System.out.println(corrections);
    }

    /**
     * Prints out all the words from the lexicon that match the regular expression
     * pattern.
     */
    private static void testRegex(String[] commandTokens) {
        String pattern = commandTokens[1].toLowerCase();
        System.out.println("Words that match pattern " + pattern);
        System.out.println("-----------------------------------");
        Set<String> matches = lex.matchRegex(pattern);
        System.out.println(matches);
    }

}
