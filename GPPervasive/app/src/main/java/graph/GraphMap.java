package graph;

import com.estimote.sdk.Region;
import com.estimote.sdk.internal.utils.L;

import java.util.LinkedHashMap;
import java.util.LinkedList;

/**
 * Created by Andrea on 17/04/2016.
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

    //it provides one inizialization at compilation time of one map
    public void inizializateMaps(){
        Node[] nodes = new Node[7];

        //0 collegato con 1,2,3
        //3 collegato 4
        //5 con 1
        //6 con 2
        nodes[0] = new Node();
        nodes[1] = new Node();
        nodes[2] = new Node();
        nodes[3] = new Node();
        nodes[4] = new Node();
        nodes[5] = new Node();
        nodes[6] = new Node();

        LinkedList<Edge> e_n0 = new LinkedList<>();
        e_n0.add(new Edge( nodes[0], nodes[1],0,5));
        e_n0.add(new Edge( nodes[0], nodes[2],0,5));
        e_n0.add(new Edge( nodes[0], nodes[3],0,10));

        LinkedList<Edge> e_n1 = new LinkedList<>();
        e_n1.add(new Edge(nodes[1], nodes[0],0,5));
        e_n1.add(new Edge(nodes[1], nodes[5],0,20));

        LinkedList<Edge> e_n2 = new LinkedList<>();
        e_n2.add(new Edge( nodes[2],nodes[0],0,5));
        e_n2.add(new Edge( nodes[2],nodes[6],0,20));


        LinkedList<Edge> e_n3 = new LinkedList<>();
        e_n3.add(new Edge( nodes[3], nodes[4],0,20));
        e_n3.add(new Edge( nodes[0], nodes[3],0,10));

        LinkedList<Edge> e_n4 = new LinkedList<>();
        e_n4.add(new Edge(nodes[4], nodes[3],0,20));

        LinkedList<Edge> e_n5 = new LinkedList<>();
        e_n5.add(new Edge(nodes[5], nodes[1],0,20));
        LinkedList<Edge> e_n6 = new LinkedList<>();
        e_n6.add(new Edge(nodes[6], nodes[1],0,20));

        nodes[0].setAudio_Edges(e_n0," ",Node.CATEGORY.ROOM);
        nodes[1].setAudio_Edges(e_n1," ",Node.CATEGORY.STAIRS);
        nodes[2].setAudio_Edges(e_n2,"",Node.CATEGORY.STAIRS);
        nodes[3].setAudio_Edges(e_n3,"",Node.CATEGORY.OUTDOOR);
        nodes[4].setAudio_Edges(e_n4,"",Node.CATEGORY.OUTDOOR);
        nodes[5].setAudio_Edges(e_n5,"",Node.CATEGORY.ROOM);
        nodes[6].setAudio_Edges(e_n6,"",Node.CATEGORY.ROOM);

    }

}
