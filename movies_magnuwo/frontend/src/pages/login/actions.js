import {from, of} from 'rxjs';
import {mergeMap, map, catchError} from 'rxjs/operators';
import {ofType} from 'redux-observable';

import {performLogin} from '../../api/login-lib';

export const LOGIN = "LOGIN/LOGIN";
export const LOGIN_SUCCESS = "LOGIN/LOGIN_SUCCESS";
export const LOGIN_FAILURE = "LOGIN/LOGIN_FAILURE";

export function login(formData) {
    return {
        type: LOGIN,
        formData,
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
        mergeMap(action => from(performLogin(action.formData))),
        map(result => loginSuccess(result)),
        catchError(error => of(loginFailure(error)))
    );

export const loginReducer = (state = {}, action) => {
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