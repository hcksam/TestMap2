package gs.spatialite.helpers;

import jsqlite.Database;
import jsqlite.Exception;
import jsqlite.Stmt;

public class Utilities {
	static public String queryVersions(Database db) throws Exception {
		Stmt stmt01;
		StringBuilder sb = new StringBuilder();
		sb.append("Check versions...\n");
		
		stmt01 = db.prepare("SELECT sqlite_version();");
		if (stmt01.step()) {
			sb.append("\t").append("SQLite version: " + stmt01.column_string(0));
			sb.append("\n");
		}
		
		stmt01 = db.prepare("SELECT spatialite_version();");
		if (stmt01.step()) {
			sb.append("\t").append("SpatiaLite version: " + stmt01.column_string(0));
			sb.append("\n");
		}
		
		stmt01 = db.prepare("SELECT proj4_version();");
		if (stmt01.step()) {
			sb.append("\t").append("PROJ4 version: " + stmt01.column_string(0));
			sb.append("\n");
		}

		stmt01 = db.prepare("SELECT geos_version();");
		if (stmt01.step()) {
			sb.append("\t").append("GEOS version: " + stmt01.column_string(0));
			sb.append("\n");
		}
		stmt01.close();
		sb.append("Done...\n");
		
		return sb.toString();
	}
}
