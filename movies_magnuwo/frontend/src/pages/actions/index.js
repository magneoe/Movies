import {loadMoviesEpic, voteEpic, loadRatingEpic, loadCommentsEpic} from '../home/actions';
import {loginEpic} from '../login/actions';
import { combineEpics } from 'redux-observable';

export default combineEpics(
    loadMoviesEpic,
    voteEpic,
    loginEpic,
    loadRatingEpic,
    loadCommentsEpic
);