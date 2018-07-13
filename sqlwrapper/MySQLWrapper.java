package sqlwrapper;

import anonymizer.DBSelector;

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;


/**
 * Implementation of the SQLWrapper interface for MySQL database.
 *
 */

public class MySQLWrapper implements SQLWrapper {


        private DBSelector config =new DBSelector();

        private final String driver = "com.mysql.cj.jdbc.Driver";
        private final String protocol = "jdbc:mysql:";
        private final String dbName = config.getdbName();
        private String dbPath = config.getdbPath();
        private String username = config.getdbUser();
        private String password = config.getdbPassword();

        private Connection conn = null;

	/**
	 * Single instance created upon class loading.
	 */

    private static final MySQLWrapper mysqlInstance = new MySQLWrapper();

    public MySQLWrapper() throws Exception {
    }

	/**
	 * Returns the singleton sqLite instance
	 * @return mysqlInstance
	 */
    public static MySQLWrapper getInstance() {

    	try{
    		if(mysqlInstance.conn == null || mysqlInstance.conn.isClosed())
    			mysqlInstance.conn = mysqlInstance.getConnection();

    	}catch(Exception ex){
    		ex.printStackTrace();
    		return null;
    	}

        return mysqlInstance;
     }

	/**
	 * Execute SQL statement for data definition and manipulation
	 * @param sql Sql operation
	 * @return success of executed operation
	 */

	public boolean execute(String sql) {
		Statement st = null;
		try {
			st =mysqlInstance.conn.createStatement();
			boolean successful = st.execute(sql);
			st.close();
			return successful;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		} finally {
			try {
				st.close();
                //conn.close();
			} catch(Exception e) {}
		}
	}



	/**
	 * Execute SQL statement for data query
	 * @param sql Sql operation
	 * @return queryResult
	 */

	public QueryResult executeQuery(String sql) {

		QueryResult result = null;

		try {
			Statement st =mysqlInstance.conn.createStatement();
			result = new QueryResult(st, sql);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}


	/**
	 * Commit transaction
	 */

	public void commit() {
		try {
			conn.commit();
		} catch(SQLException e) {
			e.printStackTrace();
		}
	}


	/**
	 * Commit transaction, delete temporary data
	 * @return success of the operation
	 */

	public boolean flush() {
		try {
			conn.commit();
			conn.close();

			String dbPath = mysqlInstance.dbPath +"/" +mysqlInstance.dbName;
			File file = new File(dbPath);
			boolean succ = file.delete();

			if(succ){
				File dir = new File(mysqlInstance.dbPath);
				dir.delete();
			}

		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

    private Connection getConnection() throws SQLException {

	    loadDriver();

	    //if(mysqlInstance.dbPath == null){

	    	//mysqlInstance.dbPath= System.getProperty("user.home") + "/.Toolbox" ;

	    	if (!(new File(mysqlInstance.dbPath)).exists()){
	    			boolean success = (new File(mysqlInstance.dbPath)).mkdir();
	    			if(!success)
	    				return null;
	    	}

	    //}
		//String url = mysqlInstance.protocol + "/" + mysqlInstance.dbPath +"/"+mysqlInstance.dbName+"?"+other;
        String other = "useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
        conn = DriverManager.getConnection( mysqlInstance.protocol + "//" + mysqlInstance.dbPath +"/" +mysqlInstance.dbName+"?"+ other, username, password );
	    conn.setAutoCommit(false);

		return conn;
	}

    private void loadDriver() throws SQLException {
        try {
        	//System.loadLibrary("libsqlite_jni");
            Class.forName(mysqlInstance.driver);

        } catch (ClassNotFoundException cnfe) {
            System.err.println("\nUnable to load the JDBC driver ");
            System.err.println("Please check your CLASSPATH.");
            cnfe.printStackTrace(System.err);
        }
    }
}
