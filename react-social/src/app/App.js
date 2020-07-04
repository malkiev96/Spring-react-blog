import React, {Component} from 'react';
import {
    Route,
    Switch
} from 'react-router-dom';
import AppHeader from '../common/AppHeader';
import Home from '../home/Home';
import Login from '../user/login/Login';
import Signup from '../user/signup/Signup';
import Profile from '../user/profile/Profile';
import OAuth2RedirectHandler from '../user/oauth2/OAuth2RedirectHandler';
import NotFound from '../common/NotFound';
import {getCurrentUser} from '../util/APIUtils';
import {ACCESS_TOKEN} from '../constants';
import PrivateRoute from '../common/PrivateRoute';
import Alert from 'react-s-alert';
import 'react-s-alert/dist/s-alert-default.css';
import 'react-s-alert/dist/s-alert-css-effects/slide.css';
import {Container, Loader} from "semantic-ui-react";

class App extends Component {
    constructor(props) {
        super(props);
        this.state = {
            authenticated: false,
            currentUser: null,
            loading: false
        }
        this.loadCurrentlyLoggedInUser = this.loadCurrentlyLoggedInUser.bind(this);
        this.handleLogout = this.handleLogout.bind(this);
    }

    loadCurrentlyLoggedInUser() {
        getCurrentUser().then(response => {
            this.setState({
                currentUser: response,
                authenticated: true,
                loading: false
            })
        }).catch(error => {
            this.setState({
                loading: false
            })
        })
    }

    handleLogout() {
        localStorage.removeItem(ACCESS_TOKEN);
        this.setState({
            authenticated: false,
            currentUser: null
        });
        Alert.success("You're safely logged out!");
    }

    componentDidMount() {
        this.loadCurrentlyLoggedInUser();
    }

    render() {

        const {loading, authenticated, currentUser} = this.state

        if (loading) return <Loader/>

        return (
            <div>
                <AppHeader authenticated={authenticated} onLogout={this.handleLogout}/>
                <Container>
                    <Switch>
                        <Route exact path="/" component={Home}/>
                        <PrivateRoute path="/profile"
                                      authenticated={authenticated}
                                      currentUser={currentUser}
                                      component={Profile}/>
                        <Route path="/login"
                               render={(props) =>
                                   <Login authenticated={authenticated} {...props} />}/>
                        <Route path="/signup"
                               render={(props) =>
                                   <Signup authenticated={authenticated} {...props} />}/>
                        <Route path="/oauth2/redirect" component={OAuth2RedirectHandler}/>
                        <Route component={NotFound}/>
                    </Switch>
                </Container>
                <Alert stack={{limit: 3}} timeout={3000} position='top-right' effect='slide' offset={65}/>
            </div>
        );
    }
}

export default App;
