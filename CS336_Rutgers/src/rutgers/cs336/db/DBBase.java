package rutgers.cs336.db;

import rutgers.cs336.gui.Helper;
import rutgers.cs336.servlet.IConstant;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DBBase extends Utils implements ISQLConstant, IConstant {


	public static Connection getConnection() throws ClassNotFoundException, SQLException {
		// JDBC driver for MySQL. Latest: https://dev.mysql.com/downloads/connector/j/
		Class.forName("com.mysql.cj.jdbc.Driver");
		//
		Connection conn = DriverManager.getConnection(MySQL_URL, MySQL_USER_ID, MySQL_PASSWORD);
		//
		return conn;
	}

	public static String getUUID() {
		UUID uuid = UUID.randomUUID();
		return uuid.toString().replace("-", "");
	}


	public static        String                  OP_ANY                      = "any";
	//
	public static        String                  OP_SZ_EQUAL                 = "szequal";
	public static        String                  OP_SZ_EQUAL_MULTI_NO_ESCAPE = "szequalmultine";
	public static        String                  OP_SZ_NOT_EQUAL             = "sznotequal";
	public static        String                  OP_SZ_START_WITH            = "startwith";
	public static        String                  OP_SZ_CONTAIN               = "contain";
	//
	public static        String                  OP_INT_EQUAL                = "intequal";
	public static        String                  OP_INT_EQUAL_MULTI          = "intequalmulti";
	public static        String                  OP_INT_NOT_EQUAL            = "intnotequal";
	public static        String                  OP_INT_EQUAL_OR_OVER        = "equalorover";
	public static        String                  OP_INT_EQUAL_OR_UNDER       = "equalorunder";
	public static        String                  OP_INT_BETWEEN              = "between";
	//
	public static        String                  OP_BOOL_TRUE                = "true";
	public static        String                  OP_BOOL_FALSE               = "false";
	//
	private static final HashMap<String, String> sqlTokens;
	private static       Pattern                 sqlTokenPattern;

	static {
		// MySQL escape sequences: https://dev.mysql.com/doc/refman/8.0/en/string-literals.html
		String[][] search_regex_replacement = new String[][]
				  {	//   Search string       Search regex        SQL replacement regex
							 {   "\u0000"    ,       "\\x00"     ,       "\\\\0"     },
							 {   "'"         ,       "'"         ,       "\\\\'"     },
							 {   "\""        ,       "\""        ,       "\\\\\""    },
							 {   "\b"        ,       "\\x08"     ,       "\\\\b"     },
							 {   "\n"        ,       "\\n"       ,       "\\\\n"     },
							 {   "\r"        ,       "\\r"       ,       "\\\\r"     },
							 {   "\t"        ,       "\\t"       ,       "\\\\t"     },
							 {   "\u001A"    ,       "\\x1A"     ,       "\\\\Z"     },
							 {   "\\"        ,       "\\\\"      ,       "\\\\\\\\"  }
				  };
		//
		sqlTokens = new HashMap<>();
		StringBuilder patternStr = new StringBuilder();
		for (String[] srr : search_regex_replacement) {
			sqlTokens.put(srr[0], srr[2]);
			patternStr.append((patternStr.length() == 0) ? "" : "|").append(srr[1]);
		}
		sqlTokenPattern = Pattern.compile('(' + patternStr.toString() + ')');
	}


	public static String escape(String s) {
		Matcher      matcher = sqlTokenPattern.matcher(s);
		StringBuffer sb      = new StringBuffer();
		while (matcher.find()) {
			matcher.appendReplacement(sb, sqlTokens.get(matcher.group(1)));
		}
		matcher.appendTail(sb);
		return sb.toString();
	}


	public static String escapeToUpperCaseTrimNoNull(String input) {
		return escape((input == null ? "" : input.trim())).toUpperCase();
	}


	public static String toUpperCaseTrimNoNull(String input) {
		return (input == null ? "" : input.trim()).toUpperCase();
	}


	private static String oneCondition(String columnName, String op, String value, String valueAdd, boolean isCasting) {
		String output = "";
		//
		if (op.equals(OP_SZ_EQUAL) || op.equals(OP_SZ_EQUAL_MULTI_NO_ESCAPE) || op.equals(OP_SZ_NOT_EQUAL) || op.equals(OP_SZ_START_WITH) || op.equals(OP_SZ_CONTAIN)) {   // String
			if (op.equals(OP_SZ_EQUAL_MULTI_NO_ESCAPE)) {
				value = toUpperCaseTrimNoNull(value);
			}
			else {
				value = escapeToUpperCaseTrimNoNull(value);
			}
			//
			if (value.equals("")) {
				output = "";
			}
			else {
				columnName = "UPPER(" + columnName + ")";
				if (op.equals(OP_SZ_EQUAL)) {
					output = "(" + columnName + " = '" + value + "')";
				}
				else if (op.equals(OP_SZ_EQUAL_MULTI_NO_ESCAPE)) {               //no quote in input
					output = "(" + columnName + " in (" + value + "))";
				}
				else if (op.equals(OP_SZ_NOT_EQUAL)) {
					output = "(NOT " + columnName + " = '" + value + "')";
				}
				else if (op.equals(OP_SZ_START_WITH)) {
					output = "(" + columnName + " LIKE '" + value + "%')";
				}
				else if (op.equals(OP_SZ_CONTAIN)) {
					output = "(" + columnName + " LIKE '%" + value + "%')";
				}
			}
		}
		else if (op.equals(OP_INT_EQUAL) || op.equals(OP_INT_EQUAL_MULTI) || op.equals(OP_INT_NOT_EQUAL) || op.equals(OP_INT_EQUAL_OR_OVER) || op.equals(OP_INT_EQUAL_OR_UNDER) || op.equals(OP_INT_BETWEEN)) {      // Integer
			value = escapeToUpperCaseTrimNoNull(value);
			//
			if (value.equals("")) {
				output = "";
			}
			else {
				if (isCasting) {
					columnName = "CAST(" + columnName + " AS SIGNED)";
				}
				//
				if (op.equals(OP_INT_EQUAL)) {
					output = "(" + columnName + " = " + value + ")";
				}
				else if (op.equals(OP_INT_EQUAL_MULTI)) {
					output = "(" + columnName + " IN (" + value + "))";
				}
				else if (op.equals(OP_INT_NOT_EQUAL)) {
					output = "(NOT " + columnName + " = " + value + ")";
				}
				else if (op.equals(OP_INT_EQUAL_OR_OVER)) {
					output = "(" + columnName + " >= " + value + ")";
				}
				else if (op.equals(OP_INT_EQUAL_OR_UNDER)) {
					output = "(" + columnName + " <= " + value + ")";
				}
				else if (op.equals(OP_INT_BETWEEN)) {
					valueAdd = escapeToUpperCaseTrimNoNull(valueAdd);
					//
					if (valueAdd.equals("")) {
						output = "";
					}
					else {
						output = "(" + columnName + " BETWEEN " + value + " AND " + valueAdd + ")";
					}
				}
			}
		}
		else if (op.equals(OP_BOOL_TRUE) || op.equals(OP_BOOL_FALSE)) {                  // Boolean
			if (op.equals(OP_BOOL_TRUE)) {
				if (isCasting) {
					output = "(UPPER(" + columnName + ") = 'YES')";                           //UPPER('yes')
				}
				else {
					output = "(" + columnName + ")";
				}
			}
			else {   // op.equals(OP_BOOL_FALSE)
				if (isCasting) {
					output = "(NOT UPPER(" + columnName + ") = 'YES')";                           //UPPER('no')
				}
				else {
					output = "(NOT " + columnName + ")";
				}
			}
		}
		//
		return output;
	}


	public static StringBuilder addCondition(StringBuilder sb, String columnName, String op, String value, String valueAdd) {
		if (op.equals(OP_ANY)) {
			// Do Nothing
		}
		else {
			String temp = oneCondition(columnName, op, value, valueAdd, false);
			if (temp.length() > 0) {
				sb.append(" and ").append(temp);
			}
		}
		//
		return sb;
	}

	public static StringBuilder addFieldCondition(StringBuilder sb, String fieldID, String op, String value, String valueAdd) {
		if (op.equals(OP_ANY)) {
			// Do Nothing
		}
		else {
			String temp = oneCondition("of2.fieldText", op, value, valueAdd, true);
			if (temp.length() > 0) {
				sb.append(" or (of2.fieldID = ").append(fieldID).append(" and (not ").append(temp).append("))");
			}
		}
		//
		return sb;
	}


	public static BigDecimal getBigDecimalFromParamMap(String name, Map<String, String[]> parameters) {
		if (parameters != null && name != null) {
			String[] temps = parameters.get(name);
			//
			if (temps != null && temps.length > 0 && temps[0].length() > 0) {
				return new BigDecimal(temps[0]);
			}
			else {
				return new BigDecimal(-1);
			}
		}
		else {
			return new BigDecimal(-2);
		}
	}

	public static String getStringFromParamMap(String name, Map<String, String[]> parameters) {
		if (parameters != null && name != null) {
			String[] temps = parameters.get(name);
			//
			if (temps != null && temps.length > 0) {
				return temps[0];
			}
			else {
				return "";
			}
		}
		else {
			return "";
		}
	}

	public static int getIntFromParamMap(String name, Map<String, String[]> parameters) {
		if (parameters != null && name != null) {
			String[] temps = parameters.get(name);
			//
			if (temps != null && temps.length > 0 && temps[0].length() > 0) {
				int iTemp = -1;
				try {
					iTemp = Integer.parseInt(temps[0]);
				}
				catch (NumberFormatException e) {
					e.printStackTrace();
				}
				//
				return iTemp;
			}
			else {
				return -2;
			}
		}
		else {
			return -3;
		}
	}

	public static boolean getBooleanFromParamMap(String name, Map<String, String[]> parameters) {
		String[] temps = parameters.get(name);
		//
		if (temps != null && temps.length > 0) {
			String szTemp = temps[0];
			//
			return szTemp.equalsIgnoreCase("checked");
		}
		return false;
	}


	public static String getListOfStringsFromSet(Set<String> set, String delimiter) {
		String out = "";
		if (set != null) {
			for (String one : set) {
				if (one != null && one.length() > 0) {
					if (out.equals("")) {
						out = delimiter + one + delimiter;
					}
					else {
						out = out + "," + delimiter + one + delimiter;
					}
				}
			}
		}
		return out;
	}


	public static String getListOfStringsFromParamMap(String name, int startIndex, Map<String, String[]> parameters, String delimiter) {
		String out = "";
		if (parameters != null) {
			String[] temps = null;
			int      index = startIndex;
			do {
				temps = parameters.get(name + (index++));
				//
				if (temps != null) {
					String one = temps[0];
					if (one != null && one.length() > 0) {
						if (out.equals("")) {
							out = delimiter + one + delimiter;
						}
						else {
							out = out + "," + delimiter + one + delimiter;
						}
					}
				}
			}
			while (temps != null);
		}
		//
		return out;
	}

	public static int getPrefixIntFromParamMap(String name, Map<String, String[]> parameters, char delimiter) {
		int out = -1;
		//
		String temp  = getStringFromParamMap(name, parameters);
		int    index = temp.indexOf(delimiter);
		if (index >= 0) {
			temp = temp.substring(index + 1);
			//
			try {
				out = Integer.parseInt(temp);
			}
			catch (NumberFormatException e) {
				e.printStackTrace();
			}
		}
		//
		return out;
	}


	//For debug
	public static String dumpParamMap(Map<String, String[]> parameters) {
		StringBuilder sb = new StringBuilder("Params:");
		for (Map.Entry<String, String[]> s : parameters.entrySet()) {
			String   key    = s.getKey();
			String[] values = s.getValue();
			//
			for (int i = 0; i < values.length; i++) {
				if (i == 0) {
					sb.append(key).append("=").append(values[i]).append(",");
				}
				else {
					sb.append(key).append("(").append(i).append(")=").append(values[i]).append(",");
				}
			}
		}
		//
		String output = sb.toString();
		//
		if (output.endsWith(",")) {
			output = output.substring(0, output.length() - 1);
		}
		//
		return Helper.escapeHTML(output);
	}
}