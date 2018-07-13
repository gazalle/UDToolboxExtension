package sqlwrapper;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Implementation of the SQLWrapper interface for SQLite embedded database.
 *
 */

public class PostgreSQLWrapper implements SQLWrapper {

	private final String driver = "org.postgresql.Driver";
	private final String protocol = "jdbc:postgresql:";
	private final String dbName = "postgres";
	private String dbPath = "localhost:5432";
//	private String other="useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";

	private Connection conn = null;

	/**
	 * Single instance created upon class loading.
	 */

	private static final PostgreSQLWrapper postgresInstance = new PostgreSQLWrapper();

    public PostgreSQLWrapper() {
    }

	/**
	 * Returns the singleton sqLite instance
	 * @return postgresInstance
	 */
    public static PostgreSQLWrapper getInstance() {

    	try{
    		if(postgresInstance.conn == null || postgresInstance.conn.isClosed())
    			postgresInstance.conn = postgresInstance.getConnection();

    	}catch(Exception ex){
    		ex.printStackTrace();
    		return null;
    	}

        return postgresInstance;
     }

	/**
	 * Execute SQL statement for data definition and manipulation
	 * @param sql Sql operation
	 * @return success of executed operation
	 */

	public boolean execute(String sql) {
		Statement st = null;
		try {
			st =postgresInstance.conn.createStatement();
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
			Statement st =postgresInstance.conn.createStatement();
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

			String dbPath = postgresInstance.dbPath +"/" +postgresInstance.dbName;
			File file = new File(dbPath);
			boolean succ = file.delete();

			if(succ){
				File dir = new File(postgresInstance.dbPath);
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

	    //if(postgresInstance.dbPath == null){

	    	//postgresInstance.dbPath= System.getProperty("user.home") + "/.Toolbox" ;

	    	if (!(new File(postgresInstance.dbPath)).exists()){
	    			boolean success = (new File(postgresInstance.dbPath)).mkdir();
	    			if(!success)
	    				return null;
	    	}

	    //}
		//String url = postgresInstance.protocol + "/" + postgresInstance.dbPath +"/"+postgresInstance.dbName+"?"+other;
		conn = DriverManager.getConnection( postgresInstance.protocol + "//" + postgresInstance.dbPath +"/" +postgresInstance.dbName, "postgres", "abcd1234" );
	    conn.setAutoCommit(false);

		return conn;
	}

    private void loadDriver() throws SQLException {
        try {
        	//System.loadLibrary("libsqlite_jni");
            Class.forName(postgresInstance.driver);

        } catch (ClassNotFoundException cnfe) {
            System.err.println("\nUnable to load the JDBC driver ");
            System.err.println("Please check your CLASSPATH.");
            cnfe.printStackTrace(System.err);
        }
    }
}
