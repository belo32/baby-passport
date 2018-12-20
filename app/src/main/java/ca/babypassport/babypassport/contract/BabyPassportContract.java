package ca.babypassport.babypassport.contract;

import android.net.Uri;
import android.provider.BaseColumns;

public final class BabyPassportContract {


    public static final String AUTHORITY = "ca.babypassport.babypassport.provider.babypassportprovider";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    public static final String PATH_LOGS = "logs";
    public static final String PATH_LOG_TYPES = "logTypes";
    public static final String PATH_CONTACTS = "contacts";
    public static final String PATH_CONTACT_TYPES = "contactTypes";
    public static final String PATH_FEEDING_TYPES = "feedingTypes";
    public static final String PATH_WEIGHT_TYPES = "weightTypes";
    public static final String PATH_BABY_INFO = "babyInfo";

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "BabyPassport.db";

    private static final String TEXT_TYPE = " TEXT";
    private static final String INTEGER_TYPE = " INTEGER";
    private static final String BLOB_TYPE = " BLOB";
    private static final String COMMA_SEP = ",";

    public static final String[] FEEDING_TYPES = {"Breastfeeding", "Bottlefeeding"};
    public static final String[] CONTACT_TYPES = {"Family Doctor", "Paediatrician", "Midwife", "Other"};
    public static final String[] WEIGHT_TYPES = {"kg", "lbs"};
    public static final String[] LOG_TYPES = {"Feeding", "Wet Diaper", "Bowel Movement"};

    public BabyPassportContract() {
    }

    private static String foreignKey(String tableName, String rowName) {
        return " INTEGER REFERENCES " + tableName + "(" + rowName + ")";
    }

    public static abstract class Log implements BaseColumns {
        public static final String TABLE_NAME = "log";
        public static final String COLUMN_NAME_TYPE = "type";
        public static final String COLUMN_NAME_DATETIME = "datetime";
        public static final String COLUMN_NAME_DATE = "date";
        public static final String COLUMN_NAME_TIME = "time";
        public static final String COLUMN_NAME_BABY_ID = "babyid";

        public static final String SQL_INIT_LOGS =
                "CREATE TABLE " + Log.TABLE_NAME + " (" +
                        Log._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                        Log.COLUMN_NAME_DATETIME + INTEGER_TYPE + COMMA_SEP +
                        Log.COLUMN_NAME_DATE + TEXT_TYPE + COMMA_SEP +
                        Log.COLUMN_NAME_TIME + TEXT_TYPE + COMMA_SEP +
                        Log.COLUMN_NAME_TYPE + TEXT_TYPE + COMMA_SEP +
                        Log.COLUMN_NAME_BABY_ID + foreignKey(BabyInfo.TABLE_NAME, BabyInfo._ID) +
                        " )";

        public static final String SQL_DELETE_LOGS =
                "DELETE TABLE IF EXISTS " + Log.TABLE_NAME;

        public static final String DEFAULT_SORT_ORDER = COLUMN_NAME_DATETIME + " " + "DESC";

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_LOGS).build();

        public static Uri buildLogUri(String id) {
            return CONTENT_URI.buildUpon().appendPath(id).build();
        }

