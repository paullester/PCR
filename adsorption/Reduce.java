import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.LinkedList;
import org.apache.hadoop.mapreduce.*;

public class Reduce implements Reducer<> {

    public void reduce(Node node, List<Label> labels) {
        Map<String, Label> preOut = new HashMap<String, Label>();
        List<Label> out = new LinkedList<Label>();
        //float totalWeight = 0.0;
        for (Label label in labels) {
            totalWeight += label.getWeight();
            if (out.containsKey(label.getType()) {
                out.put(label.type, label)
            } else {
                float oldWeight = out.get(label.getType()).getWeight();
                float newWeight = oldWeight + label.getWeight();
            }
        }
        for (Label label in preOut.entrySet()) {
            //can't math, should be some normalization of weights...
            out.add(label);
        }
        context.write(node, out);
    }
}