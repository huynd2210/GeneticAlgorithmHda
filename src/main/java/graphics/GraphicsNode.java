package graphics;

import lombok.Getter;
import lombok.Setter;
import model.AminoAcid;
import util.Vector2D;

import java.util.ArrayList;
import java.util.List;

/* Container class to hold aminoAcids with the same position
   ! For drawing purposes !
*/

@Getter
@Setter
public class GraphicsNode {
    // i, j position of GraphicsNode
    private Integer[] position;
    // duh
    private List<AminoAcid> aminoAcids;
    private List<Vector2D> connections;

    public GraphicsNode() {
        this.position = new Integer[]{0, 0};
        this.aminoAcids = new ArrayList<>();
        this.connections = new ArrayList<>();
    }
}