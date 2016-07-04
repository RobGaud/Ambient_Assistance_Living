package graph;


import java.util.LinkedList;

/**
 *  Created by Andrea on 17/04/2016.
 *
 *  Class Node is one of the basic element for maps representation.
 *  Each node represents a important zone into the building (e.g, the hall, the stairs), and
 *  contains information about the type of the zone, the audio message to be played when the user
 *  reaches the place, and the number of stairs (actually used only if the node represents some stairs).
 *  Moreover, a node also keeps track of the nodes users can reach from it, using a list of Edge elements.
 */
public class Node {
    private LinkedList<Edge> edges;
    private String audio;
    public enum CATEGORY {ROOM,OUTDOOR, STAIRS}
    private CATEGORY category;
    private int steps;

    public Node(LinkedList<Edge> e,String a,CATEGORY c){
        edges = e;
        audio = a;
        category = c;
    }

    public Node(){
        this.edges = new LinkedList<>();
    }

    public void setAudio(String audio) { this.audio = audio; }

    public void setEdges(LinkedList<Edge> edges) { this.edges = edges; }

    public void addEdge(Edge e){ this.edges.add(e); }

    public String getAudio() { return audio; }

    public LinkedList<Edge> getEdges() { return edges; }

    public void setSteps(int steps) { this.steps = steps; }

    public int getSteps() { return steps; }

    public void setCategory(CATEGORY category) { this.category = category; }

    public CATEGORY getCategory() { return category; }
}
