package ca.babypassport.babypassport.helpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import ca.babypassport.babypassport.contract.BabyPassportContract;

public class BabyPassportDbHelper extends SQLiteOpenHelper {


    public BabyPassportDbHelper(Context context) {
        super(context, BabyPassportContract.DATABASE_NAME, null, BabyPassportContract.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(BabyPassportContract.ContactType.SQL_INIT_CONTACT_TYPE);
        db.execSQL(BabyPassportContract.LogType.SQL_INIT_LOG_TYPES);
        db.execSQL(BabyPassportContract.WeightType.SQL_INIT_WEIGHT_TYPE);
        db.execSQL(BabyPassportContract.FeedingType.SQL_INIT_FEEDING_TYPE);
        db.execSQL(BabyPassportContract.BabyInfo.SQL_INIT_BABIES);
        db.execSQL(BabyPassportContract.Contact.SQL_INIT_CONTACT);
        db.execSQL(BabyPassportContract.Log.SQL_INIT_LOGS);
        insertDefaultValues(db);


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(BabyPassportContract.BabyInfo.SQL_DELETE_BABIES);
        db.execSQL(BabyPassportContract.Contact.SQL_DELETE_CONTACTS);
        db.execSQL(BabyPassportContract.ContactType.SQL_DELETE_CONTACT_TYPES);
        db.execSQL(BabyPassportContract.FeedingType.SQL_DELETE_FEEDING_TYPES);
        db.execSQL(BabyPassportContract.Log.SQL_DELETE_LOGS);
        db.execSQL(BabyPassportContract.LogType.SQL_DELETE_LOG_TYPES);
        db.execSQL(BabyPassportContract.WeightType.SQL_DELETE_WEIGHT_TYPES);
        onCreate(db);
    }


    private void insertDefaultValues(SQLiteDatabase db) {
        insertFeedingTypes(db);
        insertWeightTypes(db);
        insertLogTypes(db);
        insertContactTypes(db);
    }

    private void insertFeedingTypes(SQLiteDatabase db) {
        for (int i = 0; i < BabyPassportContract.FEEDING_TYPES.length; i++) {
            insertFeedingType(db, BabyPassportContract.FEEDING_TYPES[i]);
        }
    }

    private void insertWeightTypes(SQLiteDatabase db) {
        for (int i = 0; i < BabyPassportContract.WEIGHT_TYPES.length; i++) {
            insertWeightType(db, BabyPassportContract.WEIGHT_TYPES[i]);
        }
    }

    private void insertLogTypes(SQLiteDatabase db) {
        for (int i = 0; i < BabyPassportContract.LOG_TYPES.length; i++) {
            insertLogType(db, BabyPassportContract.LOG_TYPES[i]);
        }
    }

    private void insertContactTypes(SQLiteDatabase db) {
        for (int i = 0; i < BabyPassportContract.CONTACT_TYPES.length; i++) {
            insertContactType(db, BabyPassportContract.CONTACT_TYPES[i]);
        }
    }

    private void insertFeedingType(SQLiteDatabase db, String feedingType) {
        insertType(db, BabyPassportContract.FeedingType.TABLE_NAME, BabyPassportContract.FeedingType.COLUMN_NAME_TYPE, feedingType);
    }

    private void insertWeightType(SQLiteDatabase db, String weightType) {
        insertType(db, BabyPassportContract.WeightType.TABLE_NAME, BabyPassportContract.WeightType.COLUMN_NAME_TYPE, weightType);
    }

    private void insertLogType(SQLiteDatabase db, String logType) {
        insertType(db, BabyPassportContract.LogType.TABLE_NAME, BabyPassportContract.LogType.COLUMN_NAME_TYPE, logType);
    }

    private void insertContactType(SQLiteDatabase db, String contactType) {
        insertType(db, BabyPassportContract.ContactType.TABLE_NAME, BabyPassportContract.ContactType.COLUMN_NAME_TYPE, contactType);
    }

    private void insertType(SQLiteDatabase db, String tableName, String columnName, String type) {
        final ContentValues values = new ContentValues();
        values.put(columnName, type);
        db.insert(tableName, null, values);
    }

}
