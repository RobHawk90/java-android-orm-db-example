package robhawk.com.br.orm_example.orm.reflection;

import java.lang.reflect.Proxy;

public class DaoFactory {

    public static <T extends Dao> T create(Class<T> daoType) {
        Class<?> modelType = new ModelHelper().getModelType(daoType);
        return daoType.cast(
                Proxy.newProxyInstance(
                        daoType.getClassLoader(),
                        new Class[]{daoType},
                        new DaoProxy(modelType)
                )
        );
    }

}
