/**
 * Main.java
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
package com.honnix.cheater.cli;

import java.net.URISyntaxException;
import java.net.URL;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.honnix.cheater.constant.CheaterConstant;
import com.honnix.cheater.exception.SpcException;
import com.honnix.cheater.service.CheaterImpl;

/**
 *
 */
public final class Main
{

    private static final Log LOG = LogFactory.getLog(Main.class);

    /**
     * @param args
     * @throws SpcException
     */
    public static void main(String[] args)
        throws SpcException
    {
        if (System.getProperty(CheaterConstant.TRUST_STORE_KEY) == null
                && !setTrustStore())
        {
            LOG.fatal("Failed setting trust store. Use cheater.sh instead.");

            System.exit(1);
        }

        new CheaterImpl().start();
    }

    private static boolean setTrustStore()
    {
        boolean result = true;

        URL trustStoreFileURL = Main.class.getClassLoader().getResource(
                CheaterConstant.TRUST_STORE_FILE_NAME);

        if (trustStoreFileURL == null)
        {
            result = false;
        }
        else
        {
            String trustStoreFilePath = null;

            try
            {
                trustStoreFilePath = trustStoreFileURL.toURI().getPath();
            }
            catch (URISyntaxException e)
            {
                LOG.fatal("Failed setting trust store.", e);

                result = false;
            }

            if (trustStoreFilePath == null)
            {
                result = false;
            }
            else
            {
                System.setProperty(CheaterConstant.TRUST_STORE_KEY,
                        trustStoreFilePath);
            }
        }

        return result;
    }

    private Main()
    {
        super();
    }

}
