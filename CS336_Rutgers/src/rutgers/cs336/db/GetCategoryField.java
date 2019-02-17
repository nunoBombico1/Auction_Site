package rutgers.cs336.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static rutgers.cs336.db.CreateOffer.PREFIX_CATEGORY_NAME;

public class GetCategoryField extends DBBase implements IDaoConstant {
	public static final String DATA_CATEGORY_LIST = "DATA_CATEGORY_LIST";
	public static final String DATA_FIELD_LIST    = "DATA_FIELD_LIST";

	public static class Category {
		String  categoryName;
		boolean isCurr;

		//
		public Category(String categoryName, boolean isCurr) {
			this.categoryName = categoryName;
			this.isCurr = isCurr;
		}

		//
		public String getCategoryName() {
			return categoryName;
		}

		//
		public boolean isCurr() {
			return isCurr;
		}
	}

	public static class Field {
		String categoryName;
		int    fieldID;
		String fieldName;

		//
		public Field(String categoryName, int fieldID, String fieldName) {
			this.categoryName = categoryName;
			this.fieldID = fieldID;
			this.fieldName = fieldName;
		}

		//
		public String getCategoryName() {
			return categoryName;
		}

		public int getFieldID() {
			return fieldID;
		}

		public String getFieldName() {
			return fieldName;
		}
	}


	private static final String queryGetCategoryField = "select categoryName, categoryfield.fieldID, fieldName from categoryfield inner join field on categoryfield.fieldID = field.fieldID order by categoryName, categoryfield.fieldID";


	public static Map getCategoryField(Map<String, String[]> parameters) {
		Map output = new HashMap();
		//
		String categoryNameFromParam = getStringFromParamMap(PREFIX_CATEGORY_NAME, parameters);
		//
		Connection con  = null;
		Statement  stmt = null;
		try {
			con = getConnection();
			//
			stmt = con.createStatement();
			//
			ResultSet rs = stmt.executeQuery(queryGetCategoryField);
			//
			List lstField    = new ArrayList();
			List lstCategory = new ArrayList();
			//
			output.put(DATA_CATEGORY_LIST, lstCategory);
			output.put(DATA_FIELD_LIST, lstField);
			//
			while (rs.next()) {
				Object categoryName = rs.getObject(1);
				Object fieldID      = rs.getObject(2);
				Object fieldName    = rs.getObject(3);
				//
				String sz_categoryName = categoryName.toString();
				int    i_fieldID       = (Integer) fieldID;
				String sz_fieldName    = fieldName.toString();
				//
				if (categoryNameFromParam.equals("")) {
					categoryNameFromParam = sz_categoryName;
				}
				//
				if (categoryNameFromParam.equals(sz_categoryName)) {
					lstField.add(new Field(sz_categoryName, i_fieldID, sz_fieldName));
				}
				//
				String currCategory;
				if (lstCategory.size() == 0) {
					currCategory = "";
				}
				else {
					Category temp = (Category) lstCategory.get(lstCategory.size() - 1);
					currCategory = temp.getCategoryName();
				}
				//
				if (!currCategory.equals(sz_categoryName)) {
					lstCategory.add(new Category(sz_categoryName, categoryNameFromParam.equals(sz_categoryName)));
				}
			}
			//
			output.put(DATA_NAME_STATUS, true);
			output.put(DATA_NAME_MESSAGE, "OK");
		}
		catch (SQLException e) {
			output.put(DATA_NAME_STATUS, false);
			output.put(DATA_NAME_MESSAGE, "ERROR: " + e.getErrorCode() + ", SQL_STATE: " + e.getSQLState());
			e.printStackTrace();
		}
		catch (ClassNotFoundException e) {
			output.put(DATA_NAME_STATUS, false);
			output.put(DATA_NAME_MESSAGE, "ERROR: " + "ClassNotFoundException" + ", SQL_STATE: " + e.getMessage());
			e.printStackTrace();
		}
		finally {
			if (stmt != null) {
				try {
					stmt.close();
				}
				catch (Throwable e) {
					e.printStackTrace();
				}
			}
			//
			if (con != null) {
				try {
					con.close();
				}
				catch (Throwable e) {
					e.printStackTrace();
				}
			}
		}
		//
		return output;
	}


	public static void main(String[] args) {
		initTest();
		//
		Map map = getCategoryField(null);
		//
		System.out.println(DATA_NAME_STATUS + "= " + map.get(DATA_NAME_STATUS));
		System.out.println(DATA_NAME_MESSAGE + "= " + map.get(DATA_NAME_MESSAGE));
		System.out.println(DATA_NAME_USER_TYPE + "= " + map.get(DATA_NAME_USER_TYPE));
	}
}
