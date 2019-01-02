package br.ufrn.lets.crashawaredev.model;

public class RootCause {
	
	private Integer order;
	
	private String cause;
	
	private Integer ocurrences;
	
	private Double rate;
	
	public RootCause() {}
	
	public RootCause(Integer order, String cause, Integer ocurrences, Double rate) {
		this.order = order;
		this.cause = cause;
		this.ocurrences = ocurrences;
		this.rate = rate;
	}
	
	public Integer getOrder() {
		return order;
	}

	public void setOrder(Integer order) {
		this.order = order;
	}

	public String getCause() {
		return cause;
	}

	public void setCause(String cause) {
		this.cause = cause;
	}

	public Integer getOcurrences() {
		return ocurrences;
	}

	public void setOcurrences(Integer ocurrences) {
		this.ocurrences = ocurrences;
	}

	public Double getRate() {
		return rate;
	}

	public void setRate(Double rate) {
		this.rate = rate;
	}
	
}
