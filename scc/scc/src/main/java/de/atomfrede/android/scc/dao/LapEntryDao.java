package de.atomfrede.android.scc.dao;

import java.util.List;
import java.util.ArrayList;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.DaoConfig;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.SqlUtils;
import de.greenrobot.dao.Query;
import de.greenrobot.dao.QueryBuilder;

import de.atomfrede.android.scc.dao.LapEntry;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table LAP_ENTRY.
*/
public class LapEntryDao extends AbstractDao<LapEntry, Void> {

    public static final String TABLENAME = "LAP_ENTRY";

    /**
     * Properties of entity LapEntry.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property LapId = new Property(0, long.class, "lapId", false, "LAP_ID");
    };

    private DaoSession daoSession;

    private Query<LapEntry> lap_LapEntryListQuery;

    public LapEntryDao(DaoConfig config) {
        super(config);
    }
    
    public LapEntryDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
        this.daoSession = daoSession;
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "'LAP_ENTRY' (" + //
                "'LAP_ID' INTEGER NOT NULL );"); // 0: lapId
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "'LAP_ENTRY'";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, LapEntry entity) {
        stmt.clearBindings();
        stmt.bindLong(1, entity.getLapId());
    }

    @Override
    protected void attachEntity(LapEntry entity) {
        super.attachEntity(entity);
        entity.__setDaoSession(daoSession);
    }

    /** @inheritdoc */
    @Override
    public Void readKey(Cursor cursor, int offset) {
        return null;
    }    

    /** @inheritdoc */
    @Override
    public LapEntry readEntity(Cursor cursor, int offset) {
        LapEntry entity = new LapEntry( //
            cursor.getLong(offset + 0) // lapId
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, LapEntry entity, int offset) {
        entity.setLapId(cursor.getLong(offset + 0));
     }
    
    /** @inheritdoc */
    @Override
    protected Void updateKeyAfterInsert(LapEntry entity, long rowId) {
        // Unsupported or missing PK type
        return null;
    }
    
    /** @inheritdoc */
    @Override
    public Void getKey(LapEntry entity) {
        return null;
    }

    /** @inheritdoc */
    @Override    
    protected boolean isEntityUpdateable() {
        return true;
    }
    
    /** Internal query to resolve the "lapEntryList" to-many relationship of Lap. */
    public synchronized List<LapEntry> _queryLap_LapEntryList(long lapId) {
        if (lap_LapEntryListQuery == null) {
            QueryBuilder<LapEntry> queryBuilder = queryBuilder();
            queryBuilder.where(Properties.LapId.eq(lapId));
            lap_LapEntryListQuery = queryBuilder.build();
        } else {
            lap_LapEntryListQuery.setParameter(0, lapId);
        }
        return lap_LapEntryListQuery.list();
    }

    private String selectDeep;

    protected String getSelectDeep() {
        if (selectDeep == null) {
            StringBuilder builder = new StringBuilder("SELECT ");
            SqlUtils.appendColumns(builder, "T", getAllColumns());
            builder.append(',');
            SqlUtils.appendColumns(builder, "T0", daoSession.getLapDao().getAllColumns());
            builder.append(" FROM LAP_ENTRY T");
            builder.append(" LEFT JOIN LAP T0 ON T.'LAP_ID'=T0.'_id'");
            builder.append(' ');
            selectDeep = builder.toString();
        }
        return selectDeep;
    }
    
    protected LapEntry loadCurrentDeep(Cursor cursor, boolean lock) {
        LapEntry entity = loadCurrent(cursor, 0, lock);
        int offset = getAllColumns().length;

        Lap lap = loadCurrentOther(daoSession.getLapDao(), cursor, offset);
         if(lap != null) {
            entity.setLap(lap);
        }

        return entity;    
    }

    public LapEntry loadDeep(Long key) {
        assertSinglePk();
        if (key == null) {
            return null;
        }

        StringBuilder builder = new StringBuilder(getSelectDeep());
        builder.append("WHERE ");
        SqlUtils.appendColumnsEqValue(builder, "T", getPkColumns());
        String sql = builder.toString();
        
        String[] keyArray = new String[] { key.toString() };
        Cursor cursor = db.rawQuery(sql, keyArray);
        
        try {
            boolean available = cursor.moveToFirst();
            if (!available) {
                return null;
            } else if (!cursor.isLast()) {
                throw new IllegalStateException("Expected unique result, but count was " + cursor.getCount());
            }
            return loadCurrentDeep(cursor, true);
        } finally {
            cursor.close();
        }
    }
    
    /** Reads all available rows from the given cursor and returns a list of new ImageTO objects. */
    public List<LapEntry> loadAllDeepFromCursor(Cursor cursor) {
        int count = cursor.getCount();
        List<LapEntry> list = new ArrayList<LapEntry>(count);
        
        if (cursor.moveToFirst()) {
            if (identityScope != null) {
                identityScope.lock();
                identityScope.reserveRoom(count);
            }
            try {
                do {
                    list.add(loadCurrentDeep(cursor, false));
                } while (cursor.moveToNext());
            } finally {
                if (identityScope != null) {
                    identityScope.unlock();
                }
            }
        }
        return list;
    }
    
    protected List<LapEntry> loadDeepAllAndCloseCursor(Cursor cursor) {
        try {
            return loadAllDeepFromCursor(cursor);
        } finally {
            cursor.close();
        }
    }
    

    /** A raw-style query where you can pass any WHERE clause and arguments. */
    public List<LapEntry> queryDeep(String where, String... selectionArg) {
        Cursor cursor = db.rawQuery(getSelectDeep() + where, selectionArg);
        return loadDeepAllAndCloseCursor(cursor);
    }
 
}