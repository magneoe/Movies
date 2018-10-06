import React from 'react';

const FullSymbol = (props) => {
    return(
        <span>Full</span>
    );
}

const EmptySymbol = (props) => {
    return(
        <span style={{color: 'red'}}>Empty</span>
    );
}

export {
    FullSymbol,
    EmptySymbol
}