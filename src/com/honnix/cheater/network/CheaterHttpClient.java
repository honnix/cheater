/**
 * CheaterHttpClient.java
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
package com.honnix.cheater.network;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.honnix.cheater.constant.CheaterConstant;
import com.honnix.cheater.validator.ContentValidator;
import com.honnix.cheater.validator.CookieValidator;
import com.honnix.cheater.validator.Validator;

/**
 *
 */
public class CheaterHttpClient
    implements Client
{

    private static final Log LOG = LogFactory.getLog(CheaterHttpClient.class);

    private HttpClient httpClient;

    private List<Validator> validatorList;

    public CheaterHttpClient()
    {
        super();

        initHttpClient();

        validatorList = new ArrayList<Validator>();
        validatorList.add(new CookieValidator());
        validatorList.add(new ContentValidator());
    }

    private boolean execute(HttpMethod method)
    {
        boolean result = true;

        try
        {
            httpClient.executeMethod(method);

            // We only follow the redirection once.
            Header locationHeader = method.getResponseHeader("location");

            if (locationHeader != null)
            {
                HttpMethod getMethod = new GetMethod(locationHeader.getValue());

                httpClient.executeMethod(getMethod);
            }
        }
        catch (Exception e)
        {
            LOG.error("Method execution error.", e);

            result = false;
        }

        return result;
    }

    private void initHttpClient()
    {
        httpClient = new HttpClient();

        httpClient.getParams().setCookiePolicy(
                CookiePolicy.BROWSER_COMPATIBILITY);
        httpClient.getParams().setParameter("http.useragent",
                CheaterConstant.USER_AGENT);
        httpClient.getParams().setParameter(
                HttpMethodParams.SINGLE_COOKIE_HEADER, Boolean.TRUE);
    }

    /*
     * (non-Javadoc)
     * @see com.honnix.cheater.network.Client#heartbeat()
     */
    public boolean heartbeat()
    {
        GetMethod getMethod = new GetMethod(CheaterConstant.HEARTBEAT_URL);
        boolean result = execute(getMethod);

        if (result)
        {
            for (Validator validator : validatorList)
            {
                result = validator.isHeartbeatValid(httpClient, getMethod);
            }
        }

        getMethod.releaseConnection();

        return result;
    }

    /*
     * (non-Javadoc)
     * @see com.honnix.cheater.network.Client#login()
     */
    public boolean login()
    {
        // Clear all exising cookies before logging in.
        httpClient.getState().clearCookies();

        PostMethod postMethod = new PostMethod(CheaterConstant.LOGIN_URL);

        postMethod.setParameter(CheaterConstant.USER_NAME_KEY,
                CheaterConstant.USER);
        postMethod.setParameter(CheaterConstant.PASSWORD_KEY,
                CheaterConstant.PASSWORD);
        postMethod.setParameter(CheaterConstant.TIMEZONE_OFFSET_KEY,
                CheaterConstant.TIMEZONE_OFFSET);
        postMethod.setParameter(CheaterConstant.REALM_KEY,
                CheaterConstant.REALM);

        boolean result = execute(postMethod);

        if (result)
        {
            for (Validator validator : validatorList)
            {
                result = validator.isLoginValid(httpClient, postMethod);
            }
        }

        postMethod.releaseConnection();

        return result;
    }

    /*
     * (non-Javadoc)
     * @see com.honnix.cheater.network.Client#logout()
     */
    public boolean logout()
    {
        GetMethod getMethod = new GetMethod(CheaterConstant.LOGOUT_URL);
        boolean result = execute(getMethod);

        if (result)
        {
            for (Validator validator : validatorList)
            {
                result = validator.isLogoutValid(httpClient, getMethod);
            }
        }

        getMethod.releaseConnection();

        return result;
    }

}
