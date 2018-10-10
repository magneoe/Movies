import { from, of} from 'rxjs';
import { switchMap, mergeMap, map, delay, catchError, concat } from 'rxjs/operators';
import { ofType } from 'redux-observable';

import { getMovies, voteMovie, getRating, getComments } from '../../api/movieDB-lib';

export const LOAD_MOVIES = "MOVIES/LOAD_MOVIES";
export const LOAD_MOVIES_SUCCESS = "MOVIES/LOAD_MOVIES_SUCCESS";
export const LOAD_MOVIES_FAILURE = "MOVIES/LOAD_MOVIES_FAILURE";

export const LOAD_COMMENTS = "MOVIES/LOAD_COMMENTS";
export const LOAD_COMMENT_SUCCESS = "MOVIES/LOAD_COMMENTS_SUCCSESS";
export const LOAD_COMMENT_FAILURE = "MOVIES/LOAD_COMMENTS_FAILURE";

export const VOTE = "MOVIES/VOTE";
export const VOTE_SUCCSESS = "MOVIES/VOTE_SUCCESS";
export const VOTE_FAILURE = "MOVIES/VOTE_FAILURE";

export const LOAD_RATING = "MOVIES/LOAD_RATING";
export const LOAD_RATING_SUCCESS = "MOVIES/LOAD_RATING_SUCCESS";
export const LOAD_RATING_FAILURE = "MOVIES/LOAD_RATING_FAILURE";

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
export function loadMoviesSuccess(movieData) {
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
export function loadComments(movieId, page, size) {
    return {
        type: LOAD_COMMENTS,
        movieId,
        size,
        page
    }
}

export function loadCommentsSuccess(result){
    return {
        type: LOAD_COMMENT_SUCCESS,
        result,
    }
}
export function loadCommentsFailure(error) {
    return {
        type: LOAD_COMMENT_FAILURE,
        error,
    }
}

export function vote(rating, movieId) {
    return {
        type: VOTE,
        rating,
        movieId,
    }
}
export function voteSuccess(movieData) {
    return {
        type: VOTE_SUCCSESS,
        movieData,
    }
}
export function voteFailure(error) {
    return {
        type: VOTE_FAILURE,
        error,
    }
}
export function loadRating(movieId) {
    return {
        type: LOAD_RATING,
        movieId,
    }
}
export function loadRatingSuccess(rating) {
    return {
        type: LOAD_RATING_SUCCESS,
        currentRating: rating,
    }
}
export function loadRatingFailure(error) {
    return {
        type: LOAD_RATING_FAILURE,
        error,
    }
}

function handleError(error) {
    console.log(error);
    if (error && error.response) {
        if (error.response.status >= 400 && error.response.status < 500) {
            //Handle client errors
            alert("Client error: " + error.response.statusText);
            return "Client error";
        }
        else if (error.response.status >= 500 && error.response.status < 600) {
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

export const voteEpic = actions$ =>
    actions$.pipe(
        ofType(VOTE),
        switchMap(action =>
            from(voteMovie(action.rating, action.movieId)).pipe(
                map(result => voteSuccess(result)),
                concat(of(loadRatingSuccess({ rating: action.rating, movieId: action.movieId })))
            )
        ),
        catchError(error => of(voteFailure(handleError(error))))
    );
export const loadRatingEpic = actions$ =>
    actions$.pipe(
        ofType(LOAD_RATING),
        delay(1000),
        mergeMap(action => from(getRating(action.movieId))),
        map(result => loadRatingSuccess(result)),
        catchError(error => of(loadRatingFailure(handleError(error))))
    );
export const loadCommentsEpic = actions$ =>
        actions$.pipe(
            ofType(LOAD_COMMENTS),
            mergeMap(action => from(getComments(action.movieId, action.page,
                action.size))),
            map(result => loadCommentsSuccess(result)),
            catchError(error => of(loadCommentsFailure(handleError(error))))
        );




export const movieReducer = (state = {}, action) => {
    console.log(action);
    switch (action.type) {
        case LOAD_MOVIES:
            return {
                ...state,
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
        case LOAD_COMMENTS:
            return {
                ...state,
                loading: true
            }
        case LOAD_COMMENT_SUCCESS:
            const result = action.result || {};
            const currentComments = state.comments || [];
            let updatedComments = [];

            const indexOfCurrentComments = currentComments.findIndex(entry => entry.movieId === result.movieId);
            if (indexOfCurrentComments === -1) {
                updatedComments = [...currentComments, action.result];
            }
            else {
                updatedComments = currentComments.map(entry => {
                    if (action.result.movieId !== entry.movieId)
                        return entry;
                    return {
                        ...action.result
                    }
                })
            }

            return {
                ...state,
                loading: false,
                comments: updatedComments,
            }
        case LOAD_COMMENT_FAILURE:
            return {
                ...state,
                loading: false,
            }
        case VOTE:
            return {
                ...state,
                loading: true,
            }
        case VOTE_SUCCSESS:
            const currentMovieData = state.movieData || {};
            const currentMovieContent = currentMovieData.content || [];
            const updatedMovieContent = currentMovieContent.map(entry => {
                if(entry.id !== action.movieData.id){
                    return entry;
                }
                return {
                    ...action.movieData
                };
            });
            return {
                ...state,
                loading: false,
                movieData: {
                    ...state.movieData,
                    content: updatedMovieContent
                }
            }
        case VOTE_FAILURE:
            return {
                ...state,
                loading: false,
            }
        case LOAD_RATING:
            return {
                ...state,
                loading: true
            }
        case LOAD_RATING_SUCCESS:
            let updatedUserRatings = [];
            const currentUserRatings = state.userRatings || [];

            const indexOfRating = currentUserRatings.findIndex(rating => rating.movieId === action.currentRating.movieId);
            if (indexOfRating === -1) {
                updatedUserRatings = [...currentUserRatings, action.currentRating];
            }
            else {
                updatedUserRatings = currentUserRatings.map(rating => {
                    if (action.currentRating.movieId !== rating.movieId)
                        return rating;
                    return {
                        ...action.currentRating
                    }
                })
            }
            return {
                ...state,
                loading: false,
                userRatings: updatedUserRatings
            }
        case LOAD_RATING_FAILURE:
            return {
                ...state,
                loading: false,
            }
        default:
            return state;
    }
}
