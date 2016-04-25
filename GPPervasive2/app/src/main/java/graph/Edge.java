package graph;

/**
 * Created by Andrea on 17/04/2016.
 */
public class Edge {
    private Node node1,node2;
    private float direction;
    private int distance;
    public Edge(Node n1, Node n2,float dir,int dist){
        node1 = n1;
        node2 = n2;
        direction = dir;
        distance = dist;
    }

    public Edge(){};

    public Node getNode1(){return node1;}
    public Node getNode2(){return node2;}
    public int getDistance(){return  distance;}
    public float getDirection(){return direction;}

    public void setNode1(Node n){node1=n;}
    public void setNode2(Node n){node2 = n;}
    public void setDirection(float d){direction=d;}
    public void setDistance(int d){distance=d;}
}
