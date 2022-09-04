package util;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.*;

import static model.Participant.cross;
import static model.Participant.tick;

public abstract class ObservableUtils {

    public static <T,V> Map<T,V> toMap(Map<T, ObjectProperty<V>> observableMap) {
        Map<T, V> out = new HashMap<>();
        observableMap.forEach((wt, wp) -> out.put(wt, wp.get()));
        return out;
    }

    public static <T,V> Map<T,ObjectProperty<V>> toObservableMap(Map<T, V> map){
        Map<T,ObjectProperty<V>> out =  FXCollections.observableHashMap();
        map.forEach((wt, wp) -> out.put(wt, new SimpleObjectProperty<>(wp)));
        return out;
    }

    public static <T> List<T> toList(ObservableList<T> list) {
        ArrayList<T> out = new ArrayList<>();
        out.addAll(list);
        return out;
    }

    public static SimpleStringProperty toSimpleStringProperty(BooleanProperty booleanProperty) {
        if (booleanProperty.get()) {
            return new SimpleStringProperty(tick);
        } else {
            return new SimpleStringProperty(cross);
        }
    }

    public static boolean equals(BooleanProperty p1, BooleanProperty p2) {
        if (p1 == null && p2 == null)
            return true;
        if (p1 ==null || p2 == null)
            return false;
        return p1.get()==p2.get();
    }

    public static boolean equals(IntegerProperty p1, IntegerProperty p2) {
        if (p1 == null && p2 == null)
            return true;
        if (p1 ==null || p2 == null)
            return false;
        return p1.get()==p2.get();
    }

    public static <T> boolean equals(ObjectProperty<T> p1, ObjectProperty<T> p2) {
        if (p1 == null && p2 == null)
            return true;
        if (p1 ==null || p2 == null)
            return false;
        return p1.get().equals(p2.get());
    }

    public static boolean equals(StringProperty p1, StringProperty p2) {
        if (p1 == null && p2 == null)
            return true;
        if (p1 ==null || p2 == null)
            return false;
        return p1.get().equals(p2.get());
    }

    public static <T,V> boolean equals(Map<T,ObjectProperty<V>> p1, Map<T,ObjectProperty<V>> p2) {
        if (p1 == null && p2 == null)
            return true;
        if (p1 ==null || p2 == null)
            return false;
        return toMap(p1).equals(toMap(p2));
    }

    public static <T> boolean equals(ObservableList<T> p1, ObservableList<T> p2) {
        if (p1 == null && p2 == null)
            return true;
        if (p1 ==null || p2 == null)
            return false;
        if (p1.size() != p2.size())
            return false;
        for(int i=0; i< p1.size(); i++) {
            if(!Objects.equals(p1.get(i),p2.get(i)))
                return false;
        }
        return true;
    }
}
