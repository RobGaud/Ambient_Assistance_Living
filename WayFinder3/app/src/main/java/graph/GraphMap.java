package graph;

import com.estimote.sdk.Region;

import java.util.LinkedHashMap;

/**
 * Created by Andrea on 17/04/2016.
 *
 * GraphMap class is used to represent maps in WayFinder.
 * In particular, the map is stored in a hashMap where each node is indexed by the related Region
 * (i.e, the related beacon identifier).
 */
public class GraphMap {
    private LinkedHashMap<Region,Node> nodes;
    private Node currentPosition;

    public GraphMap(LinkedHashMap<Region,Node> n){
        nodes = n;
    }

    public void setCurrentPosition(Node currentPosition){
        this.currentPosition = currentPosition;
    }

    public Node getCurrentPosition(){ return currentPosition;  }

    public Node getNodeFromBeacon(Region currentRegion){
        return this.nodes.get(currentRegion);
    }

}
