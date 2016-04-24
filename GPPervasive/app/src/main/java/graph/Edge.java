package graph;

/**
 * Created by Andrea on 17/04/2016.
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

    public Edge(){};

    public Node getNodeFrom(){return nodeFrom;}
    public Node getNodeTo(){return nodeTo;}
    public int getDistance(){return  distance;}
    public float getDirection(){return direction;}

    public void setNodeFrom(Node n){
        nodeFrom =n;}
    public void setNodeTo(Node n){
        nodeTo = n;}
    public void setDirection(float d){direction=d;}
    public void setDistance(int d){distance=d;}
}