        public static final String CONTENT_TYPE =
                "vnd.android.cursor.dir/vnd.babypassport.log";
        public static final String CONTENT_ITEM_TYPE =
                "vnd.android.cursor.item/vnd.babypassport.log";
    }

    public static abstract class LogType implements BaseColumns {
        public static final String TABLE_NAME = "logtype";
        public static final String COLUMN_NAME_TYPE = "type";

        public static final String SQL_INIT_LOG_TYPES =
                "CREATE TABLE " + LogType.TABLE_NAME + " (" +
                        LogType._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                        LogType.COLUMN_NAME_TYPE + TEXT_TYPE +
                        " )";
        public static final String SQL_DELETE_LOG_TYPES =
                "DELETE TABLE IF EXISTS " + LogType.TABLE_NAME;

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_LOG_TYPES).build();

        public static Uri buildLogTypeUri(String id) {
            return CONTENT_URI.buildUpon().appendPath(id).build();
        }

        public static final String CONTENT_TYPE =
                "vnd.android.cursor.dir/vnd.babypassport.logtype";
        public static final String CONTENT_ITEM_TYPE =
                "vnd.android.cursor.item/vnd.babypassport.logtype";
    }

    public static abstract class Contact implements BaseColumns {
        public static final String TABLE_NAME = "contact";
        public static final String COLUMN_NAME_CONTACT_NAME = "name";
        public static final String COLUMN_NAME_PHONE = "phone";
        public static final String COLUMN_NAME_ADDRESS = "address";
        public static final String COLUMN_NAME_TYPE = "contacttype";
        public static final String COLUMN_NAME_BABY_ID = "babyid";

        public static final String SQL_INIT_CONTACT =
                "CREATE TABLE " + Contact.TABLE_NAME + " (" +
                        Contact._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        Contact.COLUMN_NAME_CONTACT_NAME + TEXT_TYPE + COMMA_SEP +
                        Contact.COLUMN_NAME_PHONE + TEXT_TYPE + COMMA_SEP +
                        Contact.COLUMN_NAME_ADDRESS + TEXT_TYPE + COMMA_SEP +
                        Contact.COLUMN_NAME_TYPE + TEXT_TYPE + COMMA_SEP +
                        Contact.COLUMN_NAME_BABY_ID + foreignKey(BabyInfo.TABLE_NAME, BabyInfo._ID) +
                        " )";

        public static final String SQL_DELETE_CONTACTS =
                "DELETE TABLE IF EXISTS " + Contact.TABLE_NAME;

        public static final String DEFAULT_SORT_ORDER = COLUMN_NAME_CONTACT_NAME + " " + "ASC";
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_CONTACTS).build();

        public static Uri buildContactsUri(String id) {
            return CONTENT_URI.buildUpon().appendPath(id).build();
        }

        public static final String CONTENT_TYPE =
                "vnd.android.cursor.dir/vnd.babypassport.contact";
        public static final String CONTENT_ITEM_TYPE =
                "vnd.android.cursor.item/vnd.babypassport.contact";
    }

    public static abstract class ContactType implements BaseColumns {
        public static final String TABLE_NAME = "contacttype";
        public static final String COLUMN_NAME_TYPE = "type";

        public static final String SQL_INIT_CONTACT_TYPE =
                "CREATE TABLE " + ContactType.TABLE_NAME + " ("
                        + BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                        + ContactType.COLUMN_NAME_TYPE + TEXT_TYPE +
                        " )";

        public static final String SQL_DELETE_CONTACT_TYPES =
                "DELETE TABLE IF EXISTS " + ContactType.TABLE_NAME;

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_CONTACT_TYPES).build();

        public static Uri buildContactTypesUri(String id) {
            return CONTENT_URI.buildUpon().appendPath(id).build();
        }

        public static final String CONTENT_TYPE =
                "vnd.android.cursor.dir/vnd.babypassport.contacttype";
        public static final String CONTENT_ITEM_TYPE =
                "vnd.android.cursor.item/vnd.babypassport.contacttype";
    }

    public static abstract class FeedingType implements BaseColumns {
        public static final String TABLE_NAME = "feedingtype";
        public static final String COLUMN_NAME_TYPE = "type";

        public static final String SQL_INIT_FEEDING_TYPE =
                "CREATE TABLE " + FeedingType.TABLE_NAME + " ("
                        + BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                        + FeedingType.COLUMN_NAME_TYPE + TEXT_TYPE +
                        " )";
        public static final String SQL_DELETE_FEEDING_TYPES =
                "DROP TABLE IF EXISTS " + FeedingType.TABLE_NAME;

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_FEEDING_TYPES).build();

        public static Uri buildFeedingTypesUri(String id) {
            return CONTENT_URI.buildUpon().appendPath(id).build();
        }

        public static final String CONTENT_TYPE =
                "vnd.android.cursor.dir/vnd.babypassport.feedingtype";
        public static final String CONTENT_ITEM_TYPE =
                "vnd.android.cursor.item/vnd.babypassport.feedingtype";
    }

    public static abstract class WeightType implements BaseColumns {
        public static final String TABLE_NAME = "weighttype";
        public static final String COLUMN_NAME_TYPE = "type";

        public static final String SQL_INIT_WEIGHT_TYPE =
                "CREATE TABLE " + WeightType.TABLE_NAME + " ("
                        + BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                        + WeightType.COLUMN_NAME_TYPE + TEXT_TYPE +
                        " )";

        public static final String SQL_DELETE_WEIGHT_TYPES =
                "DROP TABLE IF EXISTS " + WeightType.TABLE_NAME;

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_WEIGHT_TYPES).build();

        public static Uri buildWeightTypesUri(String id) {
            return CONTENT_URI.buildUpon().appendPath(id).build();
        }

        public static final String CONTENT_TYPE =
                "vnd.android.cursor.dir/vnd.babypassport.weighttype";
        public static final String CONTENT_ITEM_TYPE =
                "vnd.android.cursor.item/vnd.babypassport.weighttype";
    }

    public static abstract class BabyInfo implements BaseColumns {
        public static final String TABLE_NAME = "babyinfo";
        //public static final String COLUMN_NAME_BABY_ID 			= "babyid";
        public static final String COLUMN_NAME_BABY_NAME = "name";
        public static final String COLUMN_NAME_DOB = "dob";
        public static final String COLUMN_NAME_BIRTH_WEIGHT = "birthweight";
        public static final String COLUMN_NAME_DISCHARGE_WEIGHT = "dischargeweight";
        public static final String COLUMN_NAME_DUE_DATE = "duedate";
        public static final String COLUMN_NAME_DISCHARGE_DATE = "dischargedate";
        public static final String COLUMN_NAME_FEEDING_TYPE = "feedingtype";
        public static final String COLUMN_NAME_WEIGHT_TYPE = "weighttype";
        public static final String COLUMN_NAME_SEX = "sex";
        public static final String COLUMN_NAME_IMAGE = "image";


        public static final String SQL_INIT_BABIES =
                "CREATE TABLE " + BabyInfo.TABLE_NAME + " (" +
                        BabyInfo._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                        BabyInfo.COLUMN_NAME_BABY_NAME + TEXT_TYPE + COMMA_SEP +
                        BabyInfo.COLUMN_NAME_DOB + INTEGER_TYPE + COMMA_SEP +
                        BabyInfo.COLUMN_NAME_BIRTH_WEIGHT + INTEGER_TYPE + COMMA_SEP +
                        BabyInfo.COLUMN_NAME_DISCHARGE_WEIGHT + INTEGER_TYPE + COMMA_SEP +
                        BabyInfo.COLUMN_NAME_DUE_DATE + INTEGER_TYPE + COMMA_SEP +
                        BabyInfo.COLUMN_NAME_DISCHARGE_DATE + INTEGER_TYPE + COMMA_SEP +
                        BabyInfo.COLUMN_NAME_SEX + INTEGER_TYPE + COMMA_SEP +
                        BabyInfo.COLUMN_NAME_FEEDING_TYPE + TEXT_TYPE + COMMA_SEP +
                        BabyInfo.COLUMN_NAME_WEIGHT_TYPE + TEXT_TYPE + COMMA_SEP +
                        BabyInfo.COLUMN_NAME_IMAGE + BLOB_TYPE +
                        " )";

        public static final String DEFAULT_SORT_ORDER = COLUMN_NAME_BABY_NAME + " " + "ASC";
        public static final String SQL_DELETE_BABIES =
                "DROP TABLE IF EXISTS " + BabyInfo.TABLE_NAME;

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_BABY_INFO).build();

        public static Uri buildBabyInfoUri(String id) {
            return CONTENT_URI.buildUpon().appendPath(id).build();
        }

        public static final String CONTENT_TYPE =
                "vnd.android.cursor.dir/vnd.babypassport.babyinfo";
        public static final String CONTENT_ITEM_TYPE =
                "vnd.android.cursor.item/vnd.babypassport.babyinfo";
    }
}
