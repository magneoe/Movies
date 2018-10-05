import axios from 'axios';

export function getMovies(page, size, sort, direction) {
    return axios.get('/movies/getAll', {
        params: {
            size,
            page,
            sort,
            direction,
        }
    }).then(result => {
        return result.data;
    });
}


