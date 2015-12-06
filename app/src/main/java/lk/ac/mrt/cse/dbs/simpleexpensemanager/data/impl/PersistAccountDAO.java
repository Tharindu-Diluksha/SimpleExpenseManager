package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.InvalidAccountException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;

/**
 * Created by Tharindu Diluksha on 2015-12-04.
 */
public class PersistAccountDAO implements AccountDAO {

    private Context context;
    private SQLiteDatabase database;
    private List<Account> accountList;
    private ContentValues contentValues;
    private DBhandler dbHelper;

    public PersistAccountDAO(Context context) {
        this.context = context;
        dbHelper = DBhandler.getDBinstance(context);
        database = dbHelper.getWritableDatabase();
        accountList = new ArrayList<>();
    }
    @Override
    public List<String> getAccountNumbersList() {
        List<String> accountNumberList = new ArrayList<>();
        database = dbHelper.getWritableDatabase();
        String[] columns = new String[]{DBhandler.AccNo};
        Cursor cursor = database.query(DBhandler.TABLE_ACCOUNT, columns, null, null, null, null, null);
        while (cursor.moveToNext()) {
            accountNumberList.add(cursor.getString(cursor.getColumnIndex(DBhandler.AccNo)));
        }

        return accountNumberList;
    }

    @Override
    public List<Account> getAccountsList() {
        Account accountInfo;
        database = dbHelper.getWritableDatabase();
        Cursor cursor = database.query(DBhandler.TABLE_ACCOUNT, null, null, null, null, null, null);
        if (cursor.getCount() == 0) {
            return null;
        } else {
            while (cursor.moveToNext()) {
                accountInfo = new Account(cursor.getString(cursor.getColumnIndex(DBhandler.AccNo)),
                        cursor.getString(cursor.getColumnIndex(DBhandler.Bank)),
                        cursor.getString(cursor.getColumnIndex(DBhandler.AccHolder)),
                        cursor.getFloat(cursor.getColumnIndex(DBhandler.InitBalance))
                );
                accountList.add(accountInfo);
            }
        }
        database.close();

        return null;
    }

    @Override
    public Account getAccount(String accountNo) throws InvalidAccountException {
        Account account=null;
        database=dbHelper.getWritableDatabase();
        Cursor cursor = database.query(DBhandler.TABLE_ACCOUNT, null, DBhandler.AccNo + "=?", new String[]{accountNo}, null, null, null);
        while (cursor.moveToNext()) {
            account= new Account(cursor.getString(cursor.getColumnIndex(DBhandler.AccNo)),
                    cursor.getString(cursor.getColumnIndex(DBhandler.Bank)),
                    cursor.getString(cursor.getColumnIndex(DBhandler.AccHolder)),
                    cursor.getFloat(cursor.getColumnIndex(DBhandler.InitBalance))
            );

        }
        cursor.close();
        database.close();
        return account;
    }

    private boolean checkAccountExists(String accountNo) {
        database = dbHelper.getWritableDatabase();
        Cursor cursor = database.query(DBhandler.TABLE_ACCOUNT, new String[]{DBhandler.AccNo}, DBhandler.AccNo + "=?", new String[]{accountNo}, null, null, null);
        while (cursor.moveToNext()) {
            if (cursor.getString(cursor.getColumnIndex(DBhandler.AccNo)).equals(accountNo))
                return true;
        }
        return false;
    }

    @Override
    public void addAccount(Account account) {

        if (checkAccountExists(account.getAccountNo())) {
            Toast.makeText(context, "Already Exists", Toast.LENGTH_SHORT).show();
        } else {
            contentValues = new ContentValues();
            contentValues.put(DBhandler.AccNo, account.getAccountNo());
            contentValues.put(DBhandler.InitBalance, account.getBalance());
            contentValues.put(DBhandler.AccHolder, account.getAccountHolderName());
            contentValues.put(DBhandler.Bank, account.getBankName());
            Long i = 0L;
            try {
                i = database.insert(DBhandler.TABLE_ACCOUNT, null, contentValues);
                database.close();
            } catch (Exception ex) {
                Log.d("TAG", "SQLite error occurred" + ex.getCause().toString());
            } finally {
                if (i == -1) {
                    Toast.makeText(context, "Failed to add account info", Toast.LENGTH_SHORT).show();
                } else {
                    Log.i("TAG", "SQLite data entered successfully");
                }
            }
        }

    }

    @Override
    public void removeAccount(String accountNo) throws InvalidAccountException {

        int i = 0;
        try {
            i = database.delete(DBhandler.TABLE_ACCOUNT, DBhandler.AccNo + "=?", new String[]{accountNo});
            database.close();
        } catch (Exception e) {
            Log.d("Error", "SQLite error occured");
        } finally {
            if (i == -1) {
                Toast.makeText(context, "Failed to remove account info", Toast.LENGTH_SHORT).show();
            } else {
                Log.i("Success", "SQLite data deleted");
            }

        }
    }

    @Override
    public void updateBalance(String accountNo, ExpenseType expenseType, double amount) throws InvalidAccountException {
        Account account = getAccount(accountNo);
        if (account != null) {
            double finalbalance;
            switch (expenseType) {
                case EXPENSE:
                    finalbalance = account.getBalance() - amount;
                    updateAccountData(finalbalance, accountNo);
                    break;
                case INCOME:
                    finalbalance = account.getBalance() + amount;
                    updateAccountData(finalbalance, accountNo);
                    break;
            }
        }
    }

    private void updateAccountData(double finalbalance, String accountNo) {
        ContentValues updateValues = new ContentValues();
        updateValues.put(DBhandler.InitBalance, finalbalance);
        database=dbHelper.getWritableDatabase();
        int i=database.update(DBhandler.TABLE_ACCOUNT, updateValues, DBhandler.AccNo + "=?", new String[]{accountNo});
        Log.e("TAG UPDATE ", String.valueOf(i));
    }
}
