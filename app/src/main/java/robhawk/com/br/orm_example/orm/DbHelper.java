package robhawk.com.br.orm_example.orm;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbHelper extends SQLiteOpenHelper {

    public DbHelper(Context context) {
        super(context, "orm_example", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE user (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "name TEXT NOT NULL," +
                "email TEXT NOT NULL," +
                "password TEXT NOT NULL" +
                ")");

        db.execSQL("CREATE TABLE task (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "description TEXT NOT NULL," +
                "date TEXT NOT NULL," +
                "completed INTEGER NOT NULL," +
                "idUser INTEGER NOT NULL," +
                "FOREIGN KEY(idUser) REFERENCES user(id)" +
                ")");

        db.execSQL("CREATE TABLE user_task (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "idUser INTEGER NOT NULL," +
                "idTask INTEGER NOT NULL," +
                "FOREIGN KEY(idUser) REFERENCES user(id)," +
                "FOREIGN KEY(idTask) REFERENCES task(id)" +
                ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}
