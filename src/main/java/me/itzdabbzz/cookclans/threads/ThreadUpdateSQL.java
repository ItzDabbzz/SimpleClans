package me.itzdabbzz.cookclans.threads;

import java.sql.Connection;
import java.sql.SQLException;
import me.itzdabbzz.cookclans.CookClans;

/**
 *
 * @author NeT32
 */
public class ThreadUpdateSQL extends Thread {

    Connection Connection;
    String Query;
    String TypeSQL;

    public ThreadUpdateSQL(Connection Connection, String Query, String TypeSQL)
    {
        this.Query = Query;
        this.Connection = Connection;
        this.TypeSQL = TypeSQL;
    }

    @Override
    public void run()
    {
        try
        {
            this.Connection.createStatement().executeUpdate(this.Query);
        }
        catch (SQLException ex)
        {
            if (!ex.toString().contains("not return ResultSet"))
            {
                CookClans.getLog().severe("[Thread] Error at SQL " + this.TypeSQL + " Query: " + ex);
                CookClans.getLog().severe("[Thread] Query: " + this.Query);
            }
        }
    }
}
