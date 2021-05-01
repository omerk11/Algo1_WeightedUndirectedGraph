package test;

import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.concurrent.atomic.AtomicInteger;



//This class will represent a graph with vertices names 0 to numOfVertices - 1, and edges between the vertices.
public class WeightedUndirectedGraph {
    //Members
    LinkedList<Edge>[] _adjacencylist;
    int _numOfVertices;

    //----Methods----//
    //Ctor
    public WeightedUndirectedGraph(int _numOfVertices) {
        this._numOfVertices = _numOfVertices;
        //init the array and its lists
        _adjacencylist = new LinkedList[_numOfVertices];
        for (int i = 0; i < _numOfVertices; ++i) {
            _adjacencylist[i] = new LinkedList<>();
        }
    }

    //getter
    public int get_numOfVertices() {
        return _numOfVertices;
    }

    //finds edge if there is one
    public Edge findEdge(int v1, int v2) {
        for (Edge edge : _adjacencylist[v1]) {
            if (edge.getOther(v1) == v2) {
                return edge;
            }
        }
        return null;
    }

    //add a edge by parameters
    public void addEdge(int v1, int v2, float w) {
        //create the new edge and add it to the graph
        Edge e = new Edge(v1, v2, w);
        addEdge(e);
    }

    //add edge by edge
    public void addEdge(Edge edge) {
        int v1 = edge._v1;
        int v2 = edge._v2;
        //validity check of vertices
        if (v1 >= _numOfVertices || v2 >= _numOfVertices || v1 < 0 || v2 < 0) {
            System.out.println("Edge is illegal");
            return;
        }
        //validity check if edge exists
        if (findEdge(v1, v2) != null) {
            System.out.println("Edge already exists");
            return;
        }
        _adjacencylist[v1].add(edge);
        _adjacencylist[v2].add(edge);
    }

    //removes a edge
    public void removeEdge(Edge edge) {
        int v1 = edge._v1;
        int v2 = edge._v2;
        _adjacencylist[v1].remove(edge);
        _adjacencylist[v2].remove(edge);
    }

    //print a graph
    public void printGraph() {
        System.out.println("This Graph has " + _numOfVertices + " vertices");
        for (int i = 0; i < _numOfVertices; ++i) {
            LinkedList<Edge> list = _adjacencylist[i];
            for (Edge edge : list) {
                if (edge.get_v1() == i) {
                    System.out.println("vertex " + edge.get_v1() + " is connected with " + edge.get_v2() + " weighed: " + edge.get_w());
                }
            }
        }
        System.out.println();
    }

    //-----------------------------------------------Edges Nested Class-----------------------------------------------//
    private static class Edge {
        private final int _v1;
        private final int _v2;
        private final float _w;

        public Edge(int _v1, int _v2, float _w) {
            this._v1 = _v1;
            this._v2 = _v2;
            this._w = _w;
        }

        public int get_v1() {
            return _v1;
        }

        public int get_v2() {
            return _v2;
        }

        public float get_w() {
            return _w;
        }

        public int getOther(int me) {
            if (me != _v1 && me != _v2) {
                System.out.println("getOther failed");
                return -1;
            }
            return _v1 == me ? _v2 : _v1;
        }
    }

    //-----------------------------------------------Q1: Prime Algorithm-----------------------------------------------//
    public WeightedUndirectedGraph prim() {
        //variable declaration
        int n = get_numOfVertices();
        WeightedUndirectedGraph mst = new WeightedUndirectedGraph(n);
        Float[] key = new Float[n];
        Integer[] pi = new Integer[n];

        //init the key array
        key[0] = 0f;
        for (int i = 1; i < n; ++i) {
            key[i] = Float.MAX_VALUE;
        }

        //set the 0 vertices father to null
        pi[0] = null;

        //init the PQ
        PriorityQueue<Integer> pq = new PriorityQueue<>(n, (o1, o2) -> key[o1].compareTo(key[o2]));
        for (int i = 0; i < n; ++i) {
            pq.add(i);
        }

        while (!pq.isEmpty()) {
            //get the head of the pq
            Integer u = pq.poll();
            for (Edge edge : _adjacencylist[u]) {
                //v is the second vertex connected with u
                Integer v = edge.getOther(u);
                //if v is in pq and there is a better way to reach it
                if (pq.contains(v) && edge.get_w() < key[v]) {
                    pi[v] = u;
                    key[v] = edge.get_w();
                    //update the pq cause key was changed
                    pq.remove(v);
                    pq.add(v);
                }
            }
        }
        //add edges to the mst
        for (int i = 1; i < n; ++i) {
            mst.addEdge(i, pi[i], key[i]);
        }
        return mst;
    }

    //-----------------------------------------------Q2: New MST-----------------------------------------------//
    public void mstNewEdge(Edge e) {
        this.addEdge(e);
        LinkedList<Edge> circle = this.findCircle();
        if (circle == null) {
            return;
        }
        float maxWeight = circle.peek().get_w();
        Edge maxEdge = circle.peek();
        for (Edge edge : circle) {
            if (edge.get_w() > maxWeight) {
                maxWeight = edge.get_w();
                maxEdge = edge;
            }
        }
        removeEdge(maxEdge);

    }

