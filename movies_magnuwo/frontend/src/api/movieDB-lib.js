import {http} from './httpConfig';


const getMovies = (page, size, sort, direction) => {
    return http.get('/movies/getAll', {
        params: {
            size,
            page,
            sort,
            direction,
        }
    }).then(result => {
        return result.data;
    }).catch(error => Promise.reject(error));
}
const getComments = (movieId, page, size) => {
    return http.get('/movies/comments', {
        params: {
            movieId,
            size,
            page
        }
    }).then(result => {
        return {movieId, data: result.data};
    }).catch(error => Promise.reject(error));
}

const voteMovie = (rating, movieId) => {
    return http({
        url: '/movies/vote ',
        method: 'POST',
        data: {
            rating,
            movieId,
        }
    }).then(result => {
        return result.data;
    }).catch(error => Promise.reject(error));
}
const getRating = (movieId) => {
    return http({
        url: '/movies/rating',
        method: 'GET',
        params: {
            movieId,
        }
    }).then(result => {
        result.data.movieId = movieId;
        return result.data;
    }).catch(error => Promise.reject(error));
}

const getAllActors = () => {
    return http({
        url: '/actors/getAll',
        method: 'GET',
    }).then(result => {
        return result.data;
    }).catch(error => Promise.reject(error));
}

export {
    getMovies,
    voteMovie,
    getAllActors,
    getRating,
    getComments
}




