package test;

import java.util.LinkedList;

public class weightedUndirectedGraph {
    LinkedList<Edge>[] _adjacencylist;
    int _numOfVertices;

    public weightedUndirectedGraph(int _numOfVertices) {
        this._numOfVertices = _numOfVertices;
        _adjacencylist = new LinkedList[_numOfVertices];
        for (int i = 0; i < _numOfVertices; ++i){
            _adjacencylist[i] = new LinkedList<>();
        }
    }

    public void addEdge(int v1, int v2, float w) {
        if (v1 > _numOfVertices || v2 > _numOfVertices || v1 < 1 || v2 < 1)
            return;
//TODO Add validiation checks if the E exists
        Edge e = new Edge(v1, v2, w);
        _adjacencylist[v1 - 1].add(e);
        _adjacencylist[v2 - 1].add(e);

    }

    public void printGraph() {
        for (int i = 0; i < _numOfVertices; i++) {
            LinkedList<Edge> list = _adjacencylist[i];
            for (Edge edge : list) {
                if (edge.getV1() == i+1){
                    System.out.println("vertex-" + edge.getV1() + " is connected to vertex-" +
                            edge.getV2() + " with weight " + edge.get_weight());
                }
            }
        }
    }

    private class Edge {
        private final int _v1;
        private final int _v2;
        private final float _w;

        public int getV1() {
            return _v1;
        }

        public int getV2() {
            return _v2;
        }

        public float get_weight() {
            return _w;
        }

        public Edge(int v1, int v2, float weight) {
            this._v1 = v1;
            this._v2 = v2;
            this._w = weight;
        }
    }


    //-------------------------------------------------------------------------------------------------////
    public static void main(String[] args) {
        weightedUndirectedGraph g = new weightedUndirectedGraph(7);
        g.addEdge(1, 2, 10);
        g.addEdge(1, 3, 6);
        g.addEdge(5, 3, 6);

        g.printGraph();
    }
}