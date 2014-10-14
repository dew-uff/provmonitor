package br.uff.ic.provmonitor;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class JavaDBConnection {
	public static void main(String[] args) {
		try {
			String driver = "org.apache.derby.jdbc.EmbeddedDriver";
			//String driver = "org.apache.derby.jdbc.ClientDriver";
			
			String dbName="jdbcDemoDB";
			String connectionURL = "jdbc:derby:" + dbName + ";create=true";
			//String connectionURL = "jdbc:derby://localhost:1527/" + dbName + ";create=true";
			
			String createString = "CREATE TABLE WISH_LIST  "
					  +  "(WISH_ID INT NOT NULL GENERATED ALWAYS AS IDENTITY, " 
					  +  " WISH_ITEM VARCHAR(32) NOT NULL) " ;
			
			Class.forName(driver);
			Connection conn = DriverManager.getConnection(connectionURL);
			
			 
			Statement s = conn.createStatement();
			 
			System.out.println (" . . . . creating table WISH_LIST");
			s.execute(createString);
			 
//			if (! WwdUtils.wwdChk4Table(conn))
//			{  
//			   System.out.println (" . . . . creating table WISH_LIST");
//			   s.execute(createString);
//			}
			
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
}
