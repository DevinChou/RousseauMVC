package org.rousseau4j.framework.helper;

import org.rousseau4j.framework.constant.ConfigConstant;
import org.rousseau4j.framework.util.PropertiesUtil;

import java.util.Properties;

/**
 * 配置获取助手
 * Created by ZhouHangqi on 2017/7/22.
 */
public final class ConfigHelper {

    private static final Properties CONFIG_PROPS = PropertiesUtil.loadProps(ConfigConstant.CONFIG_FILE);

    public static String getJdbcDriver() {
        return PropertiesUtil.getString(CONFIG_PROPS, ConfigConstant.JDBC_DRIVER);
    }

    public static String getJdbcUrl() {
        return PropertiesUtil.getString(CONFIG_PROPS, ConfigConstant.JDBC_URL);
    }

    public static String getJdbcUsername() {
        return PropertiesUtil.getString(CONFIG_PROPS, ConfigConstant.JDBC_USERNAME);
    }

    public static String getJdbcPassword() {
        return PropertiesUtil.getString(CONFIG_PROPS, ConfigConstant.JDBC_PASSWORD);
    }

    public static String getAppBasePackage() {
        return PropertiesUtil.getString(CONFIG_PROPS, ConfigConstant.APP_BASE_PACKAGE);
    }

    public static String getAppJspPath() {
        return PropertiesUtil.getString(CONFIG_PROPS, ConfigConstant.APP_JSP_PATH);
    }

    public static String getAppAssetPath() {
        return PropertiesUtil.getString(CONFIG_PROPS, ConfigConstant.APP_ASSET_PATH);
    }
}
