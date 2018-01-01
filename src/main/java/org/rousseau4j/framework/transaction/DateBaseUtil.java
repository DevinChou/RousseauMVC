package org.rousseau4j.framework.transaction;

import com.mysql.jdbc.Connection;
import lombok.extern.slf4j.Slf4j;
import org.rousseau4j.framework.helper.ConfigHelper;

import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created by zhouh on 2018/1/1.
 */
@Slf4j
public class DateBaseUtil {

    private static String JDBC_DRIVER = ConfigHelper.getJdbcDriver();
    private static String JDBC_URL = ConfigHelper.getJdbcUrl();
    private static String USERNAME = ConfigHelper.getJdbcUsername();
    private static String PASSWORD = ConfigHelper.getJdbcPassword();

    private static ThreadLocal<Connection> connectionThreadLocal = new ThreadLocal<>();

    /**
     * 获取数据库连接
     * @return
     */
    public static Connection getConnection() {
        Connection conn = connectionThreadLocal.get();
        try {
            if (conn == null) {
                Class.forName(JDBC_DRIVER);
                conn = (Connection) DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
                connectionThreadLocal.set(conn);
            }
        } catch (ClassNotFoundException e) {
            log.error("Jdbc_Driver not found, Jdbc_Driver={}", JDBC_DRIVER, e);
            throw new RuntimeException(e);
        } catch (SQLException e) {
            log.error("Connection cannot build, jdbc_url={}, username={}, password={}", JDBC_URL,
                    USERNAME, PASSWORD, e);
            throw new RuntimeException(e);
        }
        return conn;
    }

    /**
     * 关闭链接
     */
    public static void closeConnection() {
        Connection conn = connectionThreadLocal.get();
        try {
            if (conn != null) {
                conn.close();
                connectionThreadLocal.remove();
            }
        } catch (SQLException e) {
            log.error("Connection cannot be closed", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * 开启事务
     */
    public static void beginTransaction() {
        Connection conn = getConnection();
        if (conn != null) {
            try {
                conn.setAutoCommit(false);
            } catch (SQLException e) {
                log.error("Connection set autocommit false error", e);
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * 提交事务
     */
    public static void commitTransaction() {
        Connection conn = getConnection();
        if (conn != null) {
            try {
                conn.commit();
                closeConnection();
            } catch (SQLException e) {
                log.error("Connection commit transaction error", e);
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * 回滚事务
     */
    public static void rollbackTransaction() {
        Connection conn = getConnection();
        if (conn != null) {
            try {
                conn.rollback();
                closeConnection();
            } catch (SQLException e) {
                log.error("Connection rollback transaction error", e);
                throw new RuntimeException(e);
            }
        }
    }
}
