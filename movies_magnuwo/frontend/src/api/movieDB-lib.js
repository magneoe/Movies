import axios from 'axios';

const getMovies = (page, size, sort, direction) => {
    return axios.get('/api/movies/getAll', {
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

const vote = (rating, movieId) => {
    let username = 'test@gmail.com';
    let password = 'secret';
    let credentials = btoa(username + ':' + password);
    let basicAuth = 'Basic ' + credentials;
    console.log(basicAuth);
    return axios({
        url: '/api/movies/vote', 
        method: 'POST',
        headers: { 'Authorization': + basicAuth },
        data:{
            rating,
            movieId,
        }
    }).then(result => {
        return result.data;
    }).catch(error => console.log("Error:", error));
}
const getAllActors = () => {
    let username = 'test@gmail.com';
    let password = 'secret';
    let credentials = btoa(username + ':' + password);
    let basicAuth = 'Basic ' + credentials;
    console.log(basicAuth);
    return axios({
        url: '/api/actors/getAll', 
        method: 'GET',
        headers: { 'Authorization': + basicAuth },
    }).then(result => {
        return result.data;
    }).catch(error => console.log("Error:", error));
}

const performLogin = (username, password) => {
    return axios({
        method: 'POST',
        url: '/perform_login',
        data: {
          username,
          password
        }
      }).then(result => {
        return result.data;
    });
}


export {
    getMovies,
    vote,
    performLogin,
    getAllActors
}




