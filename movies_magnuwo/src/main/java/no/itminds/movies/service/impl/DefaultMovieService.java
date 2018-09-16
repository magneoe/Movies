package no.itminds.movies.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import no.itminds.movies.model.Movie;
import no.itminds.movies.repository.MovieRepository;
import no.itminds.movies.service.MovieService;

@Service
public class DefaultMovieService implements MovieService {

	@Autowired
	private MovieRepository movieRepo;
	
	@Override
	public List<Movie> getAll() {
		// TODO Auto-generated method stub
		return movieRepo.findAll();
	}

	@Override
	public Movie getDetails(Long id) {
		// TODO Auto-generated method stub
		Movie movie = movieRepo.getOne(id);
		System.out.println(movie);
		return movie;
	}
	
}
