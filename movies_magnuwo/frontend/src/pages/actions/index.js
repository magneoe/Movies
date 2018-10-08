import {loadMoviesEpic} from '../home/actions';
import {loginEpic} from '../login/actions';
import { combineEpics } from 'redux-observable';

export default combineEpics(
    loadMoviesEpic,
    loginEpic
);