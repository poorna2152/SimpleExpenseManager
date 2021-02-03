package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model;

import java.util.Date;

public interface ITransactionSchema {
    String TRANSACTION_TABLE = "`transaction`";
    String DATE = "transactionDate";
    String ACCOUNT_NO = "accountNo";
    String EXPENSE_TYPE = "expenseType";
    String AMOUNT = "amount";
    String TRANSACTION_TABLE_CREATE = "CREATE TABLE IF NOT EXISTS "
            + TRANSACTION_TABLE
            + " ("
            + DATE
            + " TEXT, "
            + ACCOUNT_NO

            + " TEXT NOT NULL, "
            + EXPENSE_TYPE
            + " TEXT,"
            + AMOUNT
            + " DOUBLE,"
            + "FOREIGN KEY (accountNo) REFERENCES account(accountNo)"

            + ")";

    String[] TRANSACTION_COLUMNS = new String[] { TRANSACTION_TABLE,
            DATE,ACCOUNT_NO, EXPENSE_TYPE, AMOUNT };
}
