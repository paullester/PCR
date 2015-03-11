import java.util.List;
import java.util.LinkedList;

public class Node {

    private String name;
    private String type;
    private List<Edge> edges;

    public Node(String name, String type) {
        this.name = name;
        this.type = type;
        this.edges = new LinkedList<Edge>();
    }

    public void addEdges(List<Edge> edges) {
        this.edges.add(edges);
    }

    // add s to make method names match?
    public void addEdge(Edge edge) {
        edges.add(edge);
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public List<Edge> getEdges() {
        return edges;
    }
}