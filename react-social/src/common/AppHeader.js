import React, {Component} from 'react';
import './AppHeader.css'
import {Button, Container, Dropdown, Grid, Icon, Menu} from "semantic-ui-react";
import {Link} from "react-router-dom";

class AppHeader extends Component {
    state = {
        dropdownMenuStyle: {
            display: "none"
        }
    };

    handleToggleDropdownMenu = () => {
        let newState = Object.assign({}, this.state);
        if (newState.dropdownMenuStyle.display === "none") {
            newState.dropdownMenuStyle = {display: "flex"};
        } else {
            newState.dropdownMenuStyle = {display: "none"};
        }
        this.setState(newState);
    };

    render() {
        return (
            <Container className='app-header'>
                <Grid padded className="tablet computer only">
                    <Menu inverted borderless fluid size="huge">
                        <Menu.Item active={window.location.pathname === '/'}
                                   as={Link} to='/' name='Главная'/>
                        <Menu.Item active={window.location.pathname === '/posts'}
                                   as={Link} to='/posts' name='Статьи'/>
                        <HeaderCategory/>
                        <Menu.Item active={window.location.pathname === '/about'}
                                   as={Link} to='/about' name='О проекте'/>
                        <Menu.Item active={window.location.pathname === '/contacts'}
                                   as={Link} to='/contacts' name='Контакты'/>
                        {
                            this.props.authenticated ?
                                <Menu.Menu position='right'>
                                    <Menu.Item active={window.location.pathname === '/profile'}
                                               as={Link} to='/profile' name='Профиль'/>
                                    <Menu.Item onClick={this.props.onLogout} name='Выход'/>
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
                    <Menu inverted borderless fluid size="huge">
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
                            <HeaderCategory/>
                            <Menu.Item active={window.location.pathname === '/about'}
                                       as={Link} to='/about' name='О проекте'/>
                            <Menu.Item active={window.location.pathname === '/contacts'}
                                       as={Link} to='/contacts' name='Контакты'/>
                            {
                                this.props.authenticated &&
                                <Menu.Item active={window.location.pathname === '/profile'}
                                           as={Link} to='/profile' name='Профиль'/>
                            }
                            {
                                this.props.authenticated &&
                                <Menu.Item onClick={this.props.onLogout} name='Выход'/>
                            }
                            {
                                !this.props.authenticated &&
                                <Menu.Item active={window.location.pathname === '/login'}
                                           as={Link} to='/login' name='Войти'/>
                            }
                            {
                                !this.props.authenticated &&
                                <Menu.Item active={window.location.pathname === '/signup'}
                                           as={Link} to='/signup' name='Регистрация'/>
                            }
                        </Menu>
                    </Menu>
                </Grid>
            </Container>
        )
    }
}

const HeaderCategory = (props) => {
    return (
        <Dropdown item text="Категории">
            <Dropdown.Menu>
                <Dropdown.Item as="a" href="#root">
                    Action
                </Dropdown.Item>
                <Dropdown.Item as="a" href="#root">
                    Another Action
                </Dropdown.Item>
                <Dropdown.Item as="a" href="#root">
                    Something else here
                </Dropdown.Item>
                <Dropdown.Divider/>
                <Dropdown.Header>Navbar header</Dropdown.Header>
                <Dropdown.Item as="a" href="#root">
                    Separated link
                </Dropdown.Item>
                <Dropdown.Item as="a" href="#root">
                    One more separated link
                </Dropdown.Item>
            </Dropdown.Menu>
        </Dropdown>
    )
}

export default AppHeader;