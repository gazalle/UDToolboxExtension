package anonymizer;


import sqlwrapper.SQLWrapper;
import sqlwrapper.SqLiteSQLWrapper;
import sqlwrapper.MySQLWrapper;
import sqlwrapper.PostgreSQLWrapper;

import java.io.FileInputStream;
import java.util.Properties;

/**
 * The class has been added to manage database instantiation according to database that has been specified in configuration file.
 * All database instantiation and conectivity check  (e.g., MySQL, SQLlite, PostgreSQL) are implemented
 * within this class.
 */
public class DBSelector {

    String configName="config.xml";
    Configuration conf =new Configuration(configName);
    /** Embedded database connection object */
    protected SQLWrapper sqlwrapper;

    private String masterdb;
    private String mastertable;
    private String type;

    Properties prop = new Properties();
    FileInputStream input=null;

    private String dbPath;
    private String dbName;
    private String dbUser;
    private String dbPassword;

    public DBSelector() throws Exception {

    }

    public SQLWrapper getDB() throws Exception{
        input = new FileInputStream("dbConfig.properties");
        prop.load(input);

        if(conf.dbWrapper==Configuration.MYSQL_WRAPPER){
            sqlwrapper = MySQLWrapper.getInstance();
            masterdb="INFORMATION_SCHEMA.TABLES";
            mastertable="TABLE_NAME";
            type="";

            dbName = prop.getProperty("mysql_dbname");
            dbPath = prop.getProperty("mysql_database");
            dbUser = prop.getProperty("mysql_dbuser");
            dbPassword = prop.getProperty("mysql_dbpassword");
      }
        else if(conf.dbWrapper==Configuration.PostgreSQL_WRAPPER){
            sqlwrapper = PostgreSQLWrapper.getInstance();
            masterdb="INFORMATION_SCHEMA.TABLES";
            mastertable="TABLE_NAME";
            type="PRECISION";

            dbName = prop.getProperty("postgres_dbname");
            dbPath = prop.getProperty("postgres_database");
            dbUser = prop.getProperty("postgres_dbuser");
            dbPassword = prop.getProperty("postgres_dbpassword");
        }
        else {
            sqlwrapper = SqLiteSQLWrapper.getInstance();
            masterdb="SQLITE_MASTER";
            mastertable="NAME";

            dbName = prop.getProperty("sqlite_dbname");
            dbPath = prop.getProperty("sqlite_database");
            dbUser = prop.getProperty("sqlite_dbuser");
            dbPassword = prop.getProperty("sqlite_dbpassword");
        }

        return sqlwrapper;
    }

    public String getMasterdb(){
        return masterdb;
    }
    public String getMastertable(){
        return mastertable;
    }
    public String getType(){
        return type;
    }

    public String getdbPath(){ return dbPath; }
    public String getdbName(){ return dbName; }
    public String getdbUser(){ return dbUser; }
    public String getdbPassword(){ return dbPassword; }
}
