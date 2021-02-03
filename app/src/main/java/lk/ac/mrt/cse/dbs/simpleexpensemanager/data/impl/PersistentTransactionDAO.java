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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ITransactionSchema;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;

/**
 * This is an Persistent implementation of TransactionDAO interface.
 */
public class PersistentTransactionDAO implements TransactionDAO, ITransactionSchema {
    private SQLiteDatabase database;
    private Cursor cursor;
    private ContentValues values;
    private  SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

    public PersistentTransactionDAO(SQLiteDatabase db) {
        database = db;
    }

    @Override
    public void logTransaction(Date date, String accountNo, ExpenseType expenseType, double amount) {
        values = new ContentValues();
        List<Transaction> list = new ArrayList<>();
        try{
            values.put(ACCOUNT_NO,accountNo);
            values.put(DATE, dateFormat.format(date));
            values.put(EXPENSE_TYPE, expenseType.toString());
            values.put(AMOUNT,amount);
            database.insert(TRANSACTION_TABLE,null,values);
            list = getAllTransactionLogs();
         }
        catch (SQLiteConstraintException | ParseException ex) {
            Log.w("Database", ex.getMessage());
        }
    }

    @Override
    public List<Transaction> getAllTransactionLogs() throws ParseException {
        List<Transaction> transactionList = new ArrayList<>();
        cursor = database.query(TRANSACTION_TABLE,null,null,null,null,null,null);
        if (cursor != null) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                Transaction transaction = cursorToTransaction(cursor);
                transactionList.add(transaction);
                cursor.moveToNext();
            }
            cursor.close();
        }
        return transactionList;
    }

    @Override
    public List<Transaction> getPaginatedTransactionLogs(int limit) throws ParseException {
        List<Transaction> transactionList = new ArrayList<>();
        cursor = database.query(TRANSACTION_TABLE,null,null,null,null,null,null,String.valueOf(limit));
        if (cursor != null) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                Transaction transaction = cursorToTransaction(cursor);
                transactionList.add(transaction);
                cursor.moveToNext();
            }
            cursor.close();
        }
        return transactionList;
    }

    protected Transaction cursorToTransaction(Cursor cursor) throws ParseException {
        Transaction transaction = null;
        int index;
        String accountNo = null;
        ExpenseType expenseType = null;
        double amount = 0;
        Date date = null;

        if (cursor != null) {
            if (cursor.getColumnIndex(ACCOUNT_NO) != -1) {
                index = cursor.getColumnIndexOrThrow(ACCOUNT_NO);
                accountNo = cursor.getString(index);
            }
            if (cursor.getColumnIndex(EXPENSE_TYPE) != -1) {
                index = cursor.getColumnIndexOrThrow(
                        EXPENSE_TYPE);
                expenseType = ExpenseType.valueOf(cursor.getString(index));
            }
            if (cursor.getColumnIndex(AMOUNT) != -1) {
                index = cursor.getColumnIndexOrThrow(
                        AMOUNT);
                amount = cursor.getDouble(index);
            }
            if (cursor.getColumnIndex(DATE) != -1) {
                index = cursor.getColumnIndexOrThrow(DATE);
                date = dateFormat.parse(cursor.getString(index));
            }
            transaction = new Transaction(date,accountNo,expenseType,amount);
        }
        return transaction;
    }

}
