package lk.ac.mrt.cse.dbs.simpleexpensemanager.control;

import android.content.Context;

import java.io.Serializable;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.control.exception.ExpenseManagerException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.PersistAccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.PersistTransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;

/**
 * Created by Tharindu Diluksha on 2015-12-04.
 */
public class PersistExpenseManager extends ExpenseManager implements Serializable {
    Context context;


    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {

        this.context = context;
    }

    public PersistExpenseManager(Context context) {
        setContext(context);
        setup();
    }

    @Override
    public void setup() {

        TransactionDAO perTransactionDAO = new PersistTransactionDAO(getContext());
        setTransactionsDAO(perTransactionDAO);

        AccountDAO perAccountDAO = new PersistAccountDAO(getContext());
        setAccountsDAO(perAccountDAO);

        //getAccountsDAO().addAccount();


    }
}
