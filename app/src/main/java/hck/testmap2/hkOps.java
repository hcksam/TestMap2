package hck.testmap2;

import jsqlite.Database;
import jsqlite.Exception;
import jsqlite.Stmt;

public class hkOps {

	public static String geoex_03_101(Database db) throws Exception {
		StringBuilder sb = new StringBuilder();
		String strSQL = "";
		
		strSQL = "SELECT CODE, NAME_CT, NAME_CS FROM districts ORDER BY CODE ASC LIMIT 3;";
		Stmt stmtSQL = db.prepare(strSQL);
		while (stmtSQL.step()) {
			String codeStr = stmtSQL.column_string(0);
			String nameCtStr = stmtSQL.column_string(1);
			String nameCsStr = stmtSQL.column_string(2);
			sb.append(codeStr).append("|").append(nameCtStr).append("|").append(nameCsStr).append("\n");
		}
		stmtSQL.close();
		
		return sb.toString();
	}

	public static String geoex_03_103(Database db) throws Exception {
		StringBuilder sb = new StringBuilder();
		String strSql = "";
		
		strSql = "SELECT CODE,t2.NAME_CT, AsText(t2.geom) FROM districts t1,education t2 WHERE (CODE=?) AND Contains(t1.geom,t2.geom) ORDER BY t2.NAME_EN LIMIT 7;";
		Stmt stmtSql = db.prepare(strSql);
		stmtSql.bind(1, "C");
		
		while (stmtSql.step()) {
			//
			String codeStr = stmtSql.column_string(0);
			String nameCtStr = stmtSql.column_string(1);
			String geomStr = stmtSql.column_string(2);
			sb.append(codeStr).append("|").append(nameCtStr).append("|").append(geomStr).append("\n");
		}
		stmtSql.close();
		
		return sb.toString();
	}
	
	public static String geoex_03_203(Database db) throws Exception {
		//
		StringBuilder sb = new StringBuilder();
		String strSql = "";
		
		strSql = "SELECT CODE,t1.NAME_CT,count(*) FROM districts t1,education t2 WHERE Contains(t1.geom,t2.geom) GROUP BY CODE HAVING count(*) < 50 ORDER BY count(*);";
		Stmt stmtSql = db.prepare(strSql);
		
		while (stmtSql.step()) {
			String codeStr = stmtSql.column_string(0);
			String nameCtStr = stmtSql.column_string(1);
			int countInt = stmtSql.column_int(2);
			sb.append(codeStr).append("|").append(nameCtStr).append("|").append(countInt).append("\n");
		}
		stmtSql.close();
		
		return sb.toString();
	}
}
