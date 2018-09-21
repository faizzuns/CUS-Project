package user.com.cus.Utils;

import com.raizlabs.android.dbflow.annotation.Database;

/**
 * Created by User on 23/08/2017.
 * @author Ahmad Faiz Sahupala
 *
 * Kelas untuk menginisiasi dbFlow atau database lokal
 */
@Database(name = MyDatabase.NAME, version = MyDatabase.VERSION)
public class MyDatabase {
    public static final String NAME = "MyDataBase";

    public static final int VERSION = 1;
}
