package com.company;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class BFS {
    private boolean[] visited;
    private int[] edgeTo;   //to keep track of each node's parent
    private int[] distTo;
    public Map<Integer, List<Integer>> graph;

    public BFS(List<Edge> edges){
       Map<Integer, List<Integer>> graph = constructGraph(edges);
       this.graph = graph;
       Set<Integer> vertices = graph.keySet();
       edgeTo = new int[vertices.size()];
       int i = 0;
       for(Integer k : vertices){
           edgeTo[i++] = k.intValue();
       }

       distTo = new int[vertices.size()];
       int j = 0;
       for (Integer k : vertices){
           distTo[j++] = k.intValue();
       }

       int v = 0;
       visited = new boolean[vertices.size()];
       for(Integer k : vertices){
           visited[v++] = false;
       }
    }



    public List<Integer> getV(Map<Integer, List<Integer>> graph){
        List<Integer> vertices = new ArrayList<>();
        for(Integer v : graph.keySet())
            vertices.add(v);

        return vertices;
    }


    //given a list of edges, construct the graph and return it
    private Map<Integer, List<Integer>> constructGraph(List<Edge> edges){
        Map<Integer, List<Integer>> graph = new HashMap<>();
        for(Edge edge : edges){
            int from  = edge.getVertex();
            int to = edge.getOtherVertex(from);

            if(graph.containsKey(from)){
                graph.get(from).add(to);
            }
            else{
                List<Integer> adjList = new ArrayList<>();
                adjList.add(to);
                graph.put(from, adjList);
            }
        }
        return graph;
    }


    //Breadth First Search to find the shortest path from s to all other vertices in the graph
    public void bfs(int s){
        Queue<Integer> queue = new LinkedList<>();

        List<Integer> vertices = getV(graph);
        for(int v = 0; v < vertices.size(); v++)
            distTo[v] = Integer.MAX_VALUE;

        distTo[s] = 0;
        visited[s] = true;
        queue.offer(s);

        while(!queue.isEmpty()){
            int v = queue.poll();
            for(int neighbor : graph.get(v)){
                if(!visited[neighbor]){
                    edgeTo[neighbor] = v;
                    distTo[neighbor] = distTo[v] + 1;
                    visited[neighbor] = true;
                    queue.offer(neighbor);
                }
            }
        }
    }


    //returns true if there is a path to v from source s, false if no path
    private boolean hasPathTo(int v){
        return visited[v];
    }


    //returns the shortest path from source s to destination v
    private Iterable<Integer> pathToV(int v){
        Deque<Integer> path = new ArrayDeque<>();
        if(!hasPathTo(v))
            return path;

        int x;
        for(x = v; distTo[x] != 0; x = edgeTo[x]){     //backtrack path to get to the source vertex
            path.push(x);
        }
        path.push(x);

        return path;
    }



    //main function
    public static void main(String[] args) throws FileNotFoundException {
        //read from file to get 1st graph
        List<Edge> edges = new ArrayList<>();

        //TODO:Uncomment line below and comment the line after it to compare BFS and dijkstra's
        File file = new File("/Users/dineshkumar/Desktop/Graphs/bfs_dijk.txt");
        //File file  = new File("/Users/dineshkumar/Desktop/Graphs/bfs_2.txt");
        Scanner scanner = new Scanner(file);
        while(scanner.hasNextLine()){
            String line = scanner.nextLine().trim();
            try {
                int n = Integer.parseInt(line);
                for(int i = 0; i < n; i++){
                    String edgeLine = scanner.nextLine();
                    String[] splitLine = edgeLine.split(",");
                    int from = Integer.parseInt(splitLine[0]);
                    int to = Integer.parseInt(splitLine[1]);
                    int edgeWeight = Integer.parseInt(splitLine[2]);

                    edges.add(new Edge(from, to, edgeWeight));
                    edges.add(new Edge(to, from, edgeWeight));
                }
            }
            catch (NumberFormatException ex){
                ex.printStackTrace();
            }
        }

        long startTime = System.nanoTime();

        BFS bfsObject = new BFS(edges);

        int source = 0;
        int dest = 13;

        bfsObject.bfs(source);

        long endTime = System.nanoTime();
        long totalRunTime = endTime - startTime;
        System.out.println("Total time (ms): " + totalRunTime/(1000000.0));

        System.out.println("Shortest path from " + source + " to " + dest + " is: " + bfsObject.pathToV(dest));
    }
}
