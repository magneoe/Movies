import React, { Component } from 'react';
import StarRatingComponent from 'react-star-rating-component';

import './style.css';


export class MovieTile extends Component {

    render() {
        let { title, year, posterUrl, duration, averageRating, onClick } = this.props;

        const genres = this.props.genres || [];
        const actors = this.props.actors || [];

        const genreView = genres.map(genre => genre.name).join(', ');
        const actorsView = actors.map(actor => actor.name).join(', ');

        return (
            <div className="tileContainer">
                <div className="tilePropertyContainer">
                    <table>
                        <tbody>
                            <tr onClick={onClick}>
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
                            <tr>
                                <td colSpan="2">
                                   <StarRatingComponent
                                    style={{zIndex: 2}}
                                    name="test"
                                    value={averageRating} /* number of selected icon (`0` - none, `1` - first) */
                                    starCount={10} /* number of icons in rating, default `5` */
                                    onStarClick={(e, a, b)=>{console.log(e);}} /* on icon click handler */
                                    starColor={"#ffb400"} /* color of selected icons, default `#ffb400` */
                                    emptyStarColor="#fff" /* color of non-selected icons, default `#333` */
                                    editing={false} /* is component available for editing, default `true` */
                                   />
                                </td>
                            </tr>
                        </tbody>
                    </table>
                </div>
                <div onClick={onClick} className="posterContainer">
                    <img alt="Movie poster" src={posterUrl} width="120px" />
                </div>
            </div>
        );
    }
}