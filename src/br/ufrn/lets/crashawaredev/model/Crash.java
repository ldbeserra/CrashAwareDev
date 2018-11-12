package br.ufrn.lets.crashawaredev.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class Crash {

	private String className;
	private String trace;
	private String rootCause;
	private String date;
	private String link;
	
	private PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);
	
	public Crash() {}
	
	public Crash(String className, String trace, String rootCause,
			 	 String date, String link) {
		this.className = className;
		this.trace = trace;
		this.rootCause = rootCause;
		this.date = date;
		this.link = link;
	}
	
	public void addPropertyChangeListener(String propertyName, PropertyChangeListener listener) {
		propertyChangeSupport.addPropertyChangeListener(propertyName, listener);
	}
	
	public void removePropertyChangeListener(PropertyChangeListener listener) {
		propertyChangeSupport.removePropertyChangeListener(listener);
	}
	
	public String getClassName() {
		return className;
	}
	
	public void setClassName(String className) {
		propertyChangeSupport.firePropertyChange(
				"className", 
				this.className, 
				this.className = className);
	}
	public String getTrace() {
		return trace;
	}
	public void setTrace(String trace) {
		propertyChangeSupport.firePropertyChange(
				"trace", 
				this.trace, 
				this.trace = trace);
	}
	public String getRootCause() {
		return rootCause;
	}
	public void setRootCause(String rootCause) {
		propertyChangeSupport.firePropertyChange(
				"rootCause", 
				this.rootCause, 
				this.rootCause = rootCause);
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		propertyChangeSupport.firePropertyChange(
				"date", 
				this.date, 
				this.date = date);
	}
	public String getLink() {
		return link;
	}
	public void setLink(String link) {
		propertyChangeSupport.firePropertyChange(
				"link", 
				this.link, 
				this.link = link);
	}
	
}
