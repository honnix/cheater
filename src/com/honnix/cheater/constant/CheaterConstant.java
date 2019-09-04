/**
 * CheaterConstant.java
 * 
 * Copyright : (C) 2008 by Honnix
 * Email     : hxliang1982@gmail.com
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 * 
 */
package com.honnix.cheater.constant;

import java.util.HashMap;
import java.util.Map;

import com.honnix.cheater.util.PropertiesLoader;

/**
 * 
 */
public final class CheaterConstant
{

    public static final String FILE_SEPARATOR =
            System.getProperty("file.separator");

    public static final String TRUST_STORE_KEY = "javax.net.ssl.trustStore";

    public static final String SPC_ERROR_MESSAGE =
            "Only one cheater process is allowed. Check with 'ps -ef | grep 'cheater''";

    public static final String CHEATER_PROPERTIES_FILE_NAME =
            "cheater.properties";

    public static final String USER_NAME_KEY = "username";

    public static final String PASSWORD_KEY = "password";

    public static final String TIMEZONE_OFFSET_KEY = "tz_offset";

    public static final String REALM_KEY = "realm";

    public static final String TRUST_STORE_FILE_NAME =
            PropertiesLoader.loadProperties(CHEATER_PROPERTIES_FILE_NAME)
                    .getProperty("cert.file.name");

    public static final String USER =
            PropertiesLoader.loadProperties(CHEATER_PROPERTIES_FILE_NAME)
                    .getProperty("user.name");

    public static final String PASSWORD =
            PropertiesLoader.loadProperties(CHEATER_PROPERTIES_FILE_NAME)
                    .getProperty("password");

    public static final String TIMEZONE_OFFSET =
            PropertiesLoader.loadProperties(CHEATER_PROPERTIES_FILE_NAME)
                    .getProperty("timezone.offset");

    public static final String REALM =
            PropertiesLoader.loadProperties(CHEATER_PROPERTIES_FILE_NAME)
                    .getProperty("realm");

    public static final String LOGIN_URL =
            PropertiesLoader.loadProperties(CHEATER_PROPERTIES_FILE_NAME)
                    .getProperty("login.url");

    public static final String LOGOUT_URL =
            PropertiesLoader.loadProperties(CHEATER_PROPERTIES_FILE_NAME)
                    .getProperty("logout.url");

    public static final String HEARTBEAT_URL =
            PropertiesLoader.loadProperties(CHEATER_PROPERTIES_FILE_NAME)
                    .getProperty("heartbeat.url");

    public static final String AFTER_LOGIN_URL =
            PropertiesLoader.loadProperties(CHEATER_PROPERTIES_FILE_NAME)
                    .getProperty("after.login.url");

    public static final String USER_AGENT =
            PropertiesLoader.loadProperties(CHEATER_PROPERTIES_FILE_NAME)
                    .getProperty("user.agent");

    public static final int HOURS_TO_LIVE =
            Integer.parseInt(PropertiesLoader.loadProperties(
                    CHEATER_PROPERTIES_FILE_NAME).getProperty("hours.to.live"));

    public static final long HEARTBEAT_INTERVAL =
            Long.parseLong(PropertiesLoader.loadProperties(
                    CHEATER_PROPERTIES_FILE_NAME).getProperty(
                    "heartbeat.interval"));

    public static final long HEARTBEAT_MAX_FAIL_TIMES =
            Long.parseLong(PropertiesLoader.loadProperties(
                    CHEATER_PROPERTIES_FILE_NAME).getProperty(
                    "heartbeat.max.fail.times"));

    public static final int LOGIN_MAX_RETRY_TIMES =
            Integer.parseInt(PropertiesLoader.loadProperties(
                    CHEATER_PROPERTIES_FILE_NAME).getProperty(
                    "login.max.retry.times"));

    public static final long LOGIN_RETRY_INTERVAL =
            Long.parseLong(PropertiesLoader.loadProperties(
                    CHEATER_PROPERTIES_FILE_NAME).getProperty(
                    "login.retry.interval"));

    public static final String CLIENT =
            PropertiesLoader.loadProperties(CHEATER_PROPERTIES_FILE_NAME)
                    .getProperty("client");

    public static final String SPC =
            PropertiesLoader.loadProperties(CHEATER_PROPERTIES_FILE_NAME)
                    .getProperty("spc");

    public static final int SOCKET_SPC_PORT =
            Integer.parseInt(PropertiesLoader.loadProperties(
                    CHEATER_PROPERTIES_FILE_NAME)
                    .getProperty("socket.spc.port"));

    public static final int INITIALIZING = 0x0001;

    public static final int INITIALIZED = 0x0002;

    public static final int STARTING = 0x0003;

    public static final int STARTED = 0x0004;

    public static final int LOGGING_IN = 0x0005;

    public static final int LOGGED_IN = 0x0006;

    public static final int HEARTBEATING = 0x0007;

    public static final int HEARTBEATING_RETRYING = 0x0008;

    public static final int HEARTBEATING_RETRY_FAILED = 0x0009;

    public static final int LOGGING_OUT = 0x000A;

    public static final int LOGGED_OUT = 0x000B;

    public static final int LOGGING_IN_RETRYING = 0x000C;

    public static final int STOPPING = 0x000D;

    public static final int STOPPED = 0x000E;

    public static final int LOGGING_IN_RETRY_FAILED = 0x000F;

    public static final Map<Integer, String> STATUS_MAP =
            new HashMap<Integer, String>();

    static
    {
        STATUS_MAP.put(INITIALIZING, "initializing");
        STATUS_MAP.put(INITIALIZED, "initialized");
        STATUS_MAP.put(STARTING, "starting");
        STATUS_MAP.put(STARTED, "started");
        STATUS_MAP.put(LOGGING_IN, "logging in");
        STATUS_MAP.put(LOGGED_IN, "logged in");
        STATUS_MAP.put(HEARTBEATING, "heartbeating");
        STATUS_MAP.put(HEARTBEATING_RETRYING, "heartbeating retrying");
        STATUS_MAP.put(HEARTBEATING_RETRY_FAILED, "heartbeating retry failed");
        STATUS_MAP.put(LOGGING_OUT, "logging out");
        STATUS_MAP.put(LOGGED_OUT, "logged out");
        STATUS_MAP.put(LOGGING_IN_RETRYING, "logging in retrying");
        STATUS_MAP.put(STOPPING, "stopping");
        STATUS_MAP.put(STOPPED, "stopped");
        STATUS_MAP.put(LOGGING_IN_RETRY_FAILED, "logging in retry failed");
    }

    private CheaterConstant()
    {
        super();
    }

}
