package com.company;
import java.io.File;
import java.io.IOException;
import java.util.*;

public class Dijkstra {
    private int[] distTo;
    private Edge[] edgeTo;
    private Map<Integer, List<Edge>> graph;

    public Dijkstra(List<Edge> edges){
        Map<Integer, List<Edge>> graph = constructGraph(edges);
        this.graph = graph;
        Set<Integer> vertices = graph.keySet();

        distTo = new int[vertices.size()];

        for (int k : vertices){
            distTo[k] = Integer.MAX_VALUE;
        }

        edgeTo = new Edge[vertices.size()];     //this only works when vertices are 0,1,2,3,4....
    }


    //constructs an edge-weighted graph from a given list of edges
    private Map<Integer, List<Edge>> constructGraph(List<Edge> edges){
        Map<Integer, List<Edge>> graph = new HashMap<>();
        for(Edge edge : edges){
            int u = edge.getVertex();
            List<Edge> adjList = graph.getOrDefault(u, new ArrayList<>());
            adjList.add(edge);
            graph.put(u, adjList);
        }
        return graph;
    }


    //returns the shortest path from source s to destination v
    private Iterable<Integer> pathToV(int v){
        Deque<Integer> path = new ArrayDeque<>();
        if (edgeTo[v] == null) {
            return path;
        }
        int x;
        for(x = v; distTo[x] != 0; x = edgeTo[x].getOtherVertex(x)){
            path.push(x);
        }
        path.push(x);

        return path;
    }


    //Dijkstra's algorithm to find the shortest path from s to all other vertices in the graph
    private void populateShortestPath(int source) {
        Comparator<Integer> cmp = Comparator.comparingInt(a -> distTo[a]);
        Queue<Integer> minHeap = new PriorityQueue<>(cmp);

        distTo[source] = 0;
        minHeap.offer(source);
        while (!minHeap.isEmpty()) {
            int u = minHeap.poll();
            for (Edge edge : graph.get(u)) {
                int v = edge.getOtherVertex(u);
                // eg. 2 -> 3 weight is 10 and 2 -> 7 weight is 2, 7 -> 3 weight is 5
                // then new distTo[3] is 2 + 5 = 7, and 3's new parent becomes 7
                if (distTo[v] > distTo[u] + edge.weight) {
                    distTo[v] = distTo[u] + edge.weight;
                    edgeTo[v] = edge;
                    // reorder priority
                    minHeap.remove(v);
                    minHeap.add(v);
                }
            }
        }
    }



    //main function
    public static void main(String[] args) throws IOException {
        List<Edge> edges = new ArrayList<>();     //<<from, to, weight>, <0, 1, 2>....

        //TODO:Uncomment the line below and comment the line after it to compare bellman ford and Dijkstra's
        //File file = new File("/Users/dineshkumar/Desktop/Graphs/bellman_dijk_input.txt");
        File file = new File("/Users/dineshkumar/Desktop/Graphs/dijk_bellman.txt");
        //File file = new File("/Users/dineshkumar/Desktop/Graphs/bfs_2.txt");
        //TODO:Uncomment the line below and comment the line above to compare BFS and Dijkstra's
        //File file = new File("/Users/dineshkumar/Desktop/Graphs/bfs_dijk.txt");
            Scanner scanner = new Scanner(file);
            while(scanner.hasNextLine()){
                String line = scanner.nextLine();
                int n = Integer.parseInt(line);
                for (int i = 0; i < n; i++) {
                    String edgeLine = scanner.nextLine();
                    String[] splitLine = edgeLine.split(",");
                    int from = Integer.parseInt(splitLine[0]);
                    int to = Integer.parseInt(splitLine[1]);
                    int edgeWeight = Integer.parseInt(splitLine[2]);
                    //check for negative weights
                    if(edgeWeight < 0){
                        System.out.println("The Graph has negative Edge weights. Dijkstra's algorithm does not work for negative weights.");
                        return;
                    }
                    edges.add(new Edge(from, to, edgeWeight));
                    edges.add(new Edge(to, from, edgeWeight));
                }
            }

        Dijkstra dj = new Dijkstra(edges);
        long startTime = System.nanoTime();
        int source = 0;
        int dest = 13;
        dj.populateShortestPath(source);

        long totalRunTime = System.nanoTime() - startTime;
        System.out.println("Total time (ms): " + totalRunTime/(1000000.0));

        System.out.println("Shortest path from " + source + " to " + dest + " is: " + dj.pathToV(dest));
    }
}

