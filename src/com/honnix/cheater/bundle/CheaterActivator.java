/**
 * CheaterActivator.java
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
package com.honnix.cheater.bundle;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Hashtable;

import org.apache.log4j.xml.DOMConfigurator;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import com.honnix.cheater.constant.CheaterConstant;
import com.honnix.cheater.service.Cheater;
import com.honnix.cheater.service.CheaterImpl;

/**
 *
 */
public class CheaterActivator
    implements BundleActivator
{

    private static final String TEMP_TRUST_STORE = new StringBuilder(System
            .getProperty("java.io.tmpdir")).append(
            CheaterConstant.FILE_SEPARATOR).append(
            CheaterConstant.TRUST_STORE_FILE_NAME).toString();

    private final Cheater cheater;

    public CheaterActivator()
    {
        super();

        // Disable logging if running as bundle.
        // System.setProperty("org.apache.commons.logging.Log",
        // "org.apache.commons.logging.impl.NoOpLog");

        DOMConfigurator.configure(CheaterActivator.class.getClassLoader()
                .getResource("log4j.xml"));

        cheater = new CheaterImpl();
    }

    private void setTrustStore(BundleContext context)
        throws IOException
    {
        InputStream is = context.getBundle().getEntry("/etc/jssecacerts")
                .openStream();
        BufferedOutputStream os = new BufferedOutputStream(
                new FileOutputStream(TEMP_TRUST_STORE));

        byte[] buffer = new byte[255];
        int length;

        while ((length = is.read(buffer)) != -1)
        {
            os.write(buffer, 0, length);
        }

        os.flush();
        os.close();
        is.close();

        System.setProperty(CheaterConstant.TRUST_STORE_KEY, TEMP_TRUST_STORE);
    }

    /*
     * (non-Javadoc)
     * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
     */
    public void start(BundleContext context)
        throws Exception // NOPMD by ehonlia on 6/24/08 4:38 PM
    {
        context.registerService(Cheater.class.getName(), cheater,
                new Hashtable<Object, Object>());

        setTrustStore(context);

        cheater.start();
    }

    /*
     * (non-Javadoc)
     * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
     */
    public void stop(BundleContext context)
        throws Exception // NOPMD by ehonlia on 6/24/08 4:38 PM
    {
        cheater.stop();

        unsetTrustStore();
    }

    private void unsetTrustStore()
    {
        File file = new File(TEMP_TRUST_STORE);

        if (file.exists())
        {
            file.delete();
        }

        System.clearProperty(CheaterConstant.TRUST_STORE_KEY);
    }

}
