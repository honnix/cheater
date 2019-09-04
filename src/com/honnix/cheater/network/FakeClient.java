/**
 * FakeClient.java
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

/**
 * @author ehonlia
 */
public class FakeClient
    implements Client
{

    public FakeClient()
    {
        super();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.honnix.cheater.network.Client#heartbeat()
     */
    public boolean heartbeat()
    {
        System.out.println("Heartbeating..."); // NOPMD by ehonlia on 6/24/08
                                               // 4:41 PM

        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.honnix.cheater.network.Client#login()
     */
    public boolean login()
    {
        System.out.println("Logging in..."); // NOPMD by ehonlia on 6/24/08 4:41
                                             // PM

        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.honnix.cheater.network.Client#logout()
     */
    public boolean logout()
    {
        System.out.println("Logging out..."); // NOPMD by ehonlia on 6/24/08
                                              // 4:41 PM

        return true;
    }

}
