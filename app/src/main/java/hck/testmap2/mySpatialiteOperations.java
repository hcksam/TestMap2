package hck.testmap2;

import jsqlite.Exception;
import jsqlite.Database;
import jsqlite.Stmt;

public class mySpatialiteOperations {
	
	public static void dropTable(Database db) throws Exception {
		String strSQL = "";
		strSQL = "DROP TABLE IF EXISTS myFirstSpatialTable;";
		db.exec(strSQL, null);
	}

	public static void createTable(Database db) throws Exception {
		String strSQL = "";
		strSQL = "CREATE TABLE IF NOT EXISTS myFirstSpatialTable (name TEXT NOT NULL, geom BLOB NOT NULL);";
		db.exec(strSQL,  null);
	}
	
	public static void insertRecords(Database db) throws Exception {
		String strSQL = "INSERT INTO myFirstSpatialTable (name,geom) VALUES (?,GeomFromText(?,2326));";
		Stmt stmtSQL = db.prepare(strSQL);
		stmtSQL.bind(1, "PolyU");
		stmtSQL.bind(2, "POINT(830000 824000)");
		stmtSQL.step();
		
		stmtSQL.clear_bindings();
		stmtSQL.reset();

		stmtSQL.bind(1, "LSGI");
		stmtSQL.bind(2, "POINT(845000 812000)");
		stmtSQL.step();

		stmtSQL.close();
	}
	
	public static int selectCount(Database db) throws Exception {
		int totalRecords;
		String strSQL = "SELECT count(*) FROM myFirstSpatialTable;";
		Stmt stmtSQL = db.prepare(strSQL);
		if (stmtSQL.step()) {
			totalRecords = stmtSQL.column_int(0);
		} else {
			totalRecords = 0;
		}
		stmtSQL.close();
		
		return totalRecords;
	}
	
	public static String showRecordAsText(Database db) throws Exception {
		//
		StringBuilder sb = new StringBuilder();
		String strSQL = "";
		strSQL = "SELECT name,AsText(geom) AS Geometry FROM myFirstSpatialTable;";
		Stmt stmtSQL = db.prepare(strSQL);
		while (stmtSQL.step()) {
			String nameStr = stmtSQL.column_string(0);
			String geomStr = stmtSQL.column_string(1);
			sb.append(nameStr).append("\t").append(geomStr).append("\n");
		}
		stmtSQL.close();
		
		return sb.toString();
	}
	
	public static String showDistance(Database db) throws Exception {
		StringBuilder sb = new StringBuilder();
		String strSQL = "";
		strSQL = "SELECT name,Distance(PointFromText(?,2326),t1.geom) FROM myFirstSpatialTable t1 WHERE name=? OR name=?;";
		Stmt stmtSQL = db.prepare(strSQL);
		stmtSQL.bind(1,"POINT(830000 812000)");
		stmtSQL.bind(2, "PolyU");
		stmtSQL.bind(3,"LSGI");
		while (stmtSQL.step()) {
			String nameStr = stmtSQL.column_string(0);
			double dist = stmtSQL.column_double(1);
			sb.append(nameStr).append("\t").append(dist).append("\n");
		}
		stmtSQL.close();
		
		return sb.toString();
	}
}
