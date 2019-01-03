package br.ufrn.lets.crashawaredev.util;

public class CrashInfoProvider {
	
	private static CrashInfoProvider instance;
	
	private CrashInfoProvider() {}
	
	public static CrashInfoProvider getInstance() {
		if(instance == null)
			return new CrashInfoProvider();
		return instance;
	}
	
	public String getInfo(String exceptionName) {
		return InfoExceptionsConstants.getInfo(exceptionName);
	}
	

}
