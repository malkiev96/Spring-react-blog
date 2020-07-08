import React, {Component} from 'react';
import {Route, Switch} from 'react-router-dom';
import AppHeader from '../common/AppHeader';
import Home from '../home/Home';
import Login from '../user/login/Login';
import Signup from '../user/signup/Signup';
import Profile from '../user/profile/Profile';
import OAuth2RedirectHandler from '../user/oauth2/OAuth2RedirectHandler';
import NotFound from '../common/NotFound';
import {getCurrentUser, getFilters} from '../util/APIUtils';
import {ACCESS_TOKEN} from '../constants';
import PrivateRoute from '../common/PrivateRoute';
import Alert from 'react-s-alert';
import 'react-s-alert/dist/s-alert-default.css';
import 'react-s-alert/dist/s-alert-css-effects/slide.css';
import {Container, Grid, Loader} from "semantic-ui-react";
import RightMenu from "../common/RightMenu";
import Posts from "../post/Posts";
import PostsCategory from "../post/PostsCategory";
import PostsTag from "../post/PostsTag";
import PostsDetail from "../post/PostsDetail";

class App extends Component {
    constructor(props) {
        super(props);
        this.state = {
            currentUser: {
                authenticated: false,
                currentUser: null,
                loading: false
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
                    loading: false
                }
            })
        }).catch(error => {
            this.setState({
                currentUser: {
                    currentUser: null,
                    authenticated: false,
                    loading: false
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
        Alert.success("You're safely logged out!");
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
                <AppHeader authenticated={currentUser.authenticated}
                           categories={categories}
                           onLogout={this.handleLogout}/>
                <Container>
                    <Grid columns={2} stackable>
                        <Grid.Column width={11}>
                            <Switch>
                                <Route exact path="/" component={Home}/>

                                <Route exact path="/posts/:id" render={(props) =>
                                    <PostsDetail categories={categories}
                                                 currentUser={currentUser} {...props} />}/>

                                <Route exact path="/category/:categoryName"
                                       render={(props) => <PostsCategory categories={categories}
                                                                         currentUser={currentUser} {...props} />}/>
                                <Route exact path="/category/:categoryName/p/:page"
                                       render={(props) => <PostsCategory categories={categories}
                                                                         currentUser={currentUser} {...props} />}/>
                                <Route exact path="/posts"
                                       render={(props) => <Posts categories={categories}
                                                                 currentUser={currentUser} {...props} />}/>
                                <Route exact path="/posts/p/:page"
                                       render={(props) => <Posts categories={categories}
                                                                 currentUser={currentUser} {...props} />}/>
                                <Route exact path="/tags/:tagName"
                                       render={(props) => <PostsTag tags={tags}
                                                                    currentUser={currentUser} {...props} />}/>
                                <Route exact path="/tags/:tagName/p/:page"
                                       render={(props) => <PostsTag tags={tags}
                                                                    currentUser={currentUser} {...props} />}/>

                                <PrivateRoute path="/profile" currentUser={currentUser} component={Profile}/>
                                <Route path="/login" render={(props) =>
                                    <Login authenticated={currentUser.authenticated} {...props} />}/>
                                <Route path="/signup" render={(props) =>
                                    <Signup authenticated={currentUser.authenticated} {...props} />}/>
                                <Route path="/oauth2/redirect" component={OAuth2RedirectHandler}/>
                                <Route component={NotFound}/>
                            </Switch>
                        </Grid.Column>
                        <Grid.Column width={5}>
                            <RightMenu tags={tags} categories={categories}/>
                        </Grid.Column>
                    </Grid>
                </Container>
                <Alert stack={{limit: 3}} timeout={3000} position='top-right' effect='slide' offset={65}/>
            </div>
        );
    }
}

export default App;
