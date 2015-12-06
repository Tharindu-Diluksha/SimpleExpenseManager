package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;

/**
 * Created by Tharindu Diluksha on 2015-12-04.
 */
public class PersistTransactionDAO implements TransactionDAO {

    private SQLiteDatabase database;
    private ContentValues contentValues;
    private DBhandler dbHelper;

    public PersistTransactionDAO(Context context) {
        dbHelper=DBhandler.getDBinstance(context);
        database=dbHelper.getWritableDatabase();

    }
    @Override
    public void logTransaction(Date date, String accountNo, ExpenseType expenseType, double amount) {
        contentValues=new ContentValues();
        database=dbHelper.getWritableDatabase();
        Log.e("TRANS date", String.valueOf(date.getTime()));
        Log.e("TRANS accN", accountNo);
        Log.e("TRANS ExpT", String.valueOf(expenseType));
        Log.e("TRANS ExpT", String.valueOf(amount));
        contentValues.put(DBhandler.AccNo, accountNo);
        contentValues.put(DBhandler.TrType,String.valueOf(expenseType));
        contentValues.put(DBhandler.Amount,amount);
        contentValues.put(DBhandler.Date,date.getTime());
        String[] columns=new String[]{DBhandler.AccNo,DBhandler.TrType,DBhandler.Amount,DBhandler.Date};
        long i=database.insert(DBhandler.TABLE_TRANSACTION, DBhandler.TransID, contentValues);
        if (i==-1){
            Log.e("TRANS insert", "ERROR");
        }else Log.e("TRANS insert", "Success "+i+" row id added");
        database.close();
    }

    @Override
    public List<Transaction> getAllTransactionLogs() {
        getPaginatedTransactionLogs(-1);
        return null;
    }

    @Override
    public List<Transaction> getPaginatedTransactionLogs(int limit) {
        database=dbHelper.getWritableDatabase();
        List<Transaction> transactionList=new ArrayList<>();
        String queryString;
        if(limit==-1){
            queryString="SELECT * FROM "+DBhandler.TABLE_TRANSACTION;
        }else{
            queryString="SELECT * FROM "+DBhandler.TABLE_TRANSACTION+" LIMIT "+String.valueOf(limit);
        }
        Cursor cursor = database.rawQuery(queryString, null);
        Log.e("TRANS retrieve", "Success "+cursor.getCount()+" row id added");
        if (cursor.moveToFirst()){
            do {
                Transaction transaction=new Transaction();
                transaction.setAccountNo(cursor.getString(cursor.getColumnIndex(DBhandler.AccNo)));
                transaction.setAmount(cursor.getDouble(cursor.getColumnIndex(DBhandler.Amount)));
                transaction.setDate(new Date(cursor.getLong(cursor.getColumnIndex(DBhandler.Date))));
                transaction.setExpenseType(ExpenseType.valueOf(cursor.getString(cursor.getColumnIndex(DBhandler.TrType))));
                transactionList.add(transaction);
            }while (cursor.moveToNext());
        }
        cursor.close();
        database.close();
        return transactionList;
    }
}
