import React from 'react';
import Modal from 'react-modal';
import { MovieTile } from '../';
import '../../../node_modules/bootstrap/dist/css/bootstrap.min.css';

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
    return (
        <div>
            <Modal
                isOpen={modalIsOpen}
                contentLabel="Movie Details"
                style={customStyles}
            >
                <h2>{movie.title}</h2>
                <MovieTile {...movie} />
                <button onClick={closeModal}>close</button>
            </Modal>
        </div>
    );
}

export { MovieModal };