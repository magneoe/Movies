import React, { Component } from 'react';
import MovieDB from './pages/home/MovieDB';
import {Header} from './components/header/Header';
import './App.css';
import '../node_modules/bootstrap/dist/css/bootstrap.min.css';

class App extends Component {

  render() {
    return (
      <div className="appStyle">
        <Header/>
        <div className="container">
          <MovieDB />
        </div>
      </div>
    );
  }
}

export default App;
