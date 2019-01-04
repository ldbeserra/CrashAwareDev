package br.ufrn.lets.crashawaredev.model;

public class SearchForm {

	private String searchTerm;
	
	private int from;
	
	private int size;
	
	private int days;
	
	private boolean count;
	
	private int idSistema;
	
	public SearchForm() {
		this.from = 0;
		this.size = 0;
		this.days = 1;
		this.count = false;
		this.idSistema = 1;
	}
	
	public String getSearchTerm() {
		return searchTerm;
	}

	public void setSearchTerm(String searchTerm) {
		this.searchTerm = searchTerm;
	}

	public int getFrom() {
		return from;
	}

	public void setFrom(int from) {
		this.from = from;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public int getDays() {
		return days;
	}

	public void setDays(int days) {
		this.days = days;
	}

	public boolean isCount() {
		return count;
	}

	public void setCount(boolean count) {
		this.count = count;
	}

	public int getIdSistema() {
		return idSistema;
	}

	public void setIdSistema(int idSistema) {
		this.idSistema = idSistema;
	}
	
}
