import {from, of} from 'rxjs';
import {mergeMap, map, delay, catchError} from 'rxjs/operators';
import {ofType} from 'redux-observable';

import {performLogin} from '../../api/movieDB-lib';

export const LOGIN = "LOGIN/LOGIN";
export const LOGIN_SUCCESS = "LOGIN/LOGIN_SUCCESS";
export const LOGIN_FAILURE = "LOGIN/LOGIN_FAILURE";

export function login(username, password) {
    return {
        type: LOGIN,
        username,
        password
    }
}
export function loginSuccess() {
    return {
        type: LOGIN_SUCCESS,
    }
}
export function loginFailure(errorMessage) {
    return {
        type: LOGIN_FAILURE,
        errorMessage,
    }
}

export const loginEpic = actions$ =>
    actions$.pipe(
        ofType(LOGIN),
        mergeMap(action => from(performLogin(action.username, action.password))),
        map(result => loginSuccess(result)),
        catchError(error => of(loginFailure(error)))
    );

export const loginReducer = (state = {}, action) => {
    console.log("Action:", action.type);
    switch(action.type){
        case LOGIN: 
            return {...state};
        case LOGIN_SUCCESS:
            return {...state};
        case LOGIN_FAILURE:
            return {...state};
        default:
            return state;
    }
}