package dk.headnet;

import java.util.ArrayList;
import java.util.List;
import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class JDBCTest {
	private static Connection dbconn;
	private static boolean create=false;
	private static PreparedStatement selectAffID;
	
	public static void main(String[] args) throws SQLException {
		dbconn = getDBConnection("ilsync");
		createTables(dbconn);
		selectAffID = dbconn.prepareStatement("select id from affiliation where instno = ? and classname = ?");

		//dropTables();
		getAffID("240380", "1A");
		getAffID("240380", "1B");
		getAffID("240071", "9B");
		//insertPerson(dbconn);
		
		selectStuff(dbconn);
	}

	private static void insertPerson(Connection dbconn) throws SQLException {
		List<String> transaction = new ArrayList<>();
		
		transaction.add("INSERT INTO PERSON"
				+ "(ID, USERID, EMAIL) " + "VALUES"
				+ "(1,'mine','mine@headnet.dk')");
	 
		transaction.add("INSERT INTO AFFILIATION (ID,INSTNO,CLASSNAME) "
				+ " VALUES (1,'240380', '1A')");
		transaction.add("INSERT INTO AFFILIATION (ID,INSTNO,CLASSNAME) "
				+ " VALUES (2,'240380', '1B')");
		
		transaction.add("INSERT INTO PERSON_AFFILIATION (ID,PERSON_ID,AFFILIATION_ID) "
				+ " VALUES (1,1,1)");
		transaction.add("INSERT INTO PERSON_AFFILIATION (ID,PERSON_ID,AFFILIATION_ID) "
				+ " VALUES (2,1,2)");
		
		
 		executeTransaction(transaction);
 	}
	
	private static void getAffID(String instno, String classname) throws SQLException {
		selectAffID.setString(1, instno);
		selectAffID.setString(2, classname);
		ResultSet rs = selectAffID.executeQuery();
		
		if (rs != null && rs.next()) {
			System.out.println(instno + "/" + classname + " => " + rs.getInt("ID"));
		} else if (rs != null) {
			System.out.println(instno + "/" + classname + " => NOT FOUND");
		} else {
			System.out.println(instno + "/" + classname + " => ID NULL");
		}
		
		
	}
	
	private static void selectStuff(Connection dbconn) throws SQLException {
		String select = "SELECT USERID,EMAIL,INSTNO,CLASSNAME from "
				+ " PERSON,AFFILIATION,PERSON_AFFILIATION "
				+ " where person.id = PERSON_AFFILIATION.person_id AND "
				+ " affiliation.id = person_affiliation.affiliation_id";
		Statement statement = dbconn.createStatement();
		ResultSet rs = statement.executeQuery(select);
		while (rs.next()) {
			System.out.println(rs.getString(1) + ", " + rs.getString(2) + ", " + rs.getString(3) + ", " + rs.getString(4));
		}

		/*String selecttbl = "SELECT TABLETYPE, TABLENAME from SYS.SYSTABLES";
		//Statement statement = dbconn.createStatement();
		rs = statement.executeQuery(selecttbl);
		while (rs.next()) {
			if (rs.getString("TABLETYPE").equalsIgnoreCase("T"))
				System.out.println(rs.getString("TABLETYPE") + " " + rs.getString("TABLENAME"));
			
		}*/
	
	}

	private static void createTables(Connection dbconn) throws SQLException {
		String createPersonTable = "CREATE TABLE IF NOT EXISTS PERSON("
				+ "ID INT NOT NULL, "
				+ "USERID VARCHAR(20) NOT NULL, "
				+ "EMAIL VARCHAR(60) NOT NULL, "
				+ "PRIMARY KEY (ID)"
				+ ")";
 
		String createAffiliationTable = "CREATE TABLE IF NOT EXISTS AFFILIATION("
				+ "ID INT NOT NULL, "
				+ "INSTNO VARCHAR(10) NOT NULL, "
				+ "CLASSNAME VARCHAR(30) NOT NULL, "
				+ "PRIMARY KEY (ID),"
				+ "UNIQUE(INSTNO,CLASSNAME) "
				+ ")";
 
		String createPerson_AffiliationTable = "CREATE TABLE IF NOT EXISTS PERSON_AFFILIATION("
				+ "ID INT NOT NULL, "
				+ "PERSON_ID INT NOT NULL, "
				+ "AFFILIATION_ID INT NOT NULL, "
				+ "PRIMARY KEY (ID),"
				+ " UNIQUE(PERSON_ID,AFFILIATION_ID) "
				+ ")";
 
 		executeStatement(createPersonTable);
 		executeStatement(createAffiliationTable);
 		executeStatement(createPerson_AffiliationTable); 
	}
	
/*	THESE USE SYNTAX THAT MySQL DOES NOT ACCEPT.
    private static void createTables(Connection dbconn) throws SQLException {
		String createPersonTable = "CREATE TABLE PERSON("
				+ "ID INT NOT NULL CONSTRAINT person_pk PRIMARY KEY, "
				+ "USERID VARCHAR(20) NOT NULL, "
				+ "EMAIL VARCHAR(60) NOT NULL "
				+ ")";
 
		String createAffiliationTable = "CREATE TABLE AFFILIATION("
				+ "ID INT NOT NULL CONSTRAINT affiliation_pk PRIMARY KEY, "
				+ "INSTNO VARCHAR(10) NOT NULL, "
				+ "CLASSNAME VARCHAR(30) NOT NULL, "
				+ "UNIQUE(INSTNO,CLASSNAME) "
				+ ")";
 
		String createPerson_AffiliationTable = "CREATE TABLE PERSON_AFFILIATION("
				+ "ID INT NOT NULL CONSTRAINT person_affiliation_pk PRIMARY KEY, "
				+ "PERSON_ID INT NOT NULL CONSTRAINT person_fk REFERENCES PERSON, "
				+ "AFFILIATION_ID INT NOT NULL CONSTRAINT aff_fk REFERENCES AFFILIATION, "
				+ " UNIQUE(PERSON_ID,AFFILIATION_ID) "
				+ ")";
 
 		executeStatement(createPersonTable);
 		executeStatement(createAffiliationTable);
 		executeStatement(createPerson_AffiliationTable); 
	} */
	
	private static void dropTables() throws SQLException {
		executeStatement("drop table person_affiliation");
		executeStatement("drop table person");
		executeStatement("drop table affiliation");
	}

	private static void executeStatement(String query) throws SQLException {
		Statement statement=null;
		
		try {
			statement = dbconn.createStatement();
 			statement.execute(query);
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} finally {
			if (statement != null) {
				statement.close();
			}
		}
	}
	
	private static void executeTransaction(List<String> transaction) throws SQLException {
		Statement statement=null;
		
		try {
			dbconn.setAutoCommit(false);
			for (String stmt : transaction) {
				statement = dbconn.createStatement();
				statement.executeUpdate(stmt);
			}
			dbconn.commit();
		} catch (SQLException e) {
			dbconn.rollback();
			System.out.println("Transaction Failed! Check output console");
			e.printStackTrace();
		} finally {
			if (statement != null) {
				statement.close();
			}
		}
	}
	
	
	private static Connection getDBConnection(String dbname) {
		String driver = "com.mysql.jdbc.Driver";
		// String driver = "org.apache.derby.jdbc.EmbeddedDriver";
		// String driver = "org.postgresql.Driver";

		try {
			Class.forName(driver);
		} catch (ClassNotFoundException e) {
			System.out.println("Where is your Derby JDBC Driver?");
			e.printStackTrace();
			return null;
		}
	 
		System.out.println("Derby JDBC Driver Registered!");
		Connection connection = null;
	 
		try {
			//connection = DriverManager.getConnection("jdbc:derby:" + dbname + ";create=true;user=???;password=???");
			connection = DriverManager.getConnection("jdbc:mysql://localhost/" + dbname, "root", "???");
		} catch (SQLException e) {
			System.out.println("Connection Failed! Check output console");
			e.printStackTrace();
		}
		
		return connection;
	}
}
