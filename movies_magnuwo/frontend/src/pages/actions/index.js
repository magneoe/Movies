import {loadMoviesEpic} from '../home/actions';
import { combineEpics } from 'redux-observable';

export default combineEpics(
    loadMoviesEpic
);