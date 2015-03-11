

public class Edge {

    private float weight;
    private Node node;

    public Edge(float weight, Node node) {
        this.weight = weight;
        this.node = node;
    }

    public float getWeight() {
        return weight;
    }

    public Node getNode() {
        return node;
    }
}