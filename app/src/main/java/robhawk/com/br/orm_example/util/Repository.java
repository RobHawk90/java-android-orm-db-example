package robhawk.com.br.orm_example.util;

import android.util.SparseArray;

import java.util.LinkedList;
import java.util.List;

public class Repository<T> extends SparseArray<T> {
    public List<T> listAll() {
        List<T> items = new LinkedList<>();
        for (int i = 0; i < size(); i++)
            items.add(get(keyAt(i)));
        return items;
    }
}
