package utils;

public class Pair<T,V> {

    private T first;
    private V second;

    public Pair(T first, V second){
        this.first = first;
        this.second = second;
    }

    public V getSecond() {
        return second;
    }

    public T getFirst() {
        return first;
    }
}
