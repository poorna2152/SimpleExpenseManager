/*
 * Copyright 2015 Department of Computer Science and Engineering, University of Moratuwa.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *                  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.InvalidAccountException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.IAccountSchema;

/**
 * This is an Persistent storage implementation of the AccountDAO interface. 
 */
public class PersistentAccountDAO implements AccountDAO,IAccountSchema {
    private SQLiteDatabase database;
    private Cursor cursor;
    private ContentValues  values;

    public PersistentAccountDAO(SQLiteDatabase db) {
        this.database = db;
    }

    @Override
    public List<String> getAccountNumbersList() {
        List<String> accountNoList = new ArrayList<>();
        int index;
        cursor = database.query(ACCOUNT_TABLE,new String[]{ACCOUNT_NO},null,null,null,null,null);
        if (cursor != null) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                if (cursor.getColumnIndex(ACCOUNT_NO) != -1) {
                    index = cursor.getColumnIndexOrThrow(ACCOUNT_NO);
                    accountNoList.add(cursor.getString(index));
                }
                cursor.moveToNext();
            }
            cursor.close();
        }
        return accountNoList;
    }

    @Override
    public List<Account> getAccountsList() {
        List<Account> list = new ArrayList<>();
        cursor = database.query(ACCOUNT_TABLE,null,null,null,null,null,null);
        if (cursor != null) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                Account account = cursorToAccount(cursor);
                list.add(account);
                cursor.moveToNext();
            }
            cursor.close();
        }
        return list;
    }

    @Override
    public Account getAccount(String accountNo) throws InvalidAccountException {
        cursor = database.query(ACCOUNT_TABLE,null,ACCOUNT_NO,new String[]{accountNo},null,null,null);
        if(cursor != null) {
            cursor.moveToFirst();
            return cursorToAccount(cursor);
        }

        String msg = "Account " + accountNo + " is invalid.";
        throw new InvalidAccountException(msg);
    }

    @Override
    public void addAccount(Account account) {
        values = new ContentValues();
        try{
            values.put(ACCOUNT_HOLDER,account.getAccountHolderName());
            values.put(BANK_NAME,account.getBankName());
            values.put(BALANCE,account.getBalance());
            values.put(ACCOUNT_NO,account.getAccountNo());
            database.insert(ACCOUNT_TABLE,null,values);
        }
         catch (SQLiteConstraintException ex){
             Log.w("Database", ex.getMessage());
        }
    }

    @Override
    public void removeAccount(String accountNo) throws InvalidAccountException {
        int result = database.delete(ACCOUNT_TABLE, ACCOUNT_NO, new String[]{accountNo});
        if (result < 1) {
            String msg = "Account " + accountNo + " is invalid.";
            throw new InvalidAccountException(msg);
        }
    }

    @Override
    public void updateBalance(String accountNo, ExpenseType expenseType, double amount) throws InvalidAccountException {
        values = new ContentValues();
        Account existing = getAccount(accountNo);
        switch (expenseType) {
            case EXPENSE:
                existing.setBalance(existing.getBalance() - amount);
                break;
            case INCOME:
                existing.setBalance(existing.getBalance() + amount);
                break;
        }
        values.put(BALANCE,existing.getBalance());
        int result = database.update(ACCOUNT_TABLE,values,ACCOUNT_NO,new String[]{accountNo});

    }
    protected Account cursorToAccount(Cursor cursor) {
        Account account = null;
        int index;
        String accountNo = "";
        String bankName = "";
        String accountHolderName = "";
        double balance = 0.0;

        if (cursor != null) {
            if (cursor.getColumnIndex(ACCOUNT_NO) != -1) {
                index = cursor.getColumnIndexOrThrow(ACCOUNT_NO);
                accountNo = cursor.getString(index);
            }
            if (cursor.getColumnIndex(BANK_NAME) != -1) {
                index = cursor.getColumnIndexOrThrow(
                        BANK_NAME);
                bankName = cursor.getString(index);
            }
            if (cursor.getColumnIndex(ACCOUNT_HOLDER) != -1) {
                index = cursor.getColumnIndexOrThrow(
                        ACCOUNT_HOLDER);
                accountHolderName = cursor.getString(index);
            }
            if (cursor.getColumnIndex(BALANCE) != -1) {
                index = cursor.getColumnIndexOrThrow(BALANCE);
                balance = cursor.getFloat(index);
            }
            account = new Account(accountNo,bankName,accountHolderName,balance);
        }
        return account;
    }
}
