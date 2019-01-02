package br.ufrn.lets.crashawaredev.ast.model;

public class ReturnMessage {

	private String message;
	
	private Integer lineNumber;
	
	private Integer markerSeverity;
	
	public String getMessage() {
		return message;
	}
	
	public void setMessage(String message) {
		this.message = message;
	}
	
	public Integer getLineNumber() {
		return lineNumber;
	}
	
	public void setLineNumber(Integer lineNumber) {
		this.lineNumber = lineNumber;
	}

	public Integer getMarkerSeverity() {
		return markerSeverity;
	}

	public void setMarkerSeverity(Integer markerSeverity) {
		this.markerSeverity = markerSeverity;
	}

	
	
}
