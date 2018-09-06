package com.util;

import java.io.File;
import java.io.FileWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;

import org.json.simple.JSONObject;

import com.util.HttpClient;

public class CsvToJson {

    private static final String FILE_DERECTORY_PATH = "/Users/SaranSoppa/pc-project/src/resource/";
    private static final String OUTPUT_FILE_NAME = "/Users/SaranSoppa/pc-project/src/resource/output.json";
    private static final String AWS_ES_ENDPOINT = "https://search-pc-domain-dtmuia33nuk4lskeequrpnizsu.us-east-2.es.amazonaws.com/plans/plan/";
    
    public static void main(String[] args) {
    	try {	
    		CsvToJson csvObj = new CsvToJson();
    		//csvObj.writeDataIntoFile();
    		csvObj.writeDataIntoESDomian();

    	} catch(Exception exp){
    		exp.printStackTrace();
    	}
    }

    private Connection getConnection(String fileDirectoryPath){
    	Connection conn = null;
    	try {	
    		Class.forName("org.relique.jdbc.csv.CsvDriver");

    		// Create a connection. The first command line parameter is the directory containing the .csv files.
    		// A single connection is thread-safe for use by several threads.
    		conn = DriverManager.getConnection("jdbc:relique:csv:" + fileDirectoryPath );

    	} catch(Exception exp){
    		exp.printStackTrace();
    	} 
      return conn;
    }
    
    
    @SuppressWarnings("unchecked")
    private void writeDataIntoESDomian(){

    	try {
    		//Provide the location of the destination file
    		File outFile = new File(OUTPUT_FILE_NAME);
    		FileWriter writer = new FileWriter(outFile, true);

    		Connection conn = getConnection(FILE_DERECTORY_PATH);

    		// Create a Statement object to execute the query with.
    		Statement stmt = conn.createStatement();
    		String query= "SELECT * FROM csvdata";	//csvdata is the source file

    		// Select columns from csvdata.csv
    		ResultSet results = stmt.executeQuery(query);

    		ResultSetMetaData metadata = results.getMetaData();
    		int numColumns = metadata.getColumnCount();
    		System.out.println("numColumns :"+ numColumns);
    		int count=1;
    		while(results.next())  //iterate rows
    		{
    			JSONObject obj = new JSONObject();      
    			for (int i = 1; i <= numColumns; ++i)    //iterate columns
    			{
    				String column_name = metadata.getColumnName(i);
    				obj.put(column_name, results.getObject(column_name).toString());
    			}

    			//writing to aws elasticsearch domain
    			boolean isSuccess = HttpClient.triggerPut(AWS_ES_ENDPOINT + count, obj.toString());
    			if(isSuccess) {
    				// writing to out file
    				writer.write("{ \"index\" : { \"_index\": \"plans\", \"_type\" : \"plan\", \"_id\" : \""+count+"\" } }\n");
    				writer.write(obj.toString());
    				writer.write("\n");

    				count++;
    			}
    			if(count > 500)
    				break;
    		}

    		writer.close();	
    		conn.close();

    	} catch(Exception exp){
    		exp.printStackTrace();
    	}
    }
    
    @SuppressWarnings("unchecked")
    private void writeDataIntoFile(){

    	try {
    		//Provide the location of the destination file
    		File outFile = new File(OUTPUT_FILE_NAME);
    		FileWriter writer = new FileWriter(outFile, true);

    		Connection conn = getConnection(FILE_DERECTORY_PATH);

    		// Create a Statement object to execute the query with.
    		Statement stmt = conn.createStatement();
    		String query= "SELECT * FROM csvdata";	//csvdata is the source file

    		// Select columns from csvdata.csv
    		ResultSet results = stmt.executeQuery(query);

    		ResultSetMetaData metadata = results.getMetaData();
    		int numColumns = metadata.getColumnCount();
    		System.out.println("numColumns :"+ numColumns);
    		int count=1;
    		while(results.next())  //iterate rows
    		{
    			JSONObject obj = new JSONObject();      
    			for (int i = 1; i <= numColumns; ++i)    //iterate columns
    			{
    				String column_name = metadata.getColumnName(i);
    				obj.put(column_name, results.getObject(column_name).toString());
    			}

    				// writing to out file
    				writer.write("{ \"index\" : { \"_index\": \"plans\", \"_type\" : \"plan\", \"_id\" : \""+count+"\" } }\n");
    				writer.write(obj.toString());
    				writer.write("\n");

    				count++;
    		}

    	  writer.close();
    	  conn.close();
    	} catch(Exception exp){
    		exp.printStackTrace();
    	}
    	
    }
}
