package matc89.exercicio3;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.Random;

public class TarefaDB extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "tese";

    TarefaDB (Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS TAREFA ( DESCRICAO VARCHAR(200), PRIORIDADE NUMBER);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS TAREFA");

        onCreate(sqLiteDatabase);
    }

    public boolean insertAtiv(String descricao, int prioridate){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Random r = new Random();
        int random = r.nextInt(100);
        ContentValues cv = new ContentValues();
        cv.put("DESCRICAO", descricao);
        cv.put("PRIORIDADE", prioridate);

        return sqLiteDatabase.insert("TAREFA", null, cv) != -1;

    }

    public Cursor selectAll() {
        ArrayList<String> quotes = new ArrayList<>();

        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("select * from  tarefa order by PRIORIDADE asc", null);
        if (cursor != null) {
            return cursor;
        } else {
            return null;
        }
    }

    public boolean delete(String descricao) {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();

        sqLiteDatabase.delete("tarefa", "DESCRICAO = ?", new String[]{descricao});
        return true;
    }

    public boolean select(String descricao) {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();

        Cursor cursor = sqLiteDatabase.rawQuery("select * from tarefa where DESCRICAO = ?", new String[] {descricao});

        System.out.println(cursor.getCount());
        if (cursor.getCount() > 0) {
            return true;
        } else return false;
    }

    public void deleteAll(){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.execSQL("delete from tarefa");
    }
}