    //find a circle using a DFS based algorithm
    private LinkedList<Edge> findCircle() {
        //start DFS till finding a backwards edge
        //variable declaration
        final int WHITE = 0;
        final int GRAY = 1;
        final int BLACK = 2;
        AtomicInteger time = new AtomicInteger(0);
        int n = get_numOfVertices();
        int[] color = new int[n];
        Edge[] pi = new Edge[n];
        int[] d = new int[n];
        int[] f = new int[n];
        LinkedList<Edge> circle;
        //init arrays
        for (int i = 0; i < n; i++) {
            color[i] = WHITE;
            pi[i] = null;
        }
        for (int u = 0; u < n; u++) {
            //DFS visit
            if (color[u] == WHITE) {
                circle = dfsVisit(u, color, pi, d, f, time);
                if (circle != null) {
                    return circle;
                }
            }
        }
        //if no backwards edge found
        return null;
    }

    //DFS visit that returns a circle if found
    private LinkedList<Edge> dfsVisit(int u, int[] color, Edge[] pi, int[] d, int[] f, AtomicInteger time) {
        final int WHITE = 0;
        final int GRAY = 1;
        final int BLACK = 2;
        LinkedList<Edge> circle = null;
        color[u] = GRAY;
        time.incrementAndGet();
        d[u] = time.get();
        for (Edge edge : _adjacencylist[u]) {
            //v is the second vertex connected with u
            int v = edge.getOther(u);
            //if this edge is a backwards edge find the circle
            if (color[v] == GRAY && v != pi[u].getOther(u)) {
                circle = getCircle(v, u, edge, pi);
                break;
            }
            //if not continue dfsVisit
            if (color[v] == WHITE) {
                pi[v] = edge;
                circle = dfsVisit(v, color, pi, d, f, time);
                if (circle != null) {
                    return circle;
                }
            }
        }
        color[u] = BLACK;
        time.incrementAndGet();
        f[u] = time.get();
        return circle;
    }

    //gets 2 vertices and the pi(fathers array) and returns a list of a circle
    private LinkedList<Edge> getCircle(int v, int y, Edge e, Edge[] pi) {
        LinkedList<Edge> circle = new LinkedList<>();
        circle.add(e);
        int u = y;
        Edge father = pi[u];
        while (u != v) {
            circle.add(father);
            u = father.getOther(u);
            father = pi[u];
        }
        return circle;
    }


    //-----------------------------------------------Q3: Main Methode-----------------------------------------------//
    public static void main(String[] args) {
        System.out.println("Creating a weighted undirected graph with 20 vertices and 50 edges:");
        WeightedUndirectedGraph graph = new WeightedUndirectedGraph(20);
        graph.addEdge(0,1,1);
        graph.addEdge(0,2,2);
        graph.addEdge(1,3,3);
        graph.addEdge(2,3,4);
        graph.addEdge(2,4,5);
        graph.addEdge(3,4,6);
        graph.addEdge(4,5,7);
        graph.addEdge(3,5,8);
        graph.addEdge(4,7,9);
        graph.addEdge(5,9,10);
        graph.addEdge(5,6,11);
        graph.addEdge(6,10,12);
        graph.addEdge(6,7,13);
        graph.addEdge(7,8,14);
        graph.addEdge(8,9,15);
        graph.addEdge(19,18,16);
        graph.addEdge(17,18,17);
        graph.addEdge(16,19,18);
        graph.addEdge(18,14,19);
        graph.addEdge(16,17,20);
        graph.addEdge(15,10,21);
        graph.addEdge(16,15,22);
        graph.addEdge(14,9,23);
        graph.addEdge(9,10,24);
        graph.addEdge(14,15,25);
        graph.addEdge(12,14,26);
        graph.addEdge(13,15,27);
        graph.addEdge(13,11,28);
        graph.addEdge(8,12,29);
        graph.addEdge(5,11,30);
        graph.addEdge(4,17,31);
        graph.addEdge(3,13,32);
        graph.addEdge(6,18,33);
        graph.addEdge(1,8,34);
        graph.addEdge(0,9,35);
        graph.addEdge(19,3,36);
        graph.addEdge(6,12,37);
        graph.addEdge(2,11,38);
        graph.addEdge(7,16,39);
        graph.addEdge(8,17,40);
        graph.addEdge(19,5,41);
        graph.addEdge(12,4,42);
        graph.addEdge(0,19,43);
        graph.addEdge(1,4,44);
        graph.addEdge(10,11,45);
        graph.addEdge(11,12,46);
        graph.addEdge(12,13,47);
        graph.addEdge(13,14,48);
        graph.addEdge(10,4,49);
        graph.addEdge(6,0,50);
        graph.printGraph();
        System.out.println("-------------------------Q1: Prim Algorithm to find MST-------------------------");
        WeightedUndirectedGraph mst = graph.prim();
        mst.printGraph();
        System.out.println("-------------------------Q2 Part 1: Adding an edge that doesnt affect the MST-------------------------");
        Edge heavyEdge = new Edge(1,19,500);
        System.out.println("Added Edge: vertex 1 is connected with 19 weighed: 500.0");
        mst.mstNewEdge(heavyEdge);
        mst.printGraph();
        System.out.println("-------------------------Q2 Part 2: Adding an edge that does affect the MST-------------------------");
        Edge lightEdge = new Edge(1,19,-500);
        System.out.println("Added Edge: vertex 1 is connected with 19 weighed: -500.0");
        mst.mstNewEdge(lightEdge);
        mst.printGraph();
    }
}
