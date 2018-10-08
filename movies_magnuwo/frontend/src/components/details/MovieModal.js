import React from 'react';
import Modal from 'react-modal';
import StarRatingComponent from 'react-star-rating-component';

import '../../../node_modules/bootstrap/dist/css/bootstrap.min.css';
import './style.css';

Modal.setAppElement('#root')

const customStyles = {
    content: {
        backgroundColor: '#364046',
        color: '#e6e6e6',
        height: '500px',
        top: '50%',
        left: '50%',
        right: 'auto',
        bottom: 'auto',
        marginRight: '-50%',
        transform: 'translate(-50%, -50%)'
    }
};
const MovieModal = ({ modalIsOpen, movie, closeModal }) => {
    const {year, posterUrl, duration, averageRating, createdDate, releaseDate, plot} = movie || {};
    const genres = movie.genres || [];
    const actors = movie.actors || [];

    const genreView = genres.map(genre => genre.name).join(', ');
    const actorsView = actors.map(actor => actor.name).join(', ');
    return (
        <div>
            <Modal
                isOpen={modalIsOpen}
                contentLabel="Movie Details"
                style={customStyles}
            >
                <div className="modalHeaderContainer">
                    <h2 style={{display: 'inline-flex'}}>{movie.title}</h2>
                    <button className="closeButton" onClick={closeModal}>X</button>
                </div>
                <div className="movieModalContainer">
                    <div className="posterContainer">
                        <img alt="Movie poster" src={posterUrl} className="posterImg" />
                    </div>
                <div className="modalPropertyContainer">
                    <table className="modalProperyTable">
                        <tbody>
                            <tr>
                                <td><span className="tileHeader">Year:</span></td>
                                <td>{year}</td>
                            </tr>
                            <tr>
                                <td><span className="tileHeader">Created date:</span></td>
                                <td>{createdDate}</td>
                            </tr>
                            <tr>
                                <td><span className="tileHeader">Release date:</span></td>
                                <td>{releaseDate}</td>
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
                                <td colSpan="2"><span className="tileHeader">Avg. rating:</span></td>
                            </tr>
                            <tr>
                                <td colSpan="2">
                                    <div className="modalAvgRatingContainer">
                                   <StarRatingComponent
                                    name="avgRating"
                                    value={averageRating} /* number of selected icon (`0` - none, `1` - first) */
                                    starCount={10} /* number of icons in rating, default `5` */
                                    emptyStarColor="#e6e6e6" /* color of non-selected icons, default `#333` */
                                    editing={false} /* is component available for editing, default `true` */
                                   />
                                   <span style={{marginLeft: '5px'}}>({averageRating}/10)</span>
                                   </div>
                                </td>
                            </tr>
                            <tr>
                                <td><span className="tileHeader">Plot:</span></td>
                                <td>{plot}</td>
                            </tr>
                        </tbody>
                    </table>
                </div>
            </div>
            </Modal>
        </div>
    );
}

export { MovieModal };