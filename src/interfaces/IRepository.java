package interfaces;

import java.util.ArrayList;

public interface IRepository<T> {
    ArrayList<T> readAll();
    Boolean create(T t);
    Boolean update(T t);
    Boolean delete(T t);
    T findById(int id);
}
