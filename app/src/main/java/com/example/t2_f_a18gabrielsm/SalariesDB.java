package com.example.t2_f_a18gabrielsm;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

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

    public ArrayList<String> selectSalaries() {
        ArrayList<String> salaryList = new ArrayList<>();
        salaryList.clear();
        Cursor select = sqLiteDatabase.rawQuery("SELECT * FROM salary", null);

        salaryList.add("Total_Salary\tMonth\n");

        if (select.moveToFirst()){
            while (!select.isAfterLast()){
                salaryList.add(select.getFloat(1) + "\t\t\t" + select.getString(0) + "\n");
                Log.i("LINE", salaryList.get(salaryList.size()-1));
                select.moveToNext();
            }
        }
        return salaryList;
    }

}
