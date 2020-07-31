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
                        <HeaderCategory categories={this.props.categories}/>
                        <Menu.Item active={window.location.pathname === '/about'}
                                   as={Link} to='/about' name='О проекте'/>
                        <Menu.Item active={window.location.pathname === '/contacts'}
                                   as={Link} to='/contacts' name='Контакты'/>
                        {
                            this.props.authenticated ?
                                <Menu.Menu position='right'>
                                    <Menu.Item as={Link} to={'/publish'}
                                               active={window.location.pathname === '/publish'}
                                               name='Публикация' icon={'plus'}/>
                                    <Menu.Item active={window.location.pathname === '/profile'}
                                               as={Link} to={'/user/'+this.props.currentUser.id}
                                               name='Профиль'/>
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
                            <HeaderCategory categories={this.props.categories}/>
                            <Menu.Item active={window.location.pathname === '/about'}
                                       as={Link} to='/about' name='О проекте'/>
                            <Menu.Item active={window.location.pathname === '/contacts'}
                                       as={Link} to='/contacts' name='Контакты'/>
                            {
                                this.props.authenticated &&
                                <Menu.Item active={window.location.pathname === '/profile'}
                                           as={Link}
                                           to={'/user/'+this.props.currentUser.id}
                                           name='Профиль'/>
                            }
                            {
                                this.props.authenticated &&
                                <Menu.Item active={window.location.pathname === '/publish'}
                                           as={Link} to={'/publish'} name='Публикация' icon={'plus'}/>
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

const HeaderCategory = ({categories}) => {
    return (
        <Dropdown item simple text="Категории">
            <Dropdown.Menu>
                {
                    categories.map(cat => {
                        if (cat.childs.length !== 0) {
                            return (
                                <Dropdown.Item key={cat.id} as={Link} to={'/category/'+cat.name} >
                                    <Icon name="dropdown"/>
                                    <span className="text">{cat.name}</span>
                                    <CategoryChilds categories={cat.childs}/>
                                </Dropdown.Item>
                            )
                        } else return (
                            <Dropdown.Item as={Link} to={'/category/'+cat.name} key={cat.id}>
                                {cat.name}
                            </Dropdown.Item>
                        )
                    })
                }
            </Dropdown.Menu>
        </Dropdown>
    )
}

const CategoryChilds = ({categories}) => {
    return (
        <Dropdown.Menu>
            {
                categories.map(cat => {
                    return (
                        <Dropdown.Item as={Link} to={'/category/'+cat.name} key={cat.id}>
                            {cat.name}
                        </Dropdown.Item>
                    )
                })
            }
        </Dropdown.Menu>
    )
}

export default AppHeader;