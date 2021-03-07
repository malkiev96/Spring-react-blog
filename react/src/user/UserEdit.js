import React, {Component} from 'react';
import NotFound from "../common/notFound/NotFound";
import {updateUser} from '../service/UserService';
import {Button, Form, Grid, Header, Image, Segment} from "semantic-ui-react";
import ImageUploader from 'react-images-upload';
import Alert from "react-s-alert";
import {getDocumentSrc, saveDocument} from "../service/DocumentService";

class UserEdit extends Component {

    constructor(props) {
        super(props);
        const id = parseInt(this.props.match.params.id, 10)
        const {currentUser} = props.currentUser;
        this.state = {
            currentUser: currentUser,
            id: id,
            name: currentUser.name,
            city: currentUser.city,
            birthDate: currentUser.birthDate,
            about: currentUser.about,
            imageUrl: currentUser.imageUrl
        }
        this.handleChange = this.handleChange.bind(this);
        this.submitForm = this.submitForm.bind(this);
    }

    handleChange(evt) {
        this.setState({
            ...this.state, [evt.target.name]: evt.target.value
        })
    }

    loadAvatar(image) {
        if (image) {
            this.setState({imageLoading: true})
            saveDocument(image).then(result => {
                this.setState({
                    imageUrl: getDocumentSrc(result.id),
                    imageLoading: false
                })
            }).catch(error => {
                this.setState({imageLoading: false})
                Alert.error((error && error.message) || 'Не удалось загрузить изображение, попробуйте еще раз');
            })
        } else {
            this.setState({
                imageUrl: null
            })
        }
    }

    submitForm() {
        const {id, name, city, birthDate, about, imageUrl} = this.state
        const userRequest = {name, city, birthDate, about, imageUrl}
        updateUser(id, userRequest).then(result => {
            Alert.success("Профиль успешно обновлен")
            this.setState({
                currentUser: result
            })
        }).catch(error => {
            Alert.error((error && error.message) || 'Что то пошло не так, попробуйте еще раз');
        })
    }

    render() {
        const {id, name, city, birthDate, about, imageUrl, currentUser} = this.state
        if (currentUser.id !== id) return <NotFound/>
        document.title = 'Редактирование профиля - ' + name
        return (
            <div>
                <Segment>
                    <Header as='h2'>
                        <Image avatar src={imageUrl}/>
                        {currentUser.name}
                    </Header>
                    <Grid columns={2} stackable>
                        <Grid.Column>
                            <Form onSubmit={this.submitForm}>
                                <Form.Field required>
                                    <label>Имя</label>
                                    <input onChange={this.handleChange}
                                           required
                                           name={'name'}
                                           value={name}
                                           placeholder='Имя'/>
                                </Form.Field>
                                <Form.Field>
                                    <label>Город</label>
                                    <input onChange={this.handleChange}
                                           name={'city'}
                                           value={city} placeholder='Город'/>
                                </Form.Field>
                                <Form.Field>
                                    <label>Дата рождения</label>
                                    <input onChange={this.handleChange}
                                           name={'birthDate'}
                                           value={birthDate} type='date' placeholder='Email'/>
                                </Form.Field>
                                <Form.Field>
                                    <label>О себе</label>
                                    <input onChange={this.handleChange}
                                           name={'about'}
                                           value={about} placeholder='О себе'/>
                                </Form.Field>
                                <Button style={{backgroundColor: '#175e6b'}}
                                        primary type='submit'>Сохранить</Button>
                            </Form>
                        </Grid.Column>
                        <Grid.Column>
                            <Header textAlign='center'>Аватар</Header>
                            <ImageUploader
                                withIcon
                                buttonText='Загрузить'
                                onChange={(file => this.loadAvatar(file))}
                                singleImage
                                label='Формат: jpg, gif, png,
                                                Максимальный размер файла: 1Mb.'
                                imgExtension={['.jpg', '.gif', '.png', '.jpeg']}
                                maxFileSize={1048576}
                            />
                        </Grid.Column>
                    </Grid>
                </Segment>
            </div>
        )
    }

}

export default UserEdit;