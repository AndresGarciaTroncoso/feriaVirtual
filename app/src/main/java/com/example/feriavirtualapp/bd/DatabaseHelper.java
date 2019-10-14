package com.example.feriavirtualapp.bd;


import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class DatabaseHelper extends SQLiteOpenHelper {

    String DB_PATH=null;
    private static String DB_NAME="FrutosMaipoBD.db";
    public SQLiteDatabase myDataBase;
    private final Context myContext;

    public DatabaseHelper(Context context)
    {
        super(context,DB_NAME,null,11);
        this.myContext=context;
        this.DB_PATH="/data/data/"+ context.getPackageName()+"/"+"databases/";
        Log.e("PATH1",DB_PATH);
    }

    public void createDataBase() throws IOException {
        boolean dbExist = checkDataBase();
        if(dbExist){

        }else
        {
            this.getReadableDatabase();
            try {
                copyDataBase();
            }catch (IOException e){
                throw  new Error("Error copiando BD");
            }
        }
    }

    public void copyDataBase()  throws  IOException{
        InputStream myInput = myContext.getAssets().open(DB_NAME);
        String outFileName = DB_PATH + DB_NAME;
        OutputStream myOutput = new FileOutputStream(outFileName);
        byte[] buffer = new byte[10];
        int length;
        while ((length= myInput.read(buffer))> 0)
        {
            myOutput.write(buffer,0,length);
        }
        myOutput.flush();
        myOutput.close();
        myInput.close();
    }

    public void openDataBase() throws SQLException {
        String myPath = DB_PATH + DB_NAME;
        myDataBase = SQLiteDatabase.openDatabase(myPath,null,SQLiteDatabase.OPEN_READWRITE);
    }


    private boolean checkDataBase() {
        File databasePath = myContext.getDatabasePath(DB_NAME);
        return databasePath.exists();
    }

    public synchronized  void close()
    {
        if(myDataBase != null)
            myDataBase.close();
        super.close();
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (newVersion>oldVersion)
            try {
                copyDataBase();
            }catch (IOException e){
                e.printStackTrace();
            }
    }






}
