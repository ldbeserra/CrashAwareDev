package br.ufrn.lets.crashawaredev.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Aggregations {
	
	@JsonProperty("buckets")
    private List<Buckets> buckets = new ArrayList<>();;

	public List<Buckets> getBuckets() {
		return buckets;
	}

	public void setBuckets(List<Buckets> buckets) {
		this.buckets = buckets;
	}
	
}
