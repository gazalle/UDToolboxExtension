package anonymizer;


import sqlwrapper.SQLWrapper;
import sqlwrapper.SqLiteSQLWrapper;
import sqlwrapper.MySQLWrapper;
import sqlwrapper.PostgreSQLWrapper;

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

    public DBSelector() throws Exception {

    }

    public SQLWrapper getDB() {
        if(conf.dbWrapper== Configuration.MYSQL_WRAPPER){
            sqlwrapper = MySQLWrapper.getInstance();
            masterdb="INFORMATION_SCHEMA.TABLES";
            mastertable="TABLE_NAME";
            type="";
      }
        else if(conf.dbWrapper== Configuration.PostgreSQL_WRAPPER){
            sqlwrapper = PostgreSQLWrapper.getInstance();
            masterdb="INFORMATION_SCHEMA.TABLES";
            mastertable="TABLE_NAME";
            type="PRECISION";
        }
        else {
            sqlwrapper = SqLiteSQLWrapper.getInstance();
            masterdb="SQLITE_MASTER";
            mastertable="NAME";
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

}
