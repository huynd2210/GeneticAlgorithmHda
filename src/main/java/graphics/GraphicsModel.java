package graphics;

import lombok.Getter;
import lombok.Setter;
import graphics.GraphicsNode;

import java.util.ArrayList;
import java.util.List;

/* Container class for a representation of HPModel
   ! For drawing purposes !
*/

@Getter
@Setter
public class GraphicsModel {
    private List<GraphicsNode> nodes;

    public GraphicsModel() {
        this.nodes = new ArrayList<>();
    }

    public void AddNode(GraphicsNode newNode) {
        nodes.add(newNode);
    }

    public GraphicsNode FindNode(Integer[] position) {
        for (GraphicsNode node : nodes) {
            if (node.getPosition()[0] == position[0] && node.getPosition()[1] == position[1]) {
                return node;
            }
        }
        return null;
    }
}