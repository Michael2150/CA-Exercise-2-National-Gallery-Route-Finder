package com.ca.two.graph;

public class Edge<T> {
    private Node<T> from;
    private Node<T> to;
    private float weight;

    public Edge(Node<T> from, Node<T> to, float weight) {
        this.from = from;
        this.to = to;
        this.weight = weight;
    }

    //Getters and Setters
    public Node<T> getFrom() {
        return from;
    }

    public void setFrom(Node<T> from) {
        this.from = from;
    }

    public Node<T> getTo() {
        return to;
    }

    public void setTo(Node<T> to) {
        this.to = to;
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    //Contains Node
    public boolean contains(Node<T> node) {
        return from.equals(node) || to.equals(node);
    }

    //equals
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Edge<?> edge = (Edge<?>) o;

        return (this.from.equals(edge.from) && this.to.equals(edge.to)) ||
                (this.from.equals(edge.to) && this.to.equals(edge.from));
    }

    //toString
    @Override
    public String toString() {
        return "Edge{" +
                "from=" + from +
                ", to=" + to +
                ", weight=" + weight +
                '}';
    }
}
