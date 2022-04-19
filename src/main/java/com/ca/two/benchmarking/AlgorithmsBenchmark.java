package com.ca.two.benchmarking;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

public class AlgorithmsBenchmark {
    public static void main(String[] args) {
        System.out.println("Benchmark started");

        Options opt = new OptionsBuilder()
                .include(AlgorithmsBenchmark.class.getSimpleName())
                .forks(1)
                .build();

        try {
            new Runner(opt).run();
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("Benchmark finished");
    }

    @Benchmark
    public void benchmark() {
        //Test to benchmark
        for (int i = 0; i < 1000; i++) {
            var x = i * i;
        }
    }
}
