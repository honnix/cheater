/**
 * CookieValidator.java
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
package com.honnix.cheater.validator;

import org.apache.commons.httpclient.Cookie;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethodBase;

/**
 *
 */
public class CookieValidator
    implements Validator
{

    private static final String DS_ID = "DSID";

    private static final String DS_FIRST_ACCESS = "DSFirstAccess";

    private static final String DS_LAST_ACCESS = "DSLastAccess";

    public CookieValidator()
    {
        super();
    }

    private boolean hasDSFirstAccess(Cookie[] cookies)
    {
        boolean result = false;

        for (int i = 0; i < cookies.length; ++i)
        {
            if (DS_FIRST_ACCESS.equals(cookies[i].getName()))
            {
                result = true;

                break;
            }
        }

        return result;
    }

    private boolean hasDSID(Cookie[] cookies)
    {
        boolean result = false;

        for (int i = 0; i < cookies.length; ++i)
        {
            if (DS_ID.equals(cookies[i].getName()))
            {
                result = true;

                break;
            }
        }

        return result;
    }

    private boolean hasDSLastAccess(Cookie[] cookies)
    {
        boolean result = false;

        for (int i = 0; i < cookies.length; ++i)
        {
            if (DS_LAST_ACCESS.equals(cookies[i].getName()))
            {
                result = true;

                break;
            }
        }

        return result;
    }

    /*
     * (non-Javadoc)
     * @see com.honnix.cheater.validator.Validator#isHeartbeatValid(org.apache.commons.httpclient.HttpClient, org.apache.commons.httpclient.HttpMethod)
     */
    public boolean isHeartbeatValid(HttpClient httpClient, HttpMethodBase method)
    {
        return true;
    }

    /*
     * (non-Javadoc)
     * @see com.honnix.cheater.validator.Validator#isValid(org.apache.commons.httpclient.HttpMethod)
     */
    public boolean isLoginValid(HttpClient httpClient, HttpMethodBase method)
    {
        Cookie[] cookies = httpClient.getState().getCookies();
        boolean hasDSID = hasDSID(cookies);
        boolean hasDSFirstAccess = hasDSFirstAccess(cookies);
        boolean hasDSLastAccess = hasDSLastAccess(cookies);

        return hasDSID & hasDSFirstAccess & hasDSLastAccess;
    }

    /*
     * (non-Javadoc)
     * @see com.honnix.cheater.validator.Validator#isLogoutValid(org.apache.commons.httpclient.HttpClient, org.apache.commons.httpclient.HttpMethod)
     */
    public boolean isLogoutValid(HttpClient httpClient, HttpMethodBase method)
    {
        return true;
    }

}
