package flashbar.com.testapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class Database
{
    Context context;
    MediaDataBaseHelper helper;

    public Database(Context context) {
        this.context=context;
        helper=new MediaDataBaseHelper(context);
    }


    public void insertData(String name){
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaDataBaseHelper.COLUMN_NAME,name);
        db.insert(MediaDataBaseHelper.TABLE_NAME_FIRST,null,contentValues);
    }

    public void showData(){
        SQLiteDatabase db = helper.getWritableDatabase();
        String[] columns={MediaDataBaseHelper.COLUMN_NAME};
        Cursor cursor = db.query(MediaDataBaseHelper.TABLE_NAME_FIRST,columns,null,null,null,null,null);

        while(cursor.moveToNext()){
            int name = cursor.getColumnIndex(MediaDataBaseHelper.COLUMN_NAME);
            Constants.showMsg(context,cursor.getString(name));
        }
    }


    private static class MediaDataBaseHelper extends SQLiteOpenHelper {
        private static final int DATABASE_VERSION=2;
        private static final String DATABASE_NAME="Default";

        private static final String TABLE_NAME_FIRST="First";

        private static final String COLUMN_ID="id";
        private static final String COLUMN_NAME="name";

        private Context context;

        public MediaDataBaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
            this.context=context;
        }


        @Override
        public void onCreate(SQLiteDatabase db) {

            String query="CREATE TABLE "+TABLE_NAME_FIRST+"("
                    +COLUMN_ID+" INTEGER PRIMARY KEY AUTOINCREMENT,"
                    +COLUMN_NAME+" VARCHAR"
                    +");";

          /* String nowPlayingQuery="CREATE TABLE "+TABLE_NAME_NOWPLAYING+"("
                    +"_id INTEGER PRIMARY KEY AUTOINCREMENT,"
                    +COLUMN_ID+" INTEGER,"
                    +" FOREIGN KEY("+COLUMN_ID+") REFERENCES "+TABLE_NAME_AUDIO+"("+COLUMN_ID+") ON DELETE CASCADE,"
                    +");";
          */

            // FOREIGN KEY(quiz_name) REFERENCES quizzes(quiz_name) ON DELETE CASCADE,

            try {
                db.execSQL(query);
            }
            catch (Exception e) {
                Log.e("error while creating database",e.toString());
                Constants.showMsg(context,e.toString());
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
        {
            try {
                String query="DROP TABLE  IF EXISTS "+TABLE_NAME_FIRST+";";
                db.execSQL(query);
                onCreate(db);
            }
            catch(Exception e) {
                Constants.showMsg(context,e.toString());
            }
        }
    }
}