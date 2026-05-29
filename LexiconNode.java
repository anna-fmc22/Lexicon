import java.util.Iterator;

/**
 * A node in the Lexicon trie data structure.
 * Each node represents a letter and keeps track of its children.
 * 
 * @author Anna Feng
 * @version 2026/03/25
 */

public final class LexiconNode implements Iterable<LexiconNode> {

    /**
     * The character represented by this node.
     */
    private char letter;

    /**
     * Whether this node marks the end of a word.
     */
    private boolean isWord;

    /**
     * The children of this node, ordered alphabetically.
     */
    private LexiconNode child;

    /**
     * The sibling of this node, ordered alphabetically.
     */
    private LexiconNode sibling;

    /**
     * The parent of this node.
     */
    private LexiconNode parent;

    /**
     * Constructs a new LexiconNode.
     *
     * @param c    the character represented by this node
     * @param word whether this node marks the end of a word
     */
    public LexiconNode(char c, boolean word) {
        this.letter = c;
        this.isWord = word;
        this.child = null;
        this.sibling = null;
    }

    /**
     * Determines if this node marks the end of a word.
     *
     * @return true if this node marks the end of a word, false otherwise
     */
    public boolean isWord() {
        return isWord;
    }

    /**
     * Returns the character represented by this node.
     *
     * @return the character represented by this node
     */
    public char letter() {
        return letter;
    }


    /**
     * Returns an iterator over the children of this node.
     *
     * @return an iterator over the children of this node
     */
    @Override
    public Iterator<LexiconNode> iterator() {
        Iterator<LexiconNode> iterator = new Iterator<>() {
            private LexiconNode pointer = child;
            @Override
            public boolean hasNext() {
                return pointer != null;
            }

            @Override
            public LexiconNode next() {
                if (this.hasNext()) {
                    LexiconNode currentChild = pointer;
                    pointer = pointer.sibling;
                    return currentChild;
                }
                return null;
            }
        };
        return iterator;
    }

    /**
     * Determines if this node has any children.
     *
     * @return true if this node has children, false otherwise
     */
    public boolean hasChildren() {
        return this.child != null;
    }

    /**
     * Determines if this node has a linked sibling.
     *
     * @return true if this node has a sibling linked to it, false otherwise
     */
    public boolean hasSibling() {
        return this.sibling != null;
    }

    /**
     * Adds a child to this node, maintaining alphabetical order.
     *
     * @param node the child to add
     */
    public void addChild(LexiconNode node) {
        LexiconNode current = child;
        LexiconNode prev = null;
        if (current != null && current.letter > node.letter) {
            node.setSibling(current);
            this.child = node;
        } else if (current != null && current.letter < node.letter) {
            while (current.hasSibling() && current.letter < node.letter) {
                prev = current;
                current = current.sibling;
            }
            if (!current.hasSibling() && current.letter < node.letter) {
                current.setSibling(node);
            } else if (!current.hasSibling() && current.letter > node.letter) {
                    prev.setSibling(node);
                    node.setSibling(current);
            }
            if (current.hasSibling() && current.letter > node.letter) {
                prev.setSibling(node);
                node.setSibling(current);
            }
        } else {
            child = node;
        }
    }

    /**
     * Returns the child node corresponding to the given character.
     *
     * @return the first child node, in alphabetical order
     */
    public LexiconNode getChild() {
        return this.child;
    }

    /**
     * Returns the child node corresponding to the given character.
     *
     * @param c the character to look for
     * @return the child node, or null if no such child exists
     */
    public LexiconNode getChild(char c) {
        LexiconNode target = this.child;
        while (target.letter != c && target.hasSibling()) {
            target = target.sibling;
        }
        if (target.letter == c) {
            return target;
        }
        return null;
    }


    /**
     * Removes the child node corresponding to the given character.
     *
     * @param c the character of the child to remove
     */
    public void removeChild(char c) {
        if (child.letter == c) {
            child = child.sibling;
        } else {
            LexiconNode target = this.child;
            while (target.hasSibling() && target.sibling.letter != c) {
                target = target.sibling;
            }
            if (target.sibling.letter == c) {
                target.setSibling(target.sibling.sibling);
            }
        }
    }


    /**
     * Returns the sibling node of the current node.
     *
     * @return the sibling of the node
     */
    public LexiconNode getSibling() {
        return this.sibling;
    }


    /**
     * Change the isWord variable to indicate a word or delete a word.
     *
     * @param newIsWord the new isWord state of the node
     */
    public void setWord(boolean newIsWord) {
        this.isWord = newIsWord;
    }


    /**
     * Add a new sibling for the node.
     *
     * @param newSibling the new sibling of the node
     */
    public void setSibling(LexiconNode newSibling) {
        this.sibling = newSibling;
    }
}
