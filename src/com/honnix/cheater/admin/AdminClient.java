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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.honnix.cheater.constant.CheaterConstant;

/**
 *
 */
public class AdminClient
    extends Thread
{

    private static final Log LOG = LogFactory.getLog(AdminClient.class);

    private static final String PROMPT = "> ";

    private AdminServer server;

    private Socket clientSocket;

    public AdminClient(AdminServer server, Socket clientSocket)
    {
        this.server = server;
        this.clientSocket = clientSocket;
    }

    public void run()
    {
        try
        {
            BufferedReader br =
                    new BufferedReader(new InputStreamReader(clientSocket
                            .getInputStream()));
            PrintWriter pw = new PrintWriter(clientSocket.getOutputStream());

            pw.print(PROMPT);
            pw.flush();

            while (true)
            {
                String command = br.readLine();

                if ("status".equals(command))
                {
                    pw.println(CheaterConstant.STATUS_MAP.get(server
                            .getCheater().getCurrentStatus()));
                    pw.print(PROMPT);
                    pw.flush();
                }
                else if ("help".equals(command))
                {
                    pw.println("status    to show current status of cheater");
                    pw.println("quit      to quit administration connection");
                    pw.println("shutdown  to shutdown cheater");
                    pw.println("help      to show this message");
                    pw.print(PROMPT);
                    pw.flush();
                }
                else if ("shutdown".equals(command))
                {
                    clientSocket.close();
                    server.stop();
                    server.getCheater().stop();

                    break;
                }
                else if ("quit".equals(command) || command == null)
                {
                    server.setHasClient(false);
                    clientSocket.close();

                    break;
                }
                else
                {
                    pw.println(new StringBuilder("Unknown command,").append(
                            " use \"status|quit|shutdown|help\".").toString());
                    pw.print(PROMPT);
                    pw.flush();
                }
            }
        }
        catch (IOException e)
        {
            LOG.error("Error operating client socket.", e);
        }
        finally
        {
            try
            {
                clientSocket.close();
            }
            catch (IOException e)
            {
                LOG.error("Error closing client socket.", e);
            }
        }
    }
}
