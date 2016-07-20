import java.io.FileWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class Forecast {
	public static void main(String[] args) throws Exception {
		  /*create 'CreateDatabase' object to create database, database table and load csv file into database table.
		  to read database table to make some calculations and to write results into csv files.*/
		long startTime = System.currentTimeMillis();
		new Forecast().createDataBase();
		long endTime   = System.currentTimeMillis();
		long totalTime = endTime - startTime;
		System.out.println(totalTime/1000+" seconds");
	  }
	
  private Connection connect = null;
  private Statement statement = null;
  private ResultSet resultSet = null;
  private ResultSet resultSet2 = null;
  @SuppressWarnings("unused")
  private int createDB;
  @SuppressWarnings("unused")
  private int createTable;
  @SuppressWarnings("unused")
  private int insertCsvToSql;
  @SuppressWarnings("unused")
  private int dtable;
  private ResultSet welist;
  String dbName="interview_data_2MonthSalesData";
  String tableName = "DATA_TABLE";
  String filepath = "'D:/project/interview_data_2MonthSalesData.csv'";
  String user = "root";
  String password = "6711461";
  ArrayList<Integer> wlist =new ArrayList<>();
  public double wlistsize;
  String csvOutputFilePathForQ1andQ2="D:/project/OutputForQ1andQ2.csv";
  String csvOutputFilePathforQ3="D:/project/OutputForQ3.csv";
  public double sdeviation;
  
  public void createDataBase() throws Exception {
    try {
      // This will load the MySQL driver, each DB has its own driver
      Class.forName("com.mysql.jdbc.Driver");
      // Setup the connection with the DB
      connect = DriverManager.getConnection("jdbc:mysql://localhost/?" + "user="+user+"&password="+password+"&useSSL=false");

      // Statements allow to issue SQL queries to the database
      statement = connect.createStatement();
      
      // Database created if it is not existed. If it is already existed there will be no changes according to query
      String dbcreation = "CREATE DATABASE IF NOT EXISTS "+dbName;
      createDB = statement.executeUpdate(dbcreation);
      System.out.println("Database created.");
	  
      //it helps us to load data successfully into table every run
      String dropTable = "DROP TABLE IF EXISTS "+dbName+"."+tableName;
      dtable = statement.executeUpdate(dropTable);
      //System.out.println("Table deleted.");
	  
      //table created if it is not existed. If it is already existed there will be no changes according to query
      String tablecreation = "CREATE TABLE IF NOT EXISTS "+dbName+"."+tableName
      			+ "( "
        		+ "Product VARCHAR(50) NULL,"
          		+ "Store VARCHAR(50) NULL,"
          		+ "Date DATE NULL,"
          		+ "SalesQuantity INT NULL"
          		+ ")";
      createTable = statement.executeUpdate(tablecreation);
      System.out.println("Table created.");
      
      //load csv to database
      String csvtoMysql = "LOAD DATA LOCAL INFILE "
				+ filepath
				+ " IGNORE INTO TABLE "
				+ dbName+"."+tableName
				+ " FIELDS TERMINATED BY ',' "
				+ " LINES TERMINATED BY '\n' "
				+ " IGNORE 1 LINES "
				+ " (Product,Store, Date, SalesQuantity);";
      insertCsvToSql = statement.executeUpdate(csvtoMysql);
      System.out.println("csv file added to database.");
      
      //calculate number of week in that database.
      String weeklist = "select distinct week("+tableName+"."+"Date) weeknumber "
      		+ " from "+dbName+"."+tableName
      		+ " order by weeknumber ";
      welist = statement.executeQuery(weeklist);
      
      while (welist.next()) {      
    	   wlist.add(welist.getInt("weeknumber"));                                 
      }      
	  wlistsize = wlist.size();
	  //System.out.println("week list size : "+wlistsize);
	 
	  // this query gives use the results of question 1 and question 2
      resultSet = statement.executeQuery("SELECT WEEK(Date) WeekNumber,Store,Product,SalesQuantity,sum(SalesQuantity) as NumberofItemSoldPerWeek "
      		+ ",CONCAT(DATE_FORMAT(DATE_ADD(Date, INTERVAL(1-DAYOFWEEK(Date)) DAY),'%Y-%m-%e'), "
      		+ "' TO ',DATE_FORMAT(DATE_ADD(Date, INTERVAL(7-DAYOFWEEK(Date)) DAY),'%Y-%m-%e')) AS StartDateOfWeek "
      		+ "FROM "
      		+ dbName+"."+tableName
      		+ " group by Product "
      		+ " Order by NumberofItemSoldPerWeek desc");
      System.out.println("database has been read");
      //it exports the results of resultSet to csv file. 
      writeResultSet(resultSet);
      System.out.println("csv file for q1 and q2 have been created successfully");
      //it gives results of question3.
      resultSet2 = statement.executeQuery("Select Product,Store,sum(SalesQuantity) as EstimatedSales "
        		+ "from"
          		+ "("
          		+ "SELECT Product,Store,TIMESTAMPDIFF(week, '2016-01-01', date) AS WeekNumber,SalesQuantity "
          		+ "FROM "+dbName+"."+tableName +" LIMIT 40000) as tb "
          		+ "GROUP BY Product,WeekNumber,tb.Store");
      //it exports the results of resultSet2 to csv file. 
      writeResultSet2(resultSet2);
      System.out.println("csv file for q3 has been created successfully"); 
	  
    } catch (Exception e) {
      throw e;
    } finally {
      close();
    }
  }
  //this method exports the results of q1 and q2
  private void writeResultSet(ResultSet resultSet) throws SQLException {
	  double average = 0;
	  // ResultSet is initially before the first data set
	  try {

		  FileWriter writer = new FileWriter(csvOutputFilePathForQ1andQ2);
			 
		    writer.append("Product");
		    writer.append(',');
		    writer.append("TotalSales");
		    writer.append(',');
		    writer.append("AverageWeeklySales");
		    writer.append(',');
		    writer.append("WeeklyStandardDeviation");
		    writer.append('\n');
		
		    while (resultSet.next()) {
		    	String product = resultSet.getString("Product");
		        double nis = resultSet.getInt("NumberofItemSoldPerWeek");
		        double avperweek= nis/wlistsize;
		        String avperweektostr = String.format("%.2f", avperweek);
		        sdeviation = Math.sqrt((1/wlistsize)*(avperweek-average)*(avperweek-average));
		        String sdeviationtostr= String.format("%.2f", sdeviation);
		        writer.append(product);
		        writer.append(',');
		        writer.append(new Double(nis).toString());
		        writer.append(',');
		        writer.append(avperweektostr);
		        writer.append(',');
		        writer.append(sdeviationtostr);
		        writer.append(',');
		        writer.append("\n");
		    }
	        writer.flush();
		    writer.close();
	} catch (Exception e) {
		// TODO: handle exception
	}
  }
  //this method exports the results of question3
  private void writeResultSet2(ResultSet resultSet) throws SQLException {
	  // ResultSet is initially before the first data set
	  try {

		  FileWriter writer = new FileWriter(csvOutputFilePathforQ3);
			 
		    writer.append("Product");
		    writer.append(',');
		    writer.append("Store");
		    writer.append(',');
		    writer.append("EstimatedSales");
		    writer.append('\n');
		
		    while (resultSet.next()) {
		    	String product = resultSet.getString("Product");
		    	String store = resultSet.getString("Store");
		    	int esales = resultSet.getInt("EstimatedSales"); 
		    	writer.append(product);
		        writer.append(',');
		        writer.append(store);
		        writer.append(',');
		        writer.append(new Integer(esales).toString());
		        writer.append("\n");
		    }
	        writer.flush();
		    writer.close();
	} catch (Exception e) {
		// TODO: handle exception
	}
  }
  
	// You need to close the resultSet
	private void close() {
	    try {
	    	if (resultSet != null) {
	    		resultSet.close();
	        }
	    	if (statement != null) {
	    		statement.close();
	    	}
	    	if (connect != null) {
	    		connect.close();
	    	}
	    } catch (Exception e) {
	
	    }
	}  
}