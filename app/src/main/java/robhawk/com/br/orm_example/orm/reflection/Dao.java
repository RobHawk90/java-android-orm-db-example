package robhawk.com.br.orm_example.orm.reflection;

import java.util.List;

public interface Dao<T> {

    boolean insert(T model);

    boolean update(T model);

    boolean delete(T model);

    List<T> listAll();

    T findById(int id);

}
