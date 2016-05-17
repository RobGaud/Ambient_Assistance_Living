package graph;


import java.util.LinkedList;

/**
 * Created by Andrea on 17/04/2016.
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

    public Node(){}

    public void setAudio(String audio) {     this.audio = audio; }
    public void setEdges(LinkedList<Edge> edges) {this.edges = edges; }
    public void setAudio_Edges(LinkedList<Edge> e, String a, CATEGORY c){
        setAudio(a);
        setEdges(e);
        setCategory(c);
    }
    public String getAudio() {  return audio; }

    public LinkedList<Edge> getEdges() { return edges; }

    public void setSteps(int steps) {this.steps = steps; }

    public int getSteps() { return steps; }

    public void setCategory(CATEGORY category) {this.category = category;  }
    public CATEGORY getCategory() {   return category;  }
}
