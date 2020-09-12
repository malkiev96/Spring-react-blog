import React, {Component} from 'react';
import {Route, Switch} from 'react-router-dom';
import AppHeader from '../common/header/AppHeader';
import Login from '../user/Login';
import Signup from '../user/Signup';
import OAuth2RedirectHandler from '../util/OAuth2RedirectHandler';
import NotFound from '../common/notFound/NotFound';
import {getFilters} from '../util/APIUtils';
import {getCurrentUser} from "../service/UserService";
import {ACCESS_TOKEN} from '../util/Constants';
import PrivateRoute from '../common/PrivateRoute';
import Alert from 'react-s-alert';
import 'react-s-alert/dist/s-alert-default.css';
import 'react-s-alert/dist/s-alert-css-effects/slide.css';
import {Container, Loader} from "semantic-ui-react";
import Posts from "../post/Posts";
import PostsDetail from "../post/PostsDetail";
import UserDetail from "../user/UserDetail";
import Contacts from "./Contacts";
import UserEdit from "../user/UserEdit";
import Publish from "../post/Publish";
import AppFooter from "../common/footer/AppFooter";
import Home from "./Home";
import ImgGallery from "./ImgGallery";
import Admin from "../admin/Admin";
import AdminRoute from "../common/AdminRoute";

class App extends Component {
    constructor(props) {
        super(props);
        this.state = {
            currentUser: {
                authenticated: false,
                currentUser: null,
                loading: false,
                admin: false
            },
            loading: true,
            categories: [],
            tags: []
        }
        this.loadCurrentlyLoggedInUser = this.loadCurrentlyLoggedInUser.bind(this);
        this.loadFilters = this.loadFilters.bind(this);
        this.handleLogout = this.handleLogout.bind(this);
    }

    loadCurrentlyLoggedInUser() {
        getCurrentUser().then(response => {
            this.setState({
                currentUser: {
                    currentUser: response,
                    authenticated: true,
                    loading: false,
                    admin: response.admin
                }
            })
        }).catch(error => {
            this.setState({
                currentUser: {
                    currentUser: null,
                    authenticated: false,
                    loading: false,
                    admin: false
                }
            })
        })
    }

    loadFilters() {
        getFilters().then(response => {
            this.setState({
                categories: response.categories,
                tags: response.tags,
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
            currentUser: {
                currentUser: null,
                authenticated: false
            }
        })
    }

    componentDidMount() {
        this.loadCurrentlyLoggedInUser()
        this.loadFilters()
    }

    render() {
        const {currentUser, loading, categories, tags} = this.state
        if (currentUser.loading || loading) return <Loader/>

        return (
            <div>
                <AppHeader authenticated={currentUser.authenticated} currentUser={currentUser.currentUser}
                           categories={categories} onLogout={this.handleLogout}/>
                <Container style={{padding: '0', paddingTop: '15px'}}>
                    <Switch>
                        <Route exact path="/" render={(props) =>
                            <Home categories={categories} currentUser={currentUser} {...props} />}/>
                        <Route exact path="/user/:id" render={(props) =>
                            <UserDetail currentUser={currentUser} {...props}/>}/>
                        <PrivateRoute path="/user/:id/edit" currentUser={currentUser} component={UserEdit}/>
                        <AdminRoute path="/admin" currentUser={currentUser} component={Admin}/>
                        <PrivateRoute path="/publish" categories={categories} tags={tags}
                                      currentUser={currentUser} component={Publish}/>
                        <Route exact path="/contacts" render={(props) =>
                            <Contacts currentUser={currentUser} {...props}/>}/>
                        <Route exact path="/gallery" render={(props) =>
                            <ImgGallery currentUser={currentUser} {...props}/>}/>
                        <Route exact path="/post/:id" render={(props) =>
                            <PostsDetail categories={categories} currentUser={currentUser} {...props} />}/>
                        <PrivateRoute path="/post/:id/edit" categories={categories} tags={tags}
                                      currentUser={currentUser} component={Publish}/>

                        <Route exact path="/category/:categoryCode/:page?" render={(props) =>
                            <Posts categories={categories} tags={tags} currentUser={currentUser} {...props} />}/>
                        <Route exact path="/posts/:page?" render={(props) =>
                            <Posts categories={categories} tags={tags} currentUser={currentUser} {...props} />}/>
                        <Route exact path="/tags/:tagCode/:page?" render={(props) =>
                            <Posts tags={tags} currentUser={currentUser} {...props} />}/>

                        <Route path="/login" render={(props) =>
                            <Login authenticated={currentUser.authenticated} {...props} />}/>
                        <Route path="/signup" render={(props) =>
                            <Signup authenticated={currentUser.authenticated} {...props} />}/>
                        <Route path="/oauth2/redirect" component={OAuth2RedirectHandler}/>
                        <Route component={NotFound}/>
                    </Switch>
                </Container>
                <AppFooter/>
                <Alert stack={{limit: 3}} timeout={3000} position='top-right' effect='slide' offset={65}/>
            </div>
        );
    }
}

export default App;
