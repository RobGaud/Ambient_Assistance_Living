package graph;

/**
 * Created by Andrea on 17/04/2016.
 *
 * Class Edge is used to link Nodes together. In particular, an Edge between two nodes represents
 * the fact that an user can reach one node from the other one by walking straight ahead.
 * An edge keeps track of the distance between the nodes, and the direction (in degrees).
 */
public class Edge {
    private Node nodeFrom, nodeTo;
    private float direction;
    private int distance;

    public Edge(Node n1, Node n2,float dir,int dist){
        nodeFrom = n1;
        nodeTo = n2;
        direction = dir;
        distance = dist;
    }

    public Edge(){}

    public Node  getNodeFrom() { return nodeFrom;  }

    public Node  getNodeTo()   { return nodeTo;    }

    public int   getDistance() { return distance;  }

    public float getDirection(){ return direction; }


    public void setNodeFrom(Node n){ nodeFrom = n; }

    public void setNodeTo  (Node n){ nodeTo   = n; }

    public void setDistance(int d) { distance=d;   }

    public void setDirection(float d){ direction=d; }
}
