package com.company;

public class Edge {     //[1, 2, 2.0]
    protected int u;
    protected int v;
    protected int weight;

    public Edge(int u, int v, int weight){
        this.u = u;
        this.v = v;
        this.weight = weight;
    }

    //gets either of the 2 vertex connected to edge
    protected int getVertex(){
        return u;
    }

    //gets the other vertex connected to edge
    protected int getOtherVertex(int vertex){
        if(vertex == u)
            return v;
        else if(vertex == v)
            return u;
        else
            throw new IllegalArgumentException("V does not exist");
    }

    //compares weight of given edge with current edge
    private int compareEdge(Edge e1, Edge e2){
        return Double.compare(e1.weight, e2.weight);
    }

    @Override
    public String toString() {
        return "Edge{" +
                "u=" + u +
                ", v=" + v +
                ", weight=" + weight +
                '}';
    }
}
