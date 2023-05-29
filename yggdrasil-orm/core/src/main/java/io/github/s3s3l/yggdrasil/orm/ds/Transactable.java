package io.github.s3s3l.yggdrasil.orm.ds;

import java.sql.SQLException;

public interface Transactable {
    
    /**
     * 为当前线程开启事务
     * 
     * @throws SQLException
     */
    void transactional() throws SQLException;

    /**
     * 提交当前线程的事务
     * 
     * @throws SQLException
     */
    void transactionalCommit() throws SQLException;

    /**
     * 回滚当前线程的事务
     * 
     * @throws SQLException
     */
    void rollback() throws SQLException;
}
