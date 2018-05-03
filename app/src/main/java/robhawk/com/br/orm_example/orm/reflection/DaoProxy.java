package robhawk.com.br.orm_example.orm.reflection;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.List;

import robhawk.com.br.orm_example.orm.Dao;
import robhawk.com.br.orm_example.orm.annotation.Query;

public class DaoProxy implements InvocationHandler {

    private final ModelHelper modelHelper;
    private final Class<?> modelType;
    private final Dao dao;

    DaoProxy(Class<?> modelType) {
        this.modelType = modelType;
        this.modelHelper = new ModelHelper();
        this.dao = new Dao();
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] objects) {

        if (method.isAnnotationPresent(Query.class))
            return query(method, objects, dao);
        else if (method.getName().equals("listAll"))
            return dao.listAll(modelType);
        else if (method.getName().equals("findById"))
            return dao.findById((int) objects[0], modelType);
        else if (method.getName().equals("insert"))
            return dao.insert(objects[0]);
        else if (method.getName().equals("update"))
            return dao.update(objects[0]);
        else if (method.getName().equals("delete"))
            return dao.delete(objects[0]);

        return null;
    }

    private Object query(Method method, Object[] objects, Dao dao) {
        Query query = method.getAnnotation(Query.class);
        String sql = query.value();
        Class<?> type = modelHelper.getReturnType(method);
        if (method.getReturnType() == List.class)
            return dao.getResultList(type, sql, objects);
        return dao.getResultObject(type, sql, objects);
    }

}
