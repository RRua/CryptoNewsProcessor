package Utils.Representations;
import Utils.*;

import java.io.IOException;

// Java program for insertion in AVL Tree
public class AVLTree {

    public static int rep = 0;
    Node root;

    // A utility function to get the height of the tree
    int height(Node N) {
        if (N == null)
            return 0;
        return N.height;
    }

    // A utility function to get maximum of two integers
    int max(int a, int b) {
        return (a > b) ? a : b;
    }

    // A utility function to right rotate subtree rooted with y
    // See the diagram given above.
    Node rightRotate(Node y) {
        Node x = y.left;
        Node T2 = x.right;

        // Perform rotation
        x.right = y;
        y.left = T2;

        // Update heights
        y.height = max(height(y.left), height(y.right)) + 1;
        x.height = max(height(x.left), height(x.right)) + 1;

        // Return new root
        return x;
    }

    // A utility function to left rotate subtree rooted with x
    // See the diagram given above.
    Node leftRotate(Node x) {
        Node y = x.right;
        Node T2 = y.left;

        // Perform rotation
        y.left = x;
        x.right = T2;

        // Update heights
        x.height = max(height(x.left), height(x.right)) + 1;
        y.height = max(height(y.left), height(y.right)) + 1;

        // Return new root
        return y;
    }

    // Get Balance factor of node N
    int getBalance(Node N) {
        if (N == null)
            return 0;

        return height(N.left) - height(N.right);
    }

    Node insert(Node node, String key, int word_index) {

        if (findNode(node,key)!=null){
            key=key+rep;
            rep++;
        }
		/* 1. Perform the normal BST insertion */
        if (node == null)
            return (new Node(key,word_index));
        if (word_index < node.word_index)
            node.left = insert(node.left, key, word_index);
        else if (word_index > node.word_index)
            node.right = insert(node.right, key,word_index);
        else{
            node.right= insert(node.right,(key+rep),word_index);
        }


		/* 2. Update height of this ancestor node */
        node.height = 1 + max(height(node.left),
                height(node.right));

		/* 3. Get the balance factor of this ancestor
			node to check whether this node became
			unbalanced */
        int balance = getBalance(node);

        // If this node becomes unbalanced, then there
        // are 4 cases Left Left Case
        if (balance > 1 && word_index < node.left.word_index)
            return rightRotate(node);

        // Right Right Case
        if (balance < -1 && word_index > node.right.word_index)
            return leftRotate(node);

        // Left Right Case
        if (balance > 1 && word_index > node.left.word_index) {
            node.left = leftRotate(node.left);
            return rightRotate(node);
        }

        // Right Left Case
        if (balance < -1 && word_index < node.right.word_index) {
            node.right = rightRotate(node.right);
            return leftRotate(node);
        }

		/* return the (unchanged) node pointer */
        return node;
    }

    // A utility function to print preorder traversal
    // of the tree.
    // The function also prints height of every node
    void preOrder(Node node) {
        if (node != null) {
            System.out.print(node.key + " ");
            preOrder(node.left);
            preOrder(node.right);
        }
    }

    void preOrderToGraphViz(Node node , StringBuilder sb){
        if (node != null) {
            if(node.left!=null)
                sb.append( node.key + " " + "--" + " " + node.left.key + "\n");
            if(node.right!=null)
                sb.append( node.key + " " + "--" + " " + node.right.key + "\n");

            preOrderToGraphViz(node.left,sb);
            preOrderToGraphViz(node.right,sb);
        }
    }

    public Node findNode(Node n, String key){
        if ( n!=null){
            if (n.key.equals(key)){
                return n;
            }
            else{
                if (findNode(n.right,key)!=null){
                    return findNode(n.right,key);
                }
                else
                    return findNode(n.left,key);
            }
        }
        return null;
    }

    public static void main(String[] args) {
        AVLTree tree = new AVLTree();
        String s = "O programador e entusiasta de criptoativos John_McAfee revelou que a sua intenção de se candidatar a a presidência de os Estados_Unidos";
        int index = 0;
        for(String st : s.split(" ")){
            tree.root = tree.insert(tree.root,  st, index);
            index++;
        }

        StringBuilder sb = new StringBuilder();
        sb.append("graph mygraph {\n");
        tree.preOrderToGraphViz(tree.root, sb);
        sb.append("}\n");
        tree.preOrder(tree.root);
        try {
            GenericUtils.writeFile(new java.io.File("/Users/ruirua/Documents/PhD_Classes/AIE/work/sentenceX.gv"), sb.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
// This code has been contributed by Mayank Jaiswal
