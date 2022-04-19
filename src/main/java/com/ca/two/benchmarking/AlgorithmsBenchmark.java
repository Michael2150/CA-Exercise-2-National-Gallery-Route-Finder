package com.ca.two.benchmarking;

import com.ca.two.graph.Algorithms;
import com.ca.two.graph.Graph;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.openjdk.jmh.runner.options.WarmupMode;

public class AlgorithmsBenchmark {
    public static void main(String[] args) {
        System.out.println("Benchmark started");

        Options opt = new OptionsBuilder()
                .include(AlgorithmsBenchmark.class.getSimpleName())
                .forks(1)
                .warmupIterations(1)
                .measurementIterations(3)
                .mode(Mode.AverageTime)
                .build();

        try {
            new Runner(opt).run();
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("Benchmark finished");
    }

    @State(Scope.Benchmark)
    public static class BenchmarkState {
        private Graph<String> graph;

        @Setup(Level.Trial)
        public void setup() {
            //Create a graph to test with
            graph = new Graph<>();
            graph.addNode("S");
            graph.addNode("A", "S");
            graph.addNode("B", "S", "A");
            graph.addNode("C", "S");
            graph.addNode("D", "A", "B");
            graph.addNode("H", "B");
            graph.addNode("F", "H", "D");
            graph.addNode("G", "H");
            graph.addNode("L", "C");
            graph.addNode("I", "L");
            graph.addNode("J", "L", "I");
            graph.addNode("K", "I", "J");
            graph.addNode("E", "K", "G");

            //There should be 18 edges in the graph
            graph.getNode("S").setEdgeWeight(graph.getNode("A"), 7f/8f);
            graph.getNode("S").setEdgeWeight(graph.getNode("B"), 2f/8f);
            graph.getNode("A").setEdgeWeight(graph.getNode("B"), 3f/8f);
            graph.getNode("S").setEdgeWeight(graph.getNode("C"), 3f/8f);
            graph.getNode("A").setEdgeWeight(graph.getNode("D"), 4f/8f);
            graph.getNode("B").setEdgeWeight(graph.getNode("H"), 1f/8f);
            graph.getNode("B").setEdgeWeight(graph.getNode("D"), 4f/8f);
            graph.getNode("D").setEdgeWeight(graph.getNode("F"), 5f/8f);
            graph.getNode("H").setEdgeWeight(graph.getNode("G"), 2f/8f);
            graph.getNode("H").setEdgeWeight(graph.getNode("F"), 3f/8f);
            graph.getNode("C").setEdgeWeight(graph.getNode("L"), 2f/8f);
            graph.getNode("L").setEdgeWeight(graph.getNode("I"), 4f/8f);
            graph.getNode("L").setEdgeWeight(graph.getNode("J"), 4f/8f);
            graph.getNode("I").setEdgeWeight(graph.getNode("J"), 6f/8f);
            graph.getNode("I").setEdgeWeight(graph.getNode("K"), 4f/8f);
            graph.getNode("J").setEdgeWeight(graph.getNode("K"), 4f/8f);
            graph.getNode("K").setEdgeWeight(graph.getNode("E"), 5f/8f);
            graph.getNode("G").setEdgeWeight(graph.getNode("E"), 2f/8f);
        }
    }

    @Benchmark
    public void Dijkstra(Blackhole bh, BenchmarkState state) {
        bh.consume(Algorithms.Dijkstra(state.graph, "S", "E"));
    }

    @Benchmark
    public void BFS(Blackhole bh, BenchmarkState state) {
        bh.consume(Algorithms.BFS(state.graph, "S", "E"));
    }

    @Benchmark
    public void DFSAllPaths(Blackhole bh, BenchmarkState state) {
        bh.consume(Algorithms.DFSAllPaths(state.graph, "S", "E"));
    }
}
