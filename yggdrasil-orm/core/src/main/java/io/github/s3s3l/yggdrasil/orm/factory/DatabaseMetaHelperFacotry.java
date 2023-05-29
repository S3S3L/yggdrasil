package io.github.s3s3l.yggdrasil.orm.factory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import io.github.s3s3l.yggdrasil.orm.enumerations.DatabaseType;
import io.github.s3s3l.yggdrasil.orm.exception.DataBindExpressException;
import io.github.s3s3l.yggdrasil.orm.exception.RemoteMetaManagerGenerateException;
import io.github.s3s3l.yggdrasil.orm.meta.remote.DatabaseMetaHelper;
import io.github.s3s3l.yggdrasil.utils.reflect.scan.ClassScanner;

public class DatabaseMetaHelperFacotry {

    public static DatabaseMetaHelperFacotry DEFAULT = new DatabaseMetaHelperFacotry();

    static {
        DEFAULT.initDefault();
    }

    private final Map<DatabaseType, DatabaseMetaHelper> DATABASE_META_HELPERS = new ConcurrentHashMap<>();

    private boolean inited = false;

    public synchronized void initDefault() {
        if (inited) {
            return;
        }
        new ClassScanner().scan("io.github.s3s3l.yggdrasil.orm")
                .stream()
                .filter(c -> DatabaseMetaHelper.class.isAssignableFrom(c) && !c.isInterface() && !c.isEnum()
                        && !Modifier.isAbstract(c.getModifiers()))
                .map(c -> {
                    try {
                        return (DatabaseMetaHelper) c.getConstructor()
                                .newInstance();
                    } catch (InstantiationException | IllegalAccessException | IllegalArgumentException
                            | InvocationTargetException | NoSuchMethodException | SecurityException e) {
                        throw new DataBindExpressException(e);
                    }
                })
                .forEach(this::register);
        inited = true;
    }

    public void register(DatabaseMetaHelper databaseMetaHelper) {
        DATABASE_META_HELPERS.put(databaseMetaHelper.databaseType(), databaseMetaHelper);
    }

    public DatabaseMetaHelper getInstance(DatabaseType databaseType) {
        return DATABASE_META_HELPERS.computeIfAbsent(databaseType, k -> {
            throw new RemoteMetaManagerGenerateException(
                    "DatabaseMetaHelper not found for database type: " + databaseType);
        });
    }
}
