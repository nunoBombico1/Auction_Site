package rutgers.cs336.gui;

import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.jsp.JspWriter;

import rutgers.cs336.servlet.IConstant;

public class Helper implements IConstant {
	// GUI
	private static final String SELECT_OP_SZ_TYPE       = "<select name='?'><option value='any'>Any</option><option value='szequal'>Equal</option><option value='sznotequal'>Not Equal</option><option value='startwith'>Starts With</option><option value='contain'>Contains</option></select>";
	private static final String SELECT_OP_INT_TYPE      = "<select name='?'><option value='any'>Any</option><option value='intequal'>Equal</option><option value='intnotequal'>Not Equal</option><option value='equalorover'>Greater Than or Equal To</option><option value='equalorunder'>Less Than or Equal To</option><option value='between'>Between</option></select>";
	private static final String SELECT_OP_BOOL_TYPE     = "<select name='?'><option value='any'>Any</option><option value='true'>True</option><option value='false'>False</option></select>";
	private static final String CONDITION_CODE_CHECKBOX = "<div><input type='checkbox' id='new' name='?_1' value='yes' checked><label for='new'>New</label><input type='checkbox' id='likenew' name='?_2' value='yes' checked><label for='likenew'>Like New</label><input type='checkbox' id='manfrefurb' name='?_3'  value='yes' checked><label for='manfrefurb'>Manufacturer Refurbished</label><input type='checkbox' id='sellerrefurb' name='?_4' value='yes' checked><label for='sellerrefurb'>Seller Refurbished</label><input type='checkbox' id='used' name='?_5' value='yes' checked><label for='used'>Used</label><input type='checkbox' id='notwork' name='?_6' value='yes' checked><label for='notwork'>For parts or Not Working</label></div>";

	public static String getOPSZSelection(String name) {
		return SELECT_OP_SZ_TYPE.replaceAll("\\?", name);
	}

	public static String getOPIntSelection(String name) {
		return SELECT_OP_INT_TYPE.replaceAll("\\?", name);
	}

	public static String getOPBoolSelection(String name) {
		return SELECT_OP_BOOL_TYPE.replaceAll("\\?", name);
	}

	public static String getConditionCodeCheckBox(String name) {
		return CONDITION_CODE_CHECKBOX.replaceAll("\\?", name);
	}
	
	
	
	public static String getConditionFromCode(String code) {
		switch (code) {
			case "1":
				return "New";
			case "2":
				return "Like New";
			case "3":
				return "Manufacturer Refurbished";
			case "4":
				return "Seller Refurbished";
			case "5":
				return "Used";
			case "6":
				return "For parts or Not Working";
			default:
				return "Unknown.";
		}
	}
	public static String getCodeFromCondition(String condition) {
		switch (condition) {
			case "New":
				return "1";
			case "Like New":
				return "2";
			case "Manufacturer Refurbished":
				return "3";
			case "Seller Refurbished":
				return "4";
			case "Used":
				return "5";
			case "For parts or Not Working":
				return "6";
			default:
				return "9";
		}
	}
	
	
	
	public static String printOneRowInTable(List row, int startIndex) {
		String out = "";
		if (row != null && row.size() > 0) {
			for (int i = startIndex; i < row.size(); i++) {
				Object one = row.get(i);
				String oneItem = (one == null) ? "" : one.toString();
				out = out + "<td>" + oneItem + "</td>";
			}
		}
		return out;
	}
	
}