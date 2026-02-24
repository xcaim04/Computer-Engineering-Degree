package implementations.BinaryTree;

public class BinaryTree {
    private Node root;

    public BinaryTree() {
        this.root = null;
    }

    public void insertar(int value) {
        root = insertarRec(root, value);
    }

    private Node insertarRec(Node current, int value) {
        if (current == null) {
            return new Node(value);
        }

        if (value < current.getValue()) {
            current.left = insertarRec(current.getLeft(), value);
        } else if (value > current.getValue()) {
            current.right = insertarRec(current.getRight(), value);
        }

        return current;
    }

    public boolean buscar(int value) {
        return buscarRec(root, value);
    }

    private boolean buscarRec(Node current, int value) {
        if (current == null) {
            return false;
        }

        if (value == current.getValue()) {
            return true;
        }

        if (value < current.getValue()) {
            return buscarRec(current.getLeft(), value);
        } else {
            return buscarRec(current.getRight(), value);
        }
    }

    public void inOrder() {
        inOrderRec(root);
        System.out.println();
    }

    private void inOrderRec(Node current) {
        if (current != null) {
            inOrderRec(current.getLeft());
            System.out.print(current.getValue() + " ");
            inOrderRec(current.getRight());
        }
    }

    public void preOrder() {
        preOrderRec(root);
        System.out.println();
    }

    private void preOrderRec(Node current) {
        if (current != null) {
            System.out.print(current.getValue() + " ");
            preOrderRec(current.getLeft());
            preOrderRec(current.getRight());
        }
    }

    public void postOrder() {
        postOrderRec(root);
        System.out.println();
    }

    private void postOrderRec(Node current) {
        if (current != null) {
            postOrderRec(current.getLeft());
            postOrderRec(current.getRight());
            System.out.print(current.getValue() + " ");
        }
    }

    public boolean estaVacio() {
        return root == null;
    }
}