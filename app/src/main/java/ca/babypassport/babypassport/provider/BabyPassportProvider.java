package ca.babypassport.babypassport.provider;

import ca.babypassport.babypassport.contract.BabyPassportContract;
import ca.babypassport.babypassport.contract.BabyPassportContract.BabyInfo;
import ca.babypassport.babypassport.contract.BabyPassportContract.Contact;
import ca.babypassport.babypassport.contract.BabyPassportContract.ContactType;
import ca.babypassport.babypassport.contract.BabyPassportContract.FeedingType;
import ca.babypassport.babypassport.contract.BabyPassportContract.Log;
import ca.babypassport.babypassport.contract.BabyPassportContract.LogType;
import ca.babypassport.babypassport.contract.BabyPassportContract.WeightType;
import ca.babypassport.babypassport.helpers.BabyPassportDbHelper;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

public class BabyPassportProvider extends ContentProvider{

	
	
	private BabyPassportDbHelper dbHelper;
	
	
	private static final UriMatcher sUriMatcher= buildUriMatcher();
	
	private static final int LOGS = 100;
	private static final int LOGS_ID = 101;
	
	private static final int LOG_TYPES = 200;
	private static final int LOG_TYPES_ID = 201;
	
	private static final int CONTACTS = 300;
	private static final int CONTACTS_ID = 301;
	private static final int CONTACT_TYPES = 302;
	private static final int CONTACT_TYPES_ID = 303;
	
	private static final int FEEDING_TYPES = 400;
	private static final int FEEDING_TYPES_ID = 401;
	
	private static final int WEIGHT_TYPES = 500;
	private static final int WEIGHT_TYPES_ID = 501;
	
	private static final int BABY_INFOS = 600;
	private static final int BABY_INFOS_ID = 601;
	
