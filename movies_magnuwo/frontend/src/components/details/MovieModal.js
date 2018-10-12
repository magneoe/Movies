import React, { Component } from 'react';
import Modal from 'react-modal';
import StarRatingComponent from 'react-star-rating-component';
import { connect } from 'react-redux';
import { bindActionCreators } from 'redux';

import { vote, loadComments, postComment } from '../../pages/home/actions';
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
        transform: 'translate(-50%, -50%)',
        maxWidth: '50%',
    },
    overlay: {
        backgroundColor: 'rgba(0, 0, 0, 1)'
    }
};
class MovieModal extends Component {

    constructor(props) {
        super(props);
        this.state = {
            selectedMovie: {},
        }
    }
    /*
    * Should trigger rerendering whenever the
    * selected movie has been updated in the store.
    */
    componentDidUpdate() {
        const { movieId } = this.props;
        const content = this.props.movieData.content || [];
        const selectedMovie = content.find(movie => {
            return movie.id === movieId;
        });
        if (this.state.selectedMovie === selectedMovie)
            return;

        if (selectedMovie) {
            this.setState({ selectedMovie });
        }
    }
    onVote = (nextValue, movieId) => {
        if (nextValue) {
            this.props.actions.vote(nextValue, movieId);
        }
    }
    onPostComment = (event) => {
        event.preventDefault();
        let form = document.getElementById("newCommentForm");
        //Validate ?
        let formData = new FormData(form);
        this.props.actions.postComment(formData, this.state.selectedMovie.id);
        //Clear field
        document.getElementById("title").value = "";
        document.getElementById("comment").value = "";
    }

    render() {
        const { modalIsOpen, movieId, closeModal } = this.props;
        const movie = this.state.selectedMovie;
        const { year, posterUrl, duration, averageRating, createdDate, releaseDate, plot } = movie || {};
        const commentEntry = this.props.comments.find(entry => entry.movieId === movieId) || {};
        const commentData = commentEntry.data || {};
        const commentList = commentData.content || [];

        const genres = movie.genres || [];
        const actors = movie.actors || [];

        const userRatings = this.props.userRatings || [];
        const currentRating = userRatings.find(userRating => userRating.movieId === movieId) || { rating: 0 };

        const genreView = genres.map(genre => genre.name).join(', ');
        const actorsView = actors.map(actor => actor.name).join(', ');
        return (
            <div>
                <Modal
                    isOpen={modalIsOpen}
                    contentLabel="Movie Details"
                    style={customStyles}
                    overlayClassName=""
                >
                    <div className="modalHeaderContainer">
                        <h2 style={{ display: 'inline-flex' }}>{movie.title}</h2>
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
                                                <span style={{ marginLeft: '5px' }}>({averageRating}/10)</span>
                                            </div>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td><span className="tileHeader">Plot:</span></td>
                                        <td>{plot}</td>
                                    </tr>
                                    <tr>
                                        <td colSpan="2"><span className="tileHeader">You rated this movie:</span></td>
                                    </tr>
                                    <tr>
                                        <td colSpan="2">
                                            <StarRatingComponent
                                                name="avgRating"
                                                value={currentRating.rating} /* number of selected icon (`0` - none, `1` - first) */
                                                starCount={10} /* number of icons in rating, default `5` */
                                                emptyStarColor="#e6e6e6" /* color of non-selected icons, default `#333` */
                                                editing={true} /* is component available for editing, default `true` */
                                                onStarClick={(nextValue, prevValue, name) => { this.onVote(nextValue, movie.id); }} /* on icon click handler */
                                            />
                                            <span style={{ marginLeft: '5px' }}>({currentRating.rating || 0}/10)</span>
                                        </td>
                                    </tr>
                                </tbody>
                            </table>
                        </div>
                        <span className="tileHeader" style={{margin: '10px'}}>Comments:</span>
                        <div className="commentListContainer">
                            {commentView(commentList)}
                        </div>
                        <div style={{ alignSelf: 'flex-start'}}>
                            <span>Post a comment:</span>
                            {newCommentView(this.onPostComment)}
                        </div>
                    </div>
                </Modal>
            </div>
        );
    }
}

const commentView = (commentList) =>
    commentList.map((entry, index) => {
        return (
            <div key={index} className="commentContainer">
                <div className="commentBodyContainer">
                    <div className="commentHighlightedText">{entry.author.name} {entry.author.lastname}</div>
                    <div className="commentItalicText">{entry.created}</div>
                </div>
                <div className="commentBodyContainer">
                    <div className="commentHighlightedText">{entry.title}</div>
                    <div>{entry.comment}</div>
                </div>
            </div>
        );
    });
const newCommentView = (onPostComment) =>
    <div>
        <form method="POST" name="newCommentForm" id="newCommentForm">
            <table>
                <tbody>
                    <tr>
                        <td><input style={{ width: '100%' }} type="text" name="title" id="title" placeholder="Title" /></td>
                    </tr>
                    <tr>
                        <td><textarea name="comment" id="comment" rows="5" maxLength="150"   placeholder="Comment" /></td>
                    </tr>
                    <tr>
                        <td><input type="submit"
                            name="submitComment" value="Post comment" onClick={onPostComment} /></td>
                    </tr>
                </tbody>
            </table>
        </form>
    </div>


export default connect(
    (state) => ({
        requestConfig: state.movieReducer.requestConfig,
        movieData: state.movieReducer.movieData || {},
        loading: state.movieReducer.loading,
        userRatings: state.movieReducer.userRatings || [],
        comments: state.movieReducer.comments || []
    }),
    (dispatch) => ({
        actions: bindActionCreators(Object.assign({},
            { vote, loadComments, postComment }), dispatch)

    })
)(MovieModal)