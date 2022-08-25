package com.mck.rp.utilities;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class DataBaseUtil {
        private String connString;
        private Connection conn;

        private Connection getConnection() throws ClassNotFoundException, SQLException {

            conn = DriverManager.getConnection(connString);
            return conn;
        }

        private Statement getStatement() throws ClassNotFoundException, SQLException{

            return getConnection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
        }

        public DataBaseUtil setConnectionString(String connString) {

            this.connString = connString;
            return this;
        }


        public int executeUpdate(String sqlQuery) throws ClassNotFoundException, SQLException {

            return getStatement().executeUpdate(sqlQuery);
        }

        public LinkedHashMap<Integer, LinkedHashMap<String, Object>> executedbQuery(String sqlQuery) throws ClassNotFoundException, SQLException {

            ResultSet result = getStatement().executeQuery(sqlQuery);
            String[] columnName = getColumnName(result);

            LinkedHashMap<Integer, LinkedHashMap<String, Object>> dbData = new LinkedHashMap<>();
            int rowCounter = 1;
            while(result.next()){
                dbData.put(rowCounter, getDbData(columnName,result));
                rowCounter++;
            }
            return dbData;
        }

        private LinkedHashMap<String, Object> getDbData(String[] columnName, ResultSet result) throws SQLException {

            LinkedHashMap<String, Object> columnData = new LinkedHashMap<>();

            for(int i = 0; i < columnName.length; i++){
                columnData.put(columnName[i], getColumnData(i,result));
            }

            return columnData;

        }

        private Object getColumnData(int i, ResultSet result) throws SQLException {

            return result.getObject(i+1);
        }


        private String[] getColumnName(ResultSet result) throws SQLException {

            ResultSetMetaData data = result.getMetaData();
            String[] columnName = new String[data.getColumnCount()];

            for(int i = 1; i <= data.getColumnCount(); i++){
                columnName[i-1] = data.getColumnName(i);
            }
            return columnName;
        }

        //use this method when using DataProvider
        public Object[][] getTestDataFromDB(String sqlQuery) throws ClassNotFoundException, SQLException {

            ResultSet rs = getStatement().executeQuery(sqlQuery);

            int colCount = rs.getMetaData().getColumnCount();
            rs.last();
            int rowCount = rs.getRow();
            rs.beforeFirst();

            Object[][] data = new Object[rowCount][colCount];
            int row = 0;
            while (rs.next()) {
                for (int i = 0; i < colCount; i++) {
                    data[row][i] = rs.getObject(i+1);
                }
                row++;
            }
            return data;

        }

        //Return result set as list of map
        public List<Map<String, Object>> getTableDataList(String query) throws SQLException, ClassNotFoundException {

            ResultSet set = getStatement().executeQuery(query);
            ResultSetMetaData metaData = set.getMetaData();
            List<Map<String, Object>> data = new LinkedList<>();

            while(set.next()){
                Map<String, Object> tableData = new LinkedHashMap<>();
                for (int i = 1; i <= metaData.getColumnCount(); i++) {
                    tableData.put(metaData.getColumnLabel(i), set.getObject(i));
                }
                data.add(tableData);
            }
            return data;
        }

        //method to return resultset as map
        public Map<String, Object> getTableData(String query) throws SQLException, ClassNotFoundException {

            ResultSet set = getStatement().executeQuery(query);
            ResultSetMetaData metaData = set.getMetaData();
            Map<String, Object> tableData = new LinkedHashMap<>();

            while(set.next()){
                for (int i = 1; i <= metaData.getColumnCount(); i++) {
                    tableData.put(metaData.getColumnLabel(i), set.getObject(i));
                }
            }
            return tableData;
        }

        //Record count
        public int resultSetCount(String query) throws SQLException, ClassNotFoundException {

            ResultSet count = getStatement().executeQuery(query);
            count.next();
            return count.getInt(1);
        }

        public void close() throws SQLException {

            conn.close();

        }
}
