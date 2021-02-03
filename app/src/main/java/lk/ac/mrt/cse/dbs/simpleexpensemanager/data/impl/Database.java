package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.IAccountSchema;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ITransactionSchema;

public class Database extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "180212c";
    private static final int DATABASE_VERSION =  1;
    public Database(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(IAccountSchema.ACCOUNT_TABLE_CREATE);
        db.execSQL(ITransactionSchema.TRANSACTION_TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "
                + IAccountSchema.ACCOUNT_TABLE);
        db.execSQL("DROP TABLE IF EXISTS "
                + ITransactionSchema.TRANSACTION_TABLE);
        onCreate(db);
    }

}
