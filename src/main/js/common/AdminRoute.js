import React from 'react';
import {Route} from "react-router-dom";
import NotFound from "./notFound/NotFound";

const AdminRoute = ({component: Component, currentUser, ...rest}) => (
    <Route
        {...rest}
        render={props => (currentUser.authenticated && currentUser.currentUser.admin)
            ? (<Component currentUser={currentUser} {...rest} {...props} />)
            : (<NotFound/>)}
    />
);

export default AdminRoute