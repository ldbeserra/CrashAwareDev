package br.ufrn.lets.crashawaredev.provider;

import java.util.ArrayList;
import java.util.List;

import br.ufrn.lets.crashawaredev.model.Crash;

public enum CrashProvider {
	
	INSTANCE;
	
	private List<Crash> crashes;
	
	private List<Crash> getCrashesByClassName(String className){
		
		
		
		return null;
	}
	
	private CrashProvider() {
		crashes = new ArrayList<>();
	}
	
	public List<Crash> getCrashes(){
		return crashes;
	}

}
