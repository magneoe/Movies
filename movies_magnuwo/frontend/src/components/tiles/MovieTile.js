import React, { Component } from 'react';
import './style.css';

export class MovieTile extends Component {

    render() {
        let { title, year, posterUrl, duration, averageRating, onClick } = this.props;

        const genres = this.props.genres || [];
        const actors = this.props.actors || [];

        const genreView = genres.map(genre => genre.name).join(', ');
        const actorsView = actors.map(actor => actor.name).join(', ');

        return (
            <div onClick={onClick} className="tileContainer">
                <div className="tilePropertyContainer">
                    <table>
                        <tbody>
                            <tr>
                                <td colSpan="2">
                                    <span className="tileHeader tileTitle">{title}</span>
                                </td>
                            </tr>
                            <tr>
                                <td><span className="tileHeader">Year:</span></td>
                                <td>{year}</td>
                            </tr>
                            <tr>
                                <td><span className="tileHeader">Duration:</span></td>
                                <td>{duration} mins</td>
                            </tr>
                            <tr>
                                <td><span className="tileHeader">Genres:</span></td>
                                <td>{genreView}</td>
                            </tr>
                            <tr>
                                <td><span className="tileHeader">Actors:</span></td>
                                <td>{actorsView}</td>
                            </tr>
                            <tr>
                                <td><span className="tileHeader">Rating:</span></td>
                                <td>{averageRating}/10</td>
                            </tr>
                        </tbody>
                    </table>
                </div>
                <div className="posterContainer">
                    <img alt="Movie poster" src={posterUrl} width="120px" />
                </div>
            </div>
        );
    }
}