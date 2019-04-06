package rutgers.cs336.gui;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TableData {
	Map<String, Integer> mapHeaderToIndex;
	//
	List                 lstHeader;
	List                 lstRows;
	int[]                colSeq;
	//
	int                  indexSorted  = -1;
	boolean              normalSorted = true;
	//
	String               signStandOut = null;
	int                  idxStandOut  = -1;


	public int rowCount() {
		return (lstRows == null) ? 0 : lstRows.size();
	}

	public int colCount() {
		return (colSeq == null) ? 0 : colSeq.length;
	}


	//	public List getLstHeader() {
	//		return lstHeader;
	//	}


	public List getRows() {
		return lstRows;
	}


	//	public int[] getColSeq() {
	//		return colSeq;
	//	}

	public String getSignStandOut() {
		return signStandOut;
	}

	public void setStandOut(String input, int idx) {
		signStandOut = input;
		idxStandOut = idx;
	}

	public TableData(List _lstHeader, List _lstRows, int[] _colSeq) {
		lstHeader = _lstHeader;
		lstRows = _lstRows;
		colSeq = _colSeq;
		//
		mapHeaderToIndex = new HashMap<>();
		if (lstHeader != null) {
			for (int i = 0; i < lstHeader.size(); i++) {
				String temp = lstHeader.get(i) == null ? "" : lstHeader.get(i).toString();
				mapHeaderToIndex.put(temp, i);
			}
		}
	}

	//	public List getOneRow(int i) {
	//		return (List)(lstRows.get(i));
	//	}

	public Object getOneCell(int i, int j) {
		return ((List) (lstRows.get(i))).get(j);
	}

	public Object getOneCell(int i, String header) {
		int index = mapHeaderToIndex.get(header);
		if (index >= 0) {
			return ((List) (lstRows.get(i))).get(index);
		}
		else {
			return "";
		}
	}

	public Object getLastCellInRow(int i) {
		List lst = (List) (lstRows.get(i));
		return lst.get(lst.size() - 1);
	}


	public void sortRowPerHeader(String header) {
		if (header != null) {
			int index = mapHeaderToIndex.get(header);
			//
			if (index >= 0 && index < lstHeader.size()) {
				Comparator<Object> comparatorNorm = Comparator.comparing(o -> ((List) o).get(index) == null ? "" : ((List) o).get(index).toString().trim());
				//
				boolean isTheSame = (indexSorted == index);
				if (isTheSame) {
					if (normalSorted) {
						lstRows.sort(comparatorNorm.reversed());
					}
					else {
						lstRows.sort(comparatorNorm);
					}
					normalSorted = !normalSorted;
				}
				else {
					lstRows.sort(comparatorNorm);
					normalSorted = true;
				}
				indexSorted = index;
			}
		}
	}

	public String printHeaderForTable() {
		String out = "";
		if (lstHeader != null && lstHeader.size() > 0 && colSeq != null && colSeq.length > 0) {
			for (int value : colSeq) {
				Object one     = lstHeader.get(value);
				String oneItem = (one == null) ? "" : one.toString();
				out = out + "<th><div onclick=onClickHeader('" + oneItem + "')>" + oneItem + "</div></th>";
			}
		}
		return out;
	}


	public String printOneRowInTable(int index) {
		List   row = (List) lstRows.get(index);
		String out = "";
		if (row != null && row.size() > 0 && colSeq != null && colSeq.length > 0) {
			for (int value : colSeq) {
				Object one     = row.get(value);
				String oneItem = (one == null) ? "" : one.toString();
				out = out + "<td>" + Helper.escapeHTML(oneItem) + "</td>";
			}
		}
		return out;
	}


	public String printRowStart(int index) {
		List   row = (List) lstRows.get(index);
		String out = "";
		//
		boolean isStandOut = signStandOut != null && idxStandOut >= 0 && (row.get(idxStandOut)).toString().equals(signStandOut);
		//
		if (isStandOut) {
			out = "<tr style='color: red;'>";
			//out = "<tr name='standout' class='standout'>";
		}
		else {
			out = "<tr>";
		}
		//
		return out;
	}

}
