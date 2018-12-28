package br.ufrn.lets.crashawaredev.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ResultConsume extends SearchResult {

    @JsonProperty("hits")
    @JsonDeserialize(using = SearchHitsDeserializer.class)
    private List<ResultConsumeItem> hits = new ArrayList<ResultConsumeItem>();

    public List<ResultConsumeItem> getHits() {
        return this.hits;
    }

    public void setHits(List<ResultConsumeItem> hits) {
        this.hits = hits;
    }
}