	private static String appendIdToPath(String path){
		return path +"/*";
	}
	private static UriMatcher buildUriMatcher(){
		final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
		final String authority = BabyPassportContract.AUTHORITY;
		
		matcher.addURI(authority, BabyPassportContract.PATH_LOGS, LOGS );
		matcher.addURI(authority, appendIdToPath(BabyPassportContract.PATH_LOGS), LOGS_ID);
		
		matcher.addURI(authority, BabyPassportContract.PATH_LOG_TYPES, LOG_TYPES);
		matcher.addURI(authority, appendIdToPath(BabyPassportContract.PATH_LOG_TYPES), LOG_TYPES_ID);
		//matcher.addURI(authority, "persons/*/trips", PERSONS_ID_TRIPS);
		
		matcher.addURI(authority, BabyPassportContract.PATH_CONTACTS, CONTACTS);
		matcher.addURI(authority, appendIdToPath(BabyPassportContract.PATH_CONTACTS), CONTACTS_ID);
		
		matcher.addURI(authority, BabyPassportContract.PATH_CONTACT_TYPES, CONTACT_TYPES);
		matcher.addURI(authority, appendIdToPath(BabyPassportContract.PATH_CONTACT_TYPES), CONTACT_TYPES_ID);
		
		matcher.addURI(authority, BabyPassportContract.PATH_FEEDING_TYPES, FEEDING_TYPES);
		matcher.addURI(authority, appendIdToPath(BabyPassportContract.PATH_FEEDING_TYPES), FEEDING_TYPES_ID);
		
		matcher.addURI(authority, BabyPassportContract.PATH_WEIGHT_TYPES, WEIGHT_TYPES);
		matcher.addURI(authority, appendIdToPath(BabyPassportContract.PATH_WEIGHT_TYPES), WEIGHT_TYPES_ID);
		
		matcher.addURI(authority, BabyPassportContract.PATH_BABY_INFO, BABY_INFOS);
		matcher.addURI(authority, appendIdToPath(BabyPassportContract.PATH_BABY_INFO), BABY_INFOS_ID);
		
		
		return matcher;
	}
	
	
	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		int id;
		final SQLiteDatabase db = dbHelper.getWritableDatabase();
		final int match = sUriMatcher.match(uri);
		switch(match){
		case LOGS: 
			id = db.delete(BabyPassportContract.Log.TABLE_NAME, selection, selectionArgs);
			getContext().getContentResolver().notifyChange(uri, null);
			return id;
		case BABY_INFOS: 
			id = db.delete(BabyPassportContract.BabyInfo.TABLE_NAME, selection, selectionArgs);
			getContext().getContentResolver().notifyChange(uri, null);
			return id;
		case CONTACTS :
			id = db.delete(BabyPassportContract.Contact.TABLE_NAME, selection, selectionArgs);
			getContext().getContentResolver().notifyChange(uri, null);
			return id;
		default : 
				throw new UnsupportedOperationException("Unkown uri: " + uri);
		}
	}

	@Override
	public String getType(Uri uri) {
		final int match = sUriMatcher.match(uri);
		switch(match){
		case LOGS:
			return BabyPassportContract.Log.CONTENT_TYPE;
		case LOGS_ID:
			return BabyPassportContract.Log.CONTENT_ITEM_TYPE;
		case LOG_TYPES:
			return BabyPassportContract.LogType.CONTENT_TYPE;
		case LOG_TYPES_ID:
			return BabyPassportContract.LogType.CONTENT_ITEM_TYPE;
		case CONTACTS:
			return BabyPassportContract.Contact.CONTENT_TYPE;
		case CONTACTS_ID:
			return BabyPassportContract.Contact.CONTENT_ITEM_TYPE;
		case CONTACT_TYPES:
			return BabyPassportContract.ContactType.CONTENT_TYPE;
		case CONTACT_TYPES_ID:
			return BabyPassportContract.ContactType.CONTENT_ITEM_TYPE;
		case FEEDING_TYPES:
			return BabyPassportContract.FeedingType.CONTENT_TYPE;
		case FEEDING_TYPES_ID:
			return BabyPassportContract.FeedingType.CONTENT_ITEM_TYPE;
		case WEIGHT_TYPES:
			return BabyPassportContract.WeightType.CONTENT_TYPE;
		case WEIGHT_TYPES_ID:
			return BabyPassportContract.WeightType.CONTENT_ITEM_TYPE;
		case BABY_INFOS:
			return BabyPassportContract.BabyInfo.CONTENT_TYPE;
		case BABY_INFOS_ID:
			return BabyPassportContract.BabyInfo.CONTENT_ITEM_TYPE;
		default: throw new UnsupportedOperationException("Unknown uri: " + uri);
		
		}
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		final SQLiteDatabase db = dbHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        long id;
        switch(match){
		case LOGS:
			id = db.insertOrThrow(Log.TABLE_NAME, null, values);
			getContext().getContentResolver().notifyChange(uri, null);
			return Log.buildLogUri(Long.toString(id));
			
		case CONTACTS:
			id = db.insertOrThrow(Contact.TABLE_NAME,null,values);
			getContext().getContentResolver().notifyChange(uri, null);
			return Contact.buildContactsUri(Long.toString(id));
		case BABY_INFOS:
			id = db.insertOrThrow(BabyInfo.TABLE_NAME, null, values);
			getContext().getContentResolver().notifyChange(uri, null);
			return BabyInfo.buildBabyInfoUri(Long.toString(id));
		default:
			throw new IllegalArgumentException("Unkonwn URI " + uri);
		
		}
	}

	@Override
	public boolean onCreate() {
		dbHelper = new BabyPassportDbHelper(getContext());
		
		return true;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection, String[]  selectionArgs,
			String sortOrder) {
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		
		final int match = sUriMatcher.match(uri);
		switch(match){
		case LOGS:
			qb.setTables(Log.TABLE_NAME);
			break;
		case LOG_TYPES:
			qb.setTables(LogType.TABLE_NAME);
			break;
		case CONTACTS:
			qb.setTables(Contact.TABLE_NAME);
			break;
		case CONTACT_TYPES:
			qb.setTables(ContactType.TABLE_NAME);
			break;
		case WEIGHT_TYPES:
			qb.setTables(WeightType.TABLE_NAME);
			break;
		case FEEDING_TYPES:
			qb.setTables(FeedingType.TABLE_NAME);
			break;
		case BABY_INFOS:
			qb.setTables(BabyInfo.TABLE_NAME);
			break;
		default:
			throw new IllegalArgumentException("Unkonwn URI " + uri);
		
		}
		
		Cursor c = qb.query(db, projection, selection, selectionArgs, null, null, sortOrder);
		// cursor is notified at this Uri if data changes after it is returned
		c.setNotificationUri(getContext().getContentResolver(), uri);
		return c;
	}

	@Override
	public int update(Uri uri, ContentValues values, String whereClause, String[] whereArgs) {
		final SQLiteDatabase db = dbHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rows;
        switch(match){
		/*case LOGS:
			rows = db.update(Log.TABLE_NAME, values, whereClause, whereArgs)
			getContext().getContentResolver().notifyChange(uri, null);
			return rows;
			*/
		case CONTACTS:
			rows= db.update(Contact.TABLE_NAME, values, whereClause, whereArgs);
			getContext().getContentResolver().notifyChange(uri, null);
			return rows;
		case BABY_INFOS:
			rows = db.update(BabyInfo.TABLE_NAME, values, whereClause, whereArgs);
			getContext().getContentResolver().notifyChange(uri, null);
			return rows;
		default:
			throw new IllegalArgumentException("Unkonwn URI " + uri);
        }
		
        
	}

}
