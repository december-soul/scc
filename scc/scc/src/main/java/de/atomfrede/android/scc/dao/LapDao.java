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

import de.atomfrede.android.scc.dao.Lap;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table LAP.
*/
public class LapDao extends AbstractDao<Lap, Long> {

    public static final String TABLENAME = "LAP";

    /**
     * Properties of entity Lap.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property LapNumber = new Property(0, Integer.class, "lapNumber", false, "LAP_NUMBER");
        public final static Property Id = new Property(1, Long.class, "id", true, "_id");
        public final static Property CompetitionId = new Property(2, long.class, "competitionId", false, "COMPETITION_ID");
    };

    private DaoSession daoSession;

    private Query<Lap> competition_LapListQuery;

    public LapDao(DaoConfig config) {
        super(config);
    }
    
    public LapDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
        this.daoSession = daoSession;
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "'LAP' (" + //
                "'LAP_NUMBER' INTEGER," + // 0: lapNumber
                "'_id' INTEGER PRIMARY KEY ," + // 1: id
                "'COMPETITION_ID' INTEGER NOT NULL );"); // 2: competitionId
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "'LAP'";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, Lap entity) {
        stmt.clearBindings();
 
        Integer lapNumber = entity.getLapNumber();
        if (lapNumber != null) {
            stmt.bindLong(1, lapNumber);
        }
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(2, id);
        }
        stmt.bindLong(3, entity.getCompetitionId());
    }

    @Override
    protected void attachEntity(Lap entity) {
        super.attachEntity(entity);
        entity.__setDaoSession(daoSession);
    }

    /** @inheritdoc */
    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 1) ? null : cursor.getLong(offset + 1);
    }    

    /** @inheritdoc */
    @Override
    public Lap readEntity(Cursor cursor, int offset) {
        Lap entity = new Lap( //
            cursor.isNull(offset + 0) ? null : cursor.getInt(offset + 0), // lapNumber
            cursor.isNull(offset + 1) ? null : cursor.getLong(offset + 1), // id
            cursor.getLong(offset + 2) // competitionId
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, Lap entity, int offset) {
        entity.setLapNumber(cursor.isNull(offset + 0) ? null : cursor.getInt(offset + 0));
        entity.setId(cursor.isNull(offset + 1) ? null : cursor.getLong(offset + 1));
        entity.setCompetitionId(cursor.getLong(offset + 2));
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(Lap entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(Lap entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    /** @inheritdoc */
    @Override    
    protected boolean isEntityUpdateable() {
        return true;
    }
    
    /** Internal query to resolve the "lapList" to-many relationship of Competition. */
    public synchronized List<Lap> _queryCompetition_LapList(long competitionId) {
        if (competition_LapListQuery == null) {
            QueryBuilder<Lap> queryBuilder = queryBuilder();
            queryBuilder.where(Properties.CompetitionId.eq(competitionId));
            queryBuilder.orderRaw("LAP_NUMBER DESC");
            competition_LapListQuery = queryBuilder.build();
        } else {
            competition_LapListQuery.setParameter(0, competitionId);
        }
        return competition_LapListQuery.list();
    }

    private String selectDeep;

    protected String getSelectDeep() {
        if (selectDeep == null) {
            StringBuilder builder = new StringBuilder("SELECT ");
            SqlUtils.appendColumns(builder, "T", getAllColumns());
            builder.append(',');
            SqlUtils.appendColumns(builder, "T0", daoSession.getCompetitionDao().getAllColumns());
            builder.append(" FROM LAP T");
            builder.append(" LEFT JOIN COMPETITION T0 ON T.'COMPETITION_ID'=T0.'_id'");
            builder.append(' ');
            selectDeep = builder.toString();
        }
        return selectDeep;
    }
    
    protected Lap loadCurrentDeep(Cursor cursor, boolean lock) {
        Lap entity = loadCurrent(cursor, 0, lock);
        int offset = getAllColumns().length;

        Competition competition = loadCurrentOther(daoSession.getCompetitionDao(), cursor, offset);
         if(competition != null) {
            entity.setCompetition(competition);
        }

        return entity;    
    }

    public Lap loadDeep(Long key) {
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
    public List<Lap> loadAllDeepFromCursor(Cursor cursor) {
        int count = cursor.getCount();
        List<Lap> list = new ArrayList<Lap>(count);
        
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
    
    protected List<Lap> loadDeepAllAndCloseCursor(Cursor cursor) {
        try {
            return loadAllDeepFromCursor(cursor);
        } finally {
            cursor.close();
        }
    }
    

    /** A raw-style query where you can pass any WHERE clause and arguments. */
    public List<Lap> queryDeep(String where, String... selectionArg) {
        Cursor cursor = db.rawQuery(getSelectDeep() + where, selectionArg);
        return loadDeepAllAndCloseCursor(cursor);
    }
 
}
