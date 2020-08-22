import React, {Component} from 'react';
import './AppHeader.css'
import {Button, Container, Dropdown, Grid, Header, Icon, Menu, Segment} from "semantic-ui-react";
import {Link} from "react-router-dom";
import {CarouselProvider, Slide, Slider} from "pure-react-carousel";

class AppHeader extends Component {
    state = {
        dropdownMenuStyle: {
            display: "none"
        }
    };

    handleToggleDropdownMenu = () => {
        let newState = Object.assign({}, this.state);
        if (newState.dropdownMenuStyle.display === "none") {
            newState.dropdownMenuStyle = {display: "flex", backgroundColor: "#175e6b"};
        } else {
            newState.dropdownMenuStyle = {display: "none", backgroundColor: "#175e6b"};
        }
        this.setState(newState);
    };

    render() {
        const authenticated = this.props.authenticated;
        return (
            <div>
                <div className='app-header'>
                    <Container style={{padding: '0'}}>
                        <Grid className="tablet computer only">
                            <Menu style={{backgroundColor: '#175e6b'}} inverted borderless fluid size="huge">
                                <Menu.Item active={window.location.pathname === '/'}
                                           as={Link} to='/' name='Главная'/>
                                <Menu.Item active={window.location.pathname === '/posts'}
                                           as={Link} to='/posts' name='Статьи'/>
                                <HeaderCategory categories={this.props.categories}/>
                                <Menu.Item active={window.location.pathname === '/about'}
                                           as={Link} to='/about' name='О проекте'/>
                                <Menu.Item active={window.location.pathname === '/contacts'}
                                           as={Link} to='/contacts' name='Контакты'/>
                                {
                                    authenticated ?
                                        <Menu.Menu position='right'>
                                            <Dropdown item simple text={this.props.currentUser.name}>
                                                <Dropdown.Menu style={{backgroundColor: '#175e6b'}}>
                                                    <Dropdown.Item as={Link}
                                                                   to={'/user/' + this.props.currentUser.id}
                                                                   key={0}>
                                                        <div style={{color: 'white'}}>Профиль</div>
                                                    </Dropdown.Item>
                                                    <Dropdown.Item as={Link} to={'/publish'} key={1}>
                                                        <div style={{color: 'white'}}>Публикация</div>
                                                    </Dropdown.Item>
                                                    <Dropdown.Item onClick={this.props.onLogout} key={2}>
                                                        <div style={{color: 'white'}}>Выход</div>
                                                    </Dropdown.Item>
                                                </Dropdown.Menu>
                                            </Dropdown>
                                        </Menu.Menu> :
                                        <Menu.Menu position='right'>
                                            <Menu.Item active={window.location.pathname === '/login'}
                                                       as={Link} to='/login' name='Войти'/>
                                            <Menu.Item active={window.location.pathname === '/signup'}
                                                       as={Link} to='/signup' name='Регистрация'/>
                                        </Menu.Menu>
                                }
                            </Menu>
                        </Grid>
                        <Grid padded className="mobile only">
                            <Menu style={{backgroundColor: '#175e6b'}} inverted borderless fluid size="huge">
                                <Menu.Menu position="right">
                                    <Menu.Item>
                                        <Button inverted icon basic toggle
                                                onClick={this.handleToggleDropdownMenu}>
                                            <Icon name="content"/>
                                        </Button>
                                    </Menu.Item>
                                </Menu.Menu>
                                <Menu inverted vertical borderless fluid
                                      style={this.state.dropdownMenuStyle}>
                                    <Menu.Item active={window.location.pathname === '/'}
                                               as={Link} to='/' name='Главная'/>
                                    <Menu.Item active={window.location.pathname === '/posts'}
                                               as={Link} to='/posts' name='Статьи'/>
                                    <HeaderCategory categories={this.props.categories}/>
                                    <Menu.Item active={window.location.pathname === '/about'}
                                               as={Link} to='/about' name='О проекте'/>
                                    <Menu.Item active={window.location.pathname === '/contacts'}
                                               as={Link} to='/contacts' name='Контакты'/>
                                    {
                                        authenticated &&
                                        <Dropdown item simple text={this.props.currentUser.name}>
                                            <Dropdown.Menu style={{backgroundColor: '#175e6b'}}>
                                                <Dropdown.Item as={Link}
                                                               to={'/user/' + this.props.currentUser.id}
                                                               key={0}>
                                                    <div style={{color: 'white'}}>Профиль</div>
                                                </Dropdown.Item>
                                                <Dropdown.Item as={Link} to={'/publish'} key={1}>
                                                    <div style={{color: 'white'}}>Публикация</div>
                                                </Dropdown.Item>
                                                <Dropdown.Item onClick={this.props.onLogout} key={2}>
                                                    <div style={{color: 'white'}}>Выход</div>
                                                </Dropdown.Item>
                                            </Dropdown.Menu>
                                        </Dropdown>
                                    }
                                    {
                                        !authenticated &&
                                        <Menu.Item active={window.location.pathname === '/login'}
                                                   as={Link} to='/login' name='Войти'/>
                                    }
                                    {
                                        !authenticated &&
                                        <Menu.Item active={window.location.pathname === '/signup'}
                                                   as={Link} to='/signup' name='Регистрация'/>
                                    }
                                </Menu>
                            </Menu>
                        </Grid>
                    </Container>
                </div>
                {
                    window.location.pathname === '/' &&
                    <HeaderCarousel/>
                }
            </div>
        )
    }
}

const HeaderCategory = ({categories}) => {
    return categories.map(cat => (
        <Dropdown key={cat.id} item simple text={cat.name} as={Link} to={'/category/' + cat.description}>
            <Dropdown.Menu style={{backgroundColor: '#175e6b'}}>
                {
                    cat.childs.map(c => {
                        return (
                            <Dropdown.Item key={c.id} as={Link} to={'/category/' + c.description}>
                                <div style={{color: 'white'}}>{c.name}</div>
                            </Dropdown.Item>
                        )
                    })
                }
            </Dropdown.Menu>
        </Dropdown>
    ))
}

const HeaderCarousel = () => {
    return (
        <CarouselProvider isPlaying isIntrinsicHeight step
                          naturalSlideWidth={100} totalSlides={2} naturalSlideHeight={1}>
            <Slider>
                <Slide index={1}>
                    <Segment inverted color='grey' vertical textAlign="center">
                        <Container text className="active">
                            <Header inverted as="h1">
                                Example headline.
                            </Header>
                            <p>
                                Note: If you're viewing this page via a <code>file://</code>
                                URL, the "next" and "previous" Glyphicon buttons on the left and
                                right might not load/display properly due to web browser
                                security rules.
                            </p>
                            <Button primary size="huge">
                                Sign up today
                            </Button>
                        </Container>
                    </Segment>
                </Slide>
                <Slide index={2}>
                    <Segment inverted color='grey' vertical textAlign="center">
                        <Container text className="active">
                            <Header inverted as="h1">
                                Example headline.
                            </Header>
                            <p>
                                Note: If you're viewing this page via a <code>file://</code>
                                URL, the "next" and "previous" Glyphicon buttons on the left and
                                right might not load/display properly due to web browser
                                security rules.
                            </p>
                            <Button primary size="huge">
                                Sign up today
                            </Button>
                        </Container>
                    </Segment>
                </Slide>
            </Slider>
        </CarouselProvider>
    )
}

export default AppHeader;