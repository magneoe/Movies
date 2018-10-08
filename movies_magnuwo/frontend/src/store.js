import {combineReducers} from 'redux';

import {movieReducer} from './pages/home/actions';
import {loginReducer} from './pages/login/actions';
/*
 * Configures the store with reducers
 */
const RootReducer = combineReducers({
    movieReducer,
    loginReducer
  });

  export default RootReducer;