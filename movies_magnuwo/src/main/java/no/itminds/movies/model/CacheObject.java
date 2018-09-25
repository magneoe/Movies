package no.itminds.movies.model;

import java.time.LocalDateTime;
import java.util.List;

public class CacheObject<T extends Comparable<T>> {

	private String key;
	private List<T> cacheItems;
	private LocalDateTime lastUpdated;
	
	public CacheObject(String key, List<T> cacheItems) {
		this.key = key;
		this.cacheItems = cacheItems;
		lastUpdated = LocalDateTime.now();
	}
	
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public List<T> getCacheItems() {
		return cacheItems;
	}
	public void setCacheItems(List<T> cacheItems) {
		this.cacheItems = cacheItems;
	}
	public LocalDateTime getLastUpdated() {
		return lastUpdated;
	}
	public void setLastUpdated(LocalDateTime lastUpdated) {
		this.lastUpdated = lastUpdated;
	}
}
