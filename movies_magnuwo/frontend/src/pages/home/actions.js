import {from, of} from 'rxjs';
import {mergeMap, map, delay, catchError} from 'rxjs/operators';
import {ofType} from 'redux-observable';

import {getMovies} from '../../api/movieDB-lib';

export const LOAD_MOVIES = "MOVIES/LOAD_MOVIES";
export const LOAD_MOVIES_SUCCESS = "MOVIES/LOAD_MOVIES_SUCCESS";
export const LOAD_MOVIES_FAILURE = "MOVIES/LOAD_MOVIES_FAILURE";

export const DIRECTION_ASC = 0;
export const DIRECTION_DESC = 1;
export const SORT_OPTIONS = ["title", "year", "duration"];


export function loadMovies(page, size, sort, direction) {
    return {
        type: LOAD_MOVIES,
        size,
        page,
        sort,
        direction
    }
}
export function loadMoviesSuccess(movieData){
    return {
        type: LOAD_MOVIES_SUCCESS,
        movieData,
    }
}
export function loadMoviesFailure(error) {
    return {
        type: LOAD_MOVIES_FAILURE,
        error
    }
}
function handleError(error) {
    if(error && error.response){
        if(error.response.status >= 400 && error.response.status < 500)
        {
            //Handle client errors
            alert("Client error: " + error.response.statusText);
            return "Client error";
        }
        else if(error.response.status >= 500 && error.response.status < 600){
            //Handle server errors
            alert("Server error: " + error.response.statusText);
            return "Server error";
        }
    }
    return error;
}
export const loadMoviesEpic = actions$ =>
    actions$.pipe(
        ofType(LOAD_MOVIES),
        delay(1000),
        mergeMap(action => from(getMovies(action.page, action.size, action.sort, action.direction))),
        map(result => loadMoviesSuccess(result)),
        catchError(error => of(loadMoviesFailure(handleError(error))))
    );



export const movieReducer = (state = {}, action) => {
    switch(action.type){
        case LOAD_MOVIES:
           return {...state,
                loading: true,
                requestConfig: {
                    ...state.requestConfig,
                    sort: action.sort,
                    direction: action.direction
                }
           };
        case LOAD_MOVIES_SUCCESS:
            return {
                ...state,
                loading: false,
                movieData: action.movieData,
                error: null,
            }
        case LOAD_MOVIES_FAILURE:
            return {
                ...state,
                loading: false,
                error: action.error,
                movieData: null
            }
        default:
        return state;
    }
}
