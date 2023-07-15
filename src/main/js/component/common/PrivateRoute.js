import React from 'react';
import {Redirect, Route} from "react-router-dom";

const PrivateRoute = ({component: Component, currentUser, ...rest}) => (
    <Route
        {...rest}
        render={props => currentUser.authenticated
            ? (<Component currentUser={currentUser} {...rest} {...props} />)
            : (<Redirect
                to={{pathname: '/login', state: {from: props.location}}}/>)}
    />
);

export default PrivateRoute