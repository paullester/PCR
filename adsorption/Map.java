import org.apache.hadoop.mapreduce.*;

public class Map implements Mapper<> {

    public void map(Node node, List<Label> labels) {
        for (Edge neighbor in node.getEdges()) {
            for (Label label in labels) {
                float propagated = neighbor.getWeight() * label.getWeight();
                context.write(neighbor.node, propagated);
            }
        } 
    }
}