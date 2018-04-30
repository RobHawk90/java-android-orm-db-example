package robhawk.com.br.orm_example;

import android.app.Application;

import robhawk.com.br.orm_example.orm.DbHelper;

public class OrmExampleApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        DbHelper.init(this);
    }
}
