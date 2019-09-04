/**
 * AdminServer.java
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
package com.honnix.cheater.admin;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.honnix.cheater.service.Cheater;

/**
 *
 */
public class AdminServer
    implements Runnable
{

    private static final Log LOG = LogFactory.getLog(AdminServer.class);

    private Thread thisThread;

    private ServerSocket serverSocket;

    private Cheater cheater;

    private boolean isStarted;

    // Well, we do not lock this variable.
    private boolean hasClient;

    private final Object lock;

    public AdminServer(ServerSocket serverSocket, Cheater cheater)
    {
        super();

        thisThread = new Thread(this);
        this.serverSocket = serverSocket;
        this.cheater = cheater;
        isStarted = false;
        hasClient = false;

        lock = new Object();
    }

    public Cheater getCheater()
    {
        return cheater;
    }

    public void run()
    {
        while (true)
        {
            synchronized (lock)
            {
                if (!isStarted)
                {
                    break;
                }
            }

            Socket clientSocket = null;

            try
            {
                clientSocket = serverSocket.accept();
            }
            catch (SocketException e)
            {
                // Here we can assume that if there is SocketException, it means
                // the socket has been closed.
                break;
            }
            catch (IOException e)
            {
                LOG.error("Failed accepting admin socket connection.", e);

                continue;
            }

            if (hasClient)
            {
                try
                {
                    PrintWriter pw =
                            new PrintWriter(clientSocket.getOutputStream());

                    pw.println("Someone else is administrating me.");
                    pw.flush();

                    clientSocket.close();
                }
                catch (IOException e)
                {
                    LOG.warn("Error operating client socket.", e);
                }
            }
            else
            {
                hasClient = true;

                new AdminClient(this, clientSocket).start();
            }
        }
    }

    public void setHasClient(boolean hasClient)
    {
        this.hasClient = hasClient;
    }

    public void start()
    {
        synchronized (lock)
        {
            if (!isStarted)
            {
                isStarted = true;

                thisThread.start();
            }
        }
    }

    public void stop()
    {
        synchronized (lock)
        {
            if (isStarted)
            {
                isStarted = false;

                try
                {
                    serverSocket.close();
                }
                catch (IOException e)
                {
                    LOG.error("Failed closing server socket.", e);
                }

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
    }

}
