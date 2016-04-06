package com.mkhorie.greendaoretrofit.data.greendao;

import android.database.sqlite.SQLiteDatabase;

import com.mkhorie.greendaoretrofit.models.User;

import java.util.Map;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.AbstractDaoSession;
import de.greenrobot.dao.identityscope.IdentityScopeType;
import de.greenrobot.dao.internal.DaoConfig;

/**
 * {@inheritDoc}
 *
 * @see de.greenrobot.dao.AbstractDaoSession
 */
@SuppressWarnings("unused")
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig userDaoConfig;

    private final UserDao userDao;

    public DaoSession(SQLiteDatabase db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        userDaoConfig = daoConfigMap.get(UserDao.class).clone();
        userDaoConfig.initIdentityScope(type);

        userDao = new UserDao(userDaoConfig, this);

        registerDao(User.class, userDao);
    }

    public void clear() {
        userDaoConfig.getIdentityScope().clear();
    }

    public UserDao getUserDao() {
        return userDao;
    }
}
