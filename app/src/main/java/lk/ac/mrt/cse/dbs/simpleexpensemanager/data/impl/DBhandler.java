package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;

/**
 * Created by Tharindu Diluksha on 2015-12-04.
 */
public class DBhandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "dil.db";

    private static DBhandler DBinstance = null;

    public DBhandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        Log.e("TAG", "ACCESSED DB HELPER");
    }


    public static DBhandler getDBinstance(Context context) {
        if (DBinstance == null) {
            DBinstance = new DBhandler(context.getApplicationContext());
        }
        return DBinstance;
    }

    public static  final String TABLE_ACCOUNT = "ACCOUNT";
    public static final String AccNo = "AccountNO";
    public static final String Bank ="Bank";
    public static final String AccHolder = "Account_Holder";
    public static final String InitBalance ="Initial_Balance";


    public static final String TABLE_TRANSACTION = "TransactionInfo";
    public static final String TrType = "Type";
    public static final String Amount ="Amount";
    public static final String Date = "Date";
    public static final String TransID = "TransactionID";

    private static final String CREATE_TABLE_ACCOUNT = "CREATE TABLE "
            + TABLE_ACCOUNT + "(" + AccNo + " VARCHAR(30) PRIMARY KEY," + Bank
            + " VARCHAR(20)," + AccHolder + " VARCHAR(30)," + InitBalance
            + " REAL" + ");";

    public static final String CREATE_TABLE_TRANSACTION = "CREATE TABLE " + TABLE_TRANSACTION + " ( " +
            TransID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            AccNo + " VARCHAR(30), " + Date + " DATETIME, " + Amount + " REAL, " + TrType + " VARCHAR(40)" + ");";

    //VARCHAR(10)
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_ACCOUNT);
        db.execSQL(CREATE_TABLE_TRANSACTION);


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed, all data will be gone!!!
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ACCOUNT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TRANSACTION);

        // Create tables again
        onCreate(db);

    }

        }
