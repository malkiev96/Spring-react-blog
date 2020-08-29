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
            newState.dropdownMenuStyle = {display: "flex", backgroundColor: "#175e6b"};
        } else {
            newState.dropdownMenuStyle = {display: "none", backgroundColor: "#175e6b"};
        }
        this.setState(newState);
    };

    render() {
        const authenticated = this.props.authenticated;
        const user = this.props.currentUser;
        const pathname = window.location.pathname;
        return (
            <div>
                <div className='app-header'>
                    <Container style={{padding: '0'}}>
                        <Grid className="tablet computer only">
                            <Menu style={{backgroundColor: '#175e6b'}} inverted borderless fluid size="huge">
                                <Menu.Item active={pathname === '/'}
                                           as={Link} to='/' name='Главная'/>
                                <Menu.Item active={pathname === '/posts'}
                                           as={Link} to='/posts' name='Статьи'/>
                                <HeaderCategory categories={this.props.categories}/>
                                <Menu.Item active={pathname === '/gallery'}
                                           as={Link} to='/gallery' name='Галерея'/>
                                <Menu.Item active={pathname === '/contacts'}
                                           as={Link} to='/contacts' name='Контакты'/>
                                {
                                    authenticated ?
                                        <Menu.Menu position='right'>
                                            <Dropdown item simple text={user.name}>
                                                <Dropdown.Menu style={{backgroundColor: '#175e6b'}}>
                                                    <Dropdown.Item as={Link}
                                                                   to={'/user/' + user.id}
                                                                   key={0}>
                                                        <div style={{color: 'white'}}>Профиль</div>
                                                    </Dropdown.Item>
                                                    <Dropdown.Item as={Link} to={'/publish'} key={1}>
                                                        <div style={{color: 'white'}}>Публикация</div>
                                                    </Dropdown.Item>
                                                    {
                                                        user.admin &&
                                                        <Dropdown.Item as={Link} to={'/admin'} key={2}>
                                                            <div style={{color: 'white'}}>Админка</div>
                                                        </Dropdown.Item>
                                                    }
                                                    <Dropdown.Item onClick={this.props.onLogout} key={3}>
                                                        <div style={{color: 'white'}}>Выход</div>
                                                    </Dropdown.Item>
                                                </Dropdown.Menu>
                                            </Dropdown>
                                        </Menu.Menu> :
                                        <Menu.Menu position='right'>
                                            <Menu.Item active={pathname === '/login'}
                                                       as={Link} to='/login' name='Войти'/>
                                            <Menu.Item active={pathname === '/signup'}
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
                                    <Menu.Item active={pathname === '/'}
                                               as={Link} to='/' name='Главная'/>
                                    <Menu.Item active={pathname === '/posts'}
                                               as={Link} to='/posts' name='Статьи'/>
                                    <HeaderCategory categories={this.props.categories}/>
                                    <Menu.Item active={pathname === '/gallery'}
                                               as={Link} to='/gallery' name='Галерея'/>
                                    <Menu.Item active={pathname === '/contacts'}
                                               as={Link} to='/contacts' name='Контакты'/>
                                    {
                                        authenticated &&
                                        <Dropdown item simple text={user.name}>
                                            <Dropdown.Menu style={{backgroundColor: '#175e6b'}}>
                                                <Dropdown.Item as={Link}
                                                               to={'/user/' + user.id}
                                                               key={0}>
                                                    <div style={{color: 'white'}}>Профиль</div>
                                                </Dropdown.Item>
                                                <Dropdown.Item as={Link} to={'/publish'} key={1}>
                                                    <div style={{color: 'white'}}>Публикация</div>
                                                </Dropdown.Item>
                                                {
                                                    user.admin &&
                                                    <Dropdown.Item as={Link} to={'/admin'} key={2}>
                                                        <div style={{color: 'white'}}>Админка</div>
                                                    </Dropdown.Item>
                                                }
                                                <Dropdown.Item onClick={this.props.onLogout} key={3}>
                                                    <div style={{color: 'white'}}>Выход</div>
                                                </Dropdown.Item>
                                            </Dropdown.Menu>
                                        </Dropdown>
                                    }
                                    {
                                        !authenticated &&
                                        <Menu.Item active={pathname === '/login'}
                                                   as={Link} to='/login' name='Войти'/>
                                    }
                                    {
                                        !authenticated &&
                                        <Menu.Item active={pathname === '/signup'}
                                                   as={Link} to='/signup' name='Регистрация'/>
                                    }
                                </Menu>
                            </Menu>
                        </Grid>
                    </Container>
                </div>
            </div>
        )
    }
}

const HeaderCategory = ({categories}) => {
    return categories.map(cat => !(cat.childs && cat.childs.length !== 0) ? (
        <Menu.Item as={Link}  to={'/category/' + cat.description} name={cat.name}/>
    ) : (
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

export default AppHeader;