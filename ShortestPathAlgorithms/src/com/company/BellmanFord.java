package com.company;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class BellmanFord {
    private int[] distTo;
    private Edge[] edgeTo;
    private boolean[] isOnQueue;
    private Queue<Integer> queue;
    private Map<Integer, List<Edge>> graph;

    public BellmanFord(List<Edge> edges) {
        Map<Integer, List<Edge>> graph = constructGraph(edges);
        this.graph = graph;

        Set<Integer> vertices = graph.keySet();
        isOnQueue = new boolean[vertices.size()];
        distTo = new int[vertices.size()];

        for (int k : vertices) {
            distTo[k] = Integer.MAX_VALUE;
        }

        edgeTo = new Edge[vertices.size()];
    }


    //constructs an edge-weighted graph from a given list of edges
    private Map<Integer, List<Edge>> constructGraph(List<Edge> edges) {
        Map<Integer, List<Edge>> graph = new HashMap<>();
        for (Edge edge : edges) {
            int u = edge.getVertex();
            List<Edge> adjList = graph.getOrDefault(u, new ArrayList<>());
            adjList.add(edge);
            graph.put(u, adjList);
        }
        return graph;
    }

    //bellman-ford algorithm to find the shortest path from source vertex s to all other vertices in the graph
    private void findShortestPath(int s) {
        distTo[s] = 0;
        queue = new LinkedList<>();
        queue.offer(s);
        isOnQueue[s] = true;

        while (!queue.isEmpty()) {
            int u = queue.poll();
            isOnQueue[u] = false;
            for (Edge e : graph.get(u)) {
                int v = e.getOtherVertex(u);
                //relax edge by updating parent
                if (distTo[v] > distTo[u] + e.weight) {
                    distTo[v] = distTo[u] + e.weight;
                    edgeTo[v] = e;

                    if (!isOnQueue[v]) {
                        queue.offer(v);
                        isOnQueue[v] = true;
                    }
                }
            }
        }
    }


    //returns the shortest path from source s to destination v
    private Iterable<Integer> pathToV(int v) {
        Deque<Integer> path = new ArrayDeque<>();
        if (edgeTo[v] == null)
            return path;

        int x;
        for (x = v; distTo[x] != 0; x = edgeTo[x].getOtherVertex(x)) {
            path.push(x);
        }
        path.push(x);

        return path;
    }


    //main function
    public static void main(String[] args) throws FileNotFoundException {
        List<Edge> edges = new ArrayList<>();
        //File file = new File("/Users/dineshkumar/Desktop/Graphs/bellman_dijk_input.txt");
        File file = new File("/Users/dineshkumar/Desktop/Graphs/dijk_bellman.txt");
        Scanner scanner = new Scanner(file);

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            int n = Integer.parseInt(line);
            for (int i = 0; i < n; i++) {
                String edgeLine = scanner.nextLine();
                String[] splitLine = edgeLine.split(",");
                int from = Integer.parseInt(splitLine[0]);
                int to = Integer.parseInt(splitLine[1]);
                int edgeWeight = Integer.parseInt(splitLine[2]);

                edges.add(new Edge(from, to, edgeWeight));
                //uncomment when comparing positive weighted graph with dijkstra
                edges.add((new Edge(to, from, edgeWeight)));
            }
        }

        long startTime = System.nanoTime();
        BellmanFord bellmanFord = new BellmanFord(edges);

        int source = 0;
        int dest = 13;
        bellmanFord.findShortestPath(source);

        long totalRunTime = System.nanoTime() - startTime;
        System.out.println("Total time (ms): " + totalRunTime / (100000.0));

        System.out.println("Shortest path from " + source + " to " + dest + " is: " + bellmanFord.pathToV(dest));
    }
}