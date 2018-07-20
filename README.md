# UDToolboxExtension
An extended version of UDT Anonymization toolbox for supporting more than one database adapters. Original toolbox can be found in link [1]

Usage:

UTD Anoymization toolbox requires java run time environment(JRE).

1. Open a terminal and change directory to the toolbox main directory
2. Run the jar file (udttoolboxextension.jar)  
java -jar udttoolboxextension.jar 
3. A sample config.xml file is provided in the toolbox main directory (config.xml) which represents a configuration for applying Mondrian anamoymization method with k=10 and wrapper=postgres, on location-based dataset that is provided in the "dataset" sudirectory 
of the toolbox directory). The result will be generated in in the "datasetOutput" sudirectory.
4. Database configurations for now are hardcoded but soon will be included as .properties files. Therefore, following configurations are required for each database

#MySQL
mysql_dbpassword=abcd1234
mysql_database=localhost:3306
mysql_dbuser=root
mysql_dbname=TOOLBOX

#PostgreSQL
postgres_dbpassword=abcd1234
postgres_database=localhost:5432
postgres_dbuser=postgres
posthres_dbname=postgres

#SQLite
sqlite_dbpassword=
sqlite_database= database path
sqlite_dbuser=
sqlite_dbname=toolbox.db

1. University of Texas at Dallas. Anonymization toolbox.
http://cs.utdallas.edu/dspl/cgi-bin/toolbox/index.php
