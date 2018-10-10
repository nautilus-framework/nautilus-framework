package thiagodnf.nautilus.plugin.gui;

import java.util.ArrayList;
import java.util.List;

public class TableTabContent extends TabContent {

	private List<String> header;
	
	private List<List<String>> rows;
	
	private boolean showRowId = true;
	
	public TableTabContent(List<String> header) {
		this.header = header;
		this.rows = new ArrayList<>();
	}
	
	public List<String> getHeader() {
		return header;
	}

	public void setHeader(List<String> header) {
		this.header = header;
	}

	public List<List<String>> getRows() {
		return rows;
	}

	public void setRows(List<List<String>> rows) {
		this.rows = rows;
	}
	
	public boolean isShowRowId() {
		return showRowId;
	}

	public void setShowRowId(boolean showRowId) {
		this.showRowId = showRowId;
	}

	public String getHTML() {
		
		StringBuffer buffer = new StringBuffer();
		
		buffer.append("<table class=\"table table-striped table-hover table-sm table-datatable\">");
		buffer.append("<thead>");
		buffer.append("<tr>");
		
		if (showRowId) {
			buffer.append("<th width=\"50px\">#</th>");
		}
		
		for (int i = 0; i < header.size(); i++) {
			buffer.append("<th>").append(header.get(i)).append("</th>");
		}
		
		buffer.append("</tr>");
		buffer.append("</thead>");
		buffer.append("<tbody>");
		
		for (int i = 0; i < rows.size(); i++) {

			buffer.append("<tr>");

			if (showRowId) {
				buffer.append("<td>").append(i).append("</td>");
			}

			for (String column : rows.get(i)) {
				buffer.append("<td>").append(column).append("</td>");
			}

			buffer.append("</tr>");
		}
		
		buffer.append("</tbody>");
		buffer.append("</table>");
		
		return buffer.toString();
	}
	
}
