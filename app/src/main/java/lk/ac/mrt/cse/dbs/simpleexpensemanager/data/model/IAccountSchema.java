package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model;

public interface IAccountSchema {
    String ACCOUNT_TABLE = "account";
    String ACCOUNT_NO = "accountNo";
    String BANK_NAME = "bankName";
    String ACCOUNT_HOLDER = "accountHolderName";
    String BALANCE = "balance";
    String ACCOUNT_TABLE_CREATE = "CREATE TABLE IF NOT EXISTS "
            + ACCOUNT_TABLE
            + " ("
            + ACCOUNT_NO
            + " TEXT PRIMARY KEY, "
            + BANK_NAME
            + " TEXT NOT NULL, "
            + ACCOUNT_HOLDER
            + " TEXT,"
            + BALANCE
            + " DOUBLE "
            + ")";

    String[] ACCOUNT_COLUMNS = new String[] { ACCOUNT_TABLE,
            ACCOUNT_NO, BANK_NAME, ACCOUNT_HOLDER, BALANCE };
}

