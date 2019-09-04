/**
 * CheaterImpl.java
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
package com.honnix.cheater.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.honnix.cheater.constant.CheaterConstant;
import com.honnix.cheater.exception.SpcException;
import com.honnix.cheater.network.Client;
import com.honnix.cheater.network.FakeClient;
import com.honnix.cheater.spc.FakeSpc;
import com.honnix.cheater.spc.Spc;

/**
 * 
 */
public class CheaterImpl
    implements Cheater, Runnable
{

    private static final Log LOG = LogFactory.getLog(CheaterImpl.class);

    private static final String THREAD_NAME = "Cheater-Thread";

    private Client client;

    private Spc spc;

    private final Thread thisThread;

    private boolean isStarted;

    private int currentStatus;

    private final Object lock;

    public CheaterImpl()
    {
        super();

        currentStatus = CheaterConstant.INITIALIZING;

        try
        {
            client =
                    (Client) Class.forName(CheaterConstant.CLIENT)
                            .newInstance();
        }
        catch (Exception e)
        {
            LOG.fatal("Could not instantiate client. Assume testing run.", e);

            client = new FakeClient();
        }

        try
        {
            spc = (Spc) Class.forName(CheaterConstant.SPC).newInstance();
        }
        catch (Exception e)
        {
            LOG.fatal("Could not instantiate spc. Assume bundle run.", e);

            spc = new FakeSpc();
        }

        thisThread = new Thread(this);
        thisThread.setName(THREAD_NAME);
        isStarted = false;

        lock = new Object();

        currentStatus = CheaterConstant.INITIALIZED;
    }

    public int getCurrentStatus()
    {
        return currentStatus;
    }

    private void heartbeatLoop()
    {
        int count = 0;

        for (int i = 0; i < 120 * CheaterConstant.HOURS_TO_LIVE; i++)
        {
            if (client.heartbeat())
            {
                currentStatus = CheaterConstant.HEARTBEATING;
                count = 0;
            }
            else
            {
                if (++count == CheaterConstant.HEARTBEAT_MAX_FAIL_TIMES)
                {
                    currentStatus = CheaterConstant.HEARTBEATING_RETRY_FAILED;

                    StringBuilder sb =
                            new StringBuilder(
                                    "Heartbeat failed continuously for ")
                                    .append(
                                            CheaterConstant.HEARTBEAT_MAX_FAIL_TIMES)
                                    .append(" times. Try to login again.");

                    LOG.warn(sb.toString());

                    break;
                }

                currentStatus = CheaterConstant.HEARTBEATING_RETRYING;

                StringBuilder sb =
                        new StringBuilder("Heartbeat failed. ").append("Wait ")
                                .append(CheaterConstant.HEARTBEAT_INTERVAL)
                                .append(" seconds to retry.");

                LOG.warn(sb.toString());
            }

            synchronized (lock)
            {
                if (!isStarted)
                {
                    break;
                }

                try
                {
                    lock.wait(CheaterConstant.HEARTBEAT_INTERVAL * 1000);
                }
                catch (InterruptedException e)
                {
                    if (!isStarted)
                    {
                        break;
                    }
                    else
                    {
                        LOG.warn("Thread interrupted unexpectedly. Ignore.", e);
                    }
                }
            }
        }
    }

    public void run()
    {
        int retryTimes = 0;

        while (true)
        {
            synchronized (lock)
            {
                if (!isStarted)
                {
                    break;
                }
            }

            currentStatus = CheaterConstant.LOGGING_IN;

            if (client.login())
            {
                currentStatus = CheaterConstant.LOGGED_IN;

                LOG.info("Login successfully.");

                heartbeatLoop();

                currentStatus = CheaterConstant.LOGGING_OUT;

                if (client.logout())
                {
                    LOG.info("Logout successfully.");
                }
                else
                {
                    LOG.warn("Logout failed. But it's OK.");
                }

                currentStatus = CheaterConstant.LOGGED_OUT;
            }
            else
            {
                currentStatus = CheaterConstant.LOGGING_IN_RETRYING;

                if (++retryTimes > CheaterConstant.LOGIN_MAX_RETRY_TIMES)
                {
                    LOG.error("Maximum login retry times reached. Quit...");

                    synchronized (lock)
                    {
                        isStarted = false;
                    }

                    currentStatus = CheaterConstant.LOGGING_IN_RETRY_FAILED;

                    break;
                }

                StringBuilder sb =
                        new StringBuilder("Login failed. ").append("Wait ")
                                .append(CheaterConstant.LOGIN_RETRY_INTERVAL)
                                .append(" seconds to retry.");

                LOG.error(sb.toString());

                synchronized (lock)
                {
                    if (!isStarted)
                    {
                        break;
                    }

                    try
                    {
                        lock.wait(CheaterConstant.LOGIN_RETRY_INTERVAL * 1000);
                    }
                    catch (InterruptedException e)
                    {
                        if (!isStarted)
                        {
                            break;
                        }
                        else
                        {
                            LOG.warn(
                                    "Thread interrupted unexpectedly. Ignore.",
                                    e);
                        }
                    }
                }
            }
        }
    }

    public void start()
        throws SpcException
    {
        currentStatus = CheaterConstant.STARTING;

        synchronized (lock)
        {
            if (!isStarted)
            {
                if (!spc.check(this))
                {
                    throw new SpcException(CheaterConstant.SPC_ERROR_MESSAGE);
                }

                isStarted = true;

                thisThread.start();
            }
        }

        currentStatus = CheaterConstant.STARTED;
    }

    public void stop()
    {
        currentStatus = CheaterConstant.STOPPING;

        synchronized (lock)
        {
            if (isStarted)
            {
                isStarted = false;

                thisThread.interrupt();
            }
        }

        try
        {
            thisThread.join();
        }
        catch (InterruptedException e)
        {
            LOG.warn("Thread interrupted unexpectedly when joining.", e);
        }

        currentStatus = CheaterConstant.STOPPED;
    }

}
