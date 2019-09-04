/**
 * ContentValidator.java
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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethodBase;

import com.honnix.cheater.constant.CheaterConstant;

/**
 *
 */
public class ContentValidator
    implements Validator
{

    private static final String HEARTBEAT_KEY_STRING = "function Heartbeat()";

    private static final String LOGOUT_KEY_STRING = "Click here to sign in again";

    private boolean isValidContent(HttpMethodBase method, String keyString)
    {
        boolean result = false;
        String charSet = method.getResponseCharSet();

        try
        {
            BufferedReader br = new BufferedReader(new InputStreamReader(method
                    .getResponseBodyAsStream(), charSet));
            String line = null;

            while (!result && (line = br.readLine()) != null)
            {
                if (line.indexOf(keyString) != -1)
                {
                    result = true;
                }
            }
        }
        catch (IOException e)
        {
            result = false;
        }

        return result;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.honnix.cheater.validator.Validator#isHeartbeatValid(org.apache.commons
     * .httpclient.HttpClient, org.apache.commons.httpclient.HttpMethod)
     */
    public boolean isHeartbeatValid(HttpClient httpClient, HttpMethodBase method)
    {
        return isValidContent(method, HEARTBEAT_KEY_STRING);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.honnix.cheater.validator.Validator#isValid(org.apache.commons.httpclient
     * .HttpMethod)
     */
    public boolean isLoginValid(HttpClient httpClient, HttpMethodBase method)
    {
        boolean result = false;

        Header header = method.getResponseHeader("location");

        if (header != null
                && CheaterConstant.AFTER_LOGIN_URL.equals(header.getValue()))
        {
            result = true;
        }

        return result;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.honnix.cheater.validator.Validator#isLogoutValid(org.apache.commons
     * .httpclient.HttpClient, org.apache.commons.httpclient.HttpMethod)
     */
    public boolean isLogoutValid(HttpClient httpClient, HttpMethodBase method)
    {
        return isValidContent(method, LOGOUT_KEY_STRING);
    }

}
