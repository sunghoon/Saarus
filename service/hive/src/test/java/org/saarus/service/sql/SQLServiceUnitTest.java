package org.saarus.service.sql;

import java.sql.ResultSet;
import java.util.HashMap;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.junit.Test;
import org.saarus.service.hadoop.HadoopInfo;
import org.saarus.service.hadoop.util.HDFSUtil;
import org.saarus.service.sql.SQLService;
import org.saarus.service.sql.io.RCFileWriter;

public class SQLServiceUnitTest {
  @Test
  public void testClient() throws Exception {
    SQLService hservice  = new SQLService(HadoopInfo.HIVE_CONNECTION_URL, "hive", "");
    String tableName = "testtable";
    String location = "/tmp/testdb/testtable" ;
    System.out.println(hservice.dropTable(tableName)) ;
    hservice.executeSQL(String.format("CREATE EXTERNAL TABLE %s (key INT, value STRING) STORED AS RCFILE LOCATION '%s'", tableName, location));
    Configuration conf = HDFSUtil.getConfiguration() ;
    FileSystem fs = FileSystem.get(conf) ;
    RCFileWriter writer = new RCFileWriter(fs, location + "/data0.rcfile", 2, new HashMap<String, String>()) ;
    for(int i = 0; i < 100; i++) {
      writer.append(Integer.toString(i), "value " + i) ;
    }
    writer.close() ;
    
    writer = new RCFileWriter(fs, location + "/data1.rcfile", 2, new HashMap<String, String>()) ;
    for(int i = 100; i < 200; i++) {
      writer.append(Integer.toString(i), "value " + i) ;
    }
    writer.close() ;
    
    
    ResultSet rset = hservice.executeQuerySQL(String.format("SELECT * FROM %s", tableName)) ;
    while(rset.next()) {
      System.out.println(String.format("key = %s, value = %s", rset.getObject(1), rset.getObject(2)));
    }
  }
}