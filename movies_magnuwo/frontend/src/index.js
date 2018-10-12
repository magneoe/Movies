import React from 'react';
import ReactDOM from 'react-dom';
import { BrowserRouter as Router, Route } from 'react-router-dom';
import { Provider } from 'react-redux';
import { createStore, applyMiddleware, compose } from 'redux';
import { createEpicMiddleware } from 'redux-observable';
import thunk from 'redux-thunk';

import RootReducer from './store';
import App from './App';
import LoginHandler from './pages/login/LoginHandler';
import combinedEpics from './pages/actions';
import {DIRECTION_ASC, SORT_OPTIONS} from './pages/home/actions';

import * as serviceWorker from './serviceWorker';

const INITIAL_STATE = {
    loginReducer: {
        user: {}
    },
    movieReducer: {
        loading: false,
        comments: [],
        requestConfig: {
            size: 5, //Page size
            direction: DIRECTION_ASC, //Sort direction
            sort: SORT_OPTIONS[0],
    }
    }
};
const epicMiddleware = createEpicMiddleware();
const composeEnhancers = window.__REDUX_DEVTOOLS_EXTENSION_COMPOSE__ || compose;
const store = createStore(RootReducer, INITIAL_STATE,
    composeEnhancers(
        applyMiddleware(epicMiddleware, thunk)
        ));
epicMiddleware.run(combinedEpics)

ReactDOM.render(
    <Provider store={store}>
        <Router>
            <div>
                <Route exact path='/' component={App}>
                </Route>
                <Route path="/loginpage" component={LoginHandler} />
            </div>
        </Router>
    </Provider>
    , document.getElementById('root'));

// If you want your app to work offline and load faster, you can change
// unregister() to register() below. Note this comes with some pitfalls.
// Learn more about service workers: http://bit.ly/CRA-PWA
serviceWorker.unregister();
