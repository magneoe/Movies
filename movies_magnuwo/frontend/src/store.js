import {combineReducers} from 'redux';

import {movieReducer} from './pages/home/actions';
/*
 * Configures the store with reducers
 */
const RootReducer = combineReducers({
    movieReducer
  });

  export default RootReducer;