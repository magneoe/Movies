import axios from 'axios';
import {SESSION_ID} from './httpConfig';

export const performLogin = (formData) => {
    return axios.post('/login', {
        email: formData.get('email'),
        password: formData.get('password')
    }).then(result => {
        if(result.status === 200)
            sessionStorage.setItem(SESSION_ID, result.headers.authorization);
        return result.data;
    }).catch(error => Promise.reject(error));
}