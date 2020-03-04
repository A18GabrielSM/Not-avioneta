package com.example.t2_f_a18gabrielsm;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SalariesDB extends SQLiteOpenHelper {

    SQLiteDatabase sqLiteDatabase;

    public final static String NOME_BD = "salaries.db";
    public final static int VERSION_BD = 1;

    public SalariesDB(Context context) {
        super(context, NOME_BD, null, VERSION_BD);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS \"salary\" (\n" +
                "\t\"month\"\tTEXT,\n" +
                "\t\"total_salary\"\tREAL,\n" +
                "\tPRIMARY KEY(\"month\")\n" +
                ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public Long insertSalary(Salary salary){
        ContentValues cv = new ContentValues();
        cv.put("month", salary.getMonth());
        cv.put("total_salary", salary.getSalary_total());
        Long id = sqLiteDatabase.insert("salary", null, cv);

        return id;
    }

}
