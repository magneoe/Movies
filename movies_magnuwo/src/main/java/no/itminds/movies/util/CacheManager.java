package no.itminds.movies.util;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.scheduling.annotation.Scheduled;

import no.itminds.movies.model.Actor;
import no.itminds.movies.model.Genre;
import no.itminds.movies.model.Movie;

public class CacheManager {

	private static ConcurrentHashMap<String, Object> cacheMap = new ConcurrentHashMap<String, Object>();
	
	private final static String ACTORS = "ACTORS";
	private final static String GENRES = "GENRES";
	private final static String MOVIES = "MOVIES";
	
	public static Object getActors(){
		return cacheMap.get(ACTORS);
	}
	public static void setActors(List<Actor> actors) {
		cacheMap.put(ACTORS, actors);
	}
	public static Object getGenres() {
		return cacheMap.get(GENRES);
	}
	public static void setGenres(List<Genre> genres) {
		cacheMap.put(GENRES, genres);
	}
	public static Object getMovies() {
		return cacheMap.get(MOVIES);
	}
	public static void setMovies(List<Movie> movies) {
		cacheMap.put(MOVIES, movies);
	}
	
	@Scheduled(fixedDelay=1000*60) //Every minute cache will be cleared
	protected void clearCache() {
		cacheMap.clear();
	}
}
