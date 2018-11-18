package Utils.Representations;

class Node {
    public String key = "";
    public int  height, word_index;
    public Node left, right;

    Node(String key, int word_index ) {
        this.key = key;
        this.word_index=word_index;
        height = 1;
    }
}