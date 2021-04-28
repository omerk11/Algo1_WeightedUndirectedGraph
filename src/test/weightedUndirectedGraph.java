package test;

import java.util.LinkedList;
import java.util.PriorityQueue;

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

    public int get_numOfVertices() {
        return _numOfVertices;
    }

    public void addEdge(int v1, int v2, float w) {
        if (v1 >= _numOfVertices || v2 >= _numOfVertices || v1 < 0 || v2 < 0)
            return;
//TODO Add validiation checks if the E exists
        Edge e = new Edge(v1, v2, w);
        _adjacencylist[v1].add(e);
        _adjacencylist[v2].add(e);

    }

    public void printGraph() {
        for (int i = 0; i < _numOfVertices; i++) {
            LinkedList<Edge> list = _adjacencylist[i];
            for (Edge edge : list) {
                if (edge.getV1() == i){
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
        weightedUndirectedGraph g = new weightedUndirectedGraph(4);
        g.addEdge(0, 1, 1f);
        g.addEdge(0, 3, 5f);
        g.addEdge(1, 3, 2f);
        g.addEdge(3, 2, 1f);
        weightedUndirectedGraph mst = g.prim();
        mst.printGraph();
    }

    //--------------------------------------- Q1 Prim ----------------------------------------------------------//

    public weightedUndirectedGraph prim(){
        int n = get_numOfVertices();
        weightedUndirectedGraph mst = new weightedUndirectedGraph(this._numOfVertices);
        Float[] key = new Float[n];
        Integer[] pi = new Integer[n];
        key[0] = 0f;
        //pi[0] = null;
        for(int i= 1; i < n ; ++i ){
            key[i] = Float.MAX_VALUE;
        }

        /// init PQ
        PriorityQueue<Integer> pq = new PriorityQueue<>(n,((o1, o2) -> key[o1].compareTo(key[o2])));
        for(int i = 0; i< n; ++i){
            pq.add(i);
        }

        while(!pq.isEmpty()){
            Integer u = pq.poll();
            for(Edge edge : _adjacencylist[u]){
                Integer v = (edge.getV1() == u ? edge.getV2(): edge.getV1());
                if(pq.contains(v) && edge.get_weight() < key[v]){
                    pi[v] = u;
                    key[v] = edge.get_weight();
                    pq.remove(v);
                    pq.add(v);
                }
            }
        }

        for(int i =1 ; i<n;++i){
            mst.addEdge(i,pi[i],key[i]);
        }

        return mst;
    }

    //--------------------------------------- Q2: newMST ----------------------------------------------------------//

    public weightedUndirectedGraph newMST(Edge e){
        weightedUndirectedGraph mst = new weightedUndirectedGraph(get_numOfVertices());
        mst.addEdge(e.getV1(),e.getV2(),e.get_weight());


        return mst;
    }
    private LinkedList<Edge>getCircle(){


        return null;
    }
}