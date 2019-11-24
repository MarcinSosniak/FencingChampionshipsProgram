package util;

public class Pointer<T> {
    private T t=null;
    public Pointer() {}
    public T get() {return t;}
    public void set(T t) {this.t=t;}
}

