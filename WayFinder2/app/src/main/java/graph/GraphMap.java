package graph;

import com.estimote.sdk.Region;
import com.estimote.sdk.internal.utils.L;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.UUID;

/**
 * Created by Andrea on 17/04/2016.
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

    public void setCurrentPosition(Node currentPosition) { this.currentPosition = currentPosition;    }

    public Node getCurrentPosition(){ return currentPosition;  }

    public Node getNodeFromBeacon(Region currentRegion){
        return this.nodes.get(currentRegion);
    }

    /*
    //it provides one inizialization at compilation time of one map
    public void inizializateMaps(){

        Node[] nodes_array = new Node[7];

        //0 collegato con 1,2,3
        //3 collegato 4
        //5 con 1
        //6 con 2
        nodes_array[0] = new Node();  // Hall
        nodes_array[1] = new Node();  // Stairs A
        nodes_array[2] = new Node();  // Stairs B
        nodes_array[3] = new Node();  // Wing B
        nodes_array[4] = new Node();  // Garden_gazebos
        nodes_array[5] = new Node();  // Exit
        nodes_array[6] = new Node();  // Node_Sentinel_B

        LinkedList<Edge> e_n0 = new LinkedList<>();

        e_n0.add(new Edge( nodes_array[0], nodes_array[1],160,5));
        e_n0.add(new Edge( nodes_array[0], nodes_array[2],340,5));
        e_n0.add(new Edge( nodes_array[0], nodes_array[4],250,5));
        e_n0.add(new Edge( nodes_array[0], nodes_array[5],70,5));

        LinkedList<Edge> e_n1 = new LinkedList<>();
        e_n1.add(new Edge(nodes_array[1], nodes_array[0],340,5));
        //e_n1.add(new Edge(nodes_array[1], nodes_array[5],0,20));

        LinkedList<Edge> e_n2 = new LinkedList<>();
        e_n2.add(new Edge( nodes_array[2],nodes_array[0],160,5));
        e_n2.add(new Edge( nodes_array[2],nodes_array[3],340,5));


        LinkedList<Edge> e_n3 = new LinkedList<>();
        e_n3.add(new Edge( nodes_array[3], nodes_array[2],160,5));
        //e_n3.add(new Edge( nodes_array[3], nodes_array[3],0,10));

        LinkedList<Edge> e_n4 = new LinkedList<>();
        //e_n4.add(new Edge(nodes_array[4], nodes_array[0],70,0));

        LinkedList<Edge> e_n5 = new LinkedList<>();
       // e_n5.add(new Edge(nodes_array[5], nodes_array[0],0,20));

        LinkedList<Edge> e_n6 = new LinkedList<>();
        e_n6.add(new Edge(nodes_array[6], nodes_array[1],0,20));

        nodes_array[0].setAudio_Edges(e_n0," ",Node.CATEGORY.ROOM);
        nodes_array[1].setAudio_Edges(e_n1," ",Node.CATEGORY.STAIRS);
        nodes_array[2].setAudio_Edges(e_n2,"",Node.CATEGORY.STAIRS);
        nodes_array[3].setAudio_Edges(e_n3,"",Node.CATEGORY.ROOM);
        nodes_array[4].setAudio_Edges(e_n4,"",Node.CATEGORY.OUTDOOR);
        nodes_array[5].setAudio_Edges(e_n5,"",Node.CATEGORY.OUTDOOR);
        //nodes_array[6].setAudio_Edges(e_n6,"",Node.CATEGORY.ROOM);

        String UUID_String = "B9407F30-F5F8-466E-AFF9-25556B57FE6D";

        nodes.put(new Region("DIAG hall", UUID.fromString(UUID_String), 62887, 48775),
                             nodes_array[0]);
        //nodes_array[0].setSteps(5);
        nodes_array[1].setSteps(14);
        nodes.put(new Region("Stairs A", UUID.fromString(UUID_String), 62887, 44680),
                nodes_array[1]);

        nodes_array[2].setSteps(14);
        nodes.put(new Region("Stairs B", UUID.fromString(UUID_String), 62887, 53723),
                nodes_array[2]);

        nodes.put(new Region("Wing B", UUID.fromString(UUID_String), 62887, 4558),
                nodes_array[3]);

        nodes.put(new Region("Garden Entry", UUID.fromString(UUID_String), 62887, 5858),
                nodes_array[4]);

        nodes.put(new Region("Exit", UUID.fromString(UUID_String), 62887, 4125),
                nodes_array[5]);

        nodes_array[0].setAudio("the Hall");
        nodes_array[1].setAudio("Stairs A");
        nodes_array[2].setAudio("Stairs B");
        nodes_array[3].setAudio("Wing B");
        nodes_array[4].setAudio("the Garden gazebos");
        nodes_array[5].setAudio("Exit");


    }
    */

}
