package org.rousseau4j.framework.util;

import lombok.extern.log4j.Log4j;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by ZhouHangqi on 2017/7/31.
 */
@Log4j
public final class StreamUtil {

    public static String getString(InputStream is) {
        StringBuilder sb = new StringBuilder();
        try {
            BufferedReader read = new BufferedReader(new InputStreamReader(is));
            String line;
            while ((line = read.readLine()) != null) {
                sb.append(line);
            }
        } catch (Exception e) {
            log.error("cast stream to string failure", e);
            throw new RuntimeException(e);
        }
        return sb.toString();
    }
}
