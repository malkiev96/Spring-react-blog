import React, {Component} from 'react';
import NotFound from "../common/NotFound";
import {Button, Form, Grid, Header, Image, Segment} from "semantic-ui-react";
import ImageUploader from 'react-images-upload';

class UserEdit extends Component {

    constructor(props) {
        super(props);
        const id = parseInt(this.props.match.params.id)
        this.state = {
            id: id
        }
    }

    render() {
        const {currentUser} = this.props.currentUser
        const {id} = this.state
        if (currentUser.id !== id) return <NotFound/>
        return (
            <div>
                <Segment>
                    <Header as='h2'>
                        <Image avatar src={currentUser.imageUrl}/>
                        {currentUser.name}
                    </Header>
                    <Grid columns={2} stackable>
                        <Grid.Column>
                            <Form>
                                <Form.Field>
                                    <label>Имя</label>
                                    <input value={currentUser.name} placeholder='Имя'/>
                                </Form.Field>
                                <Form.Field>
                                    <label>Город</label>
                                    <input value={currentUser.city} placeholder='Город'/>
                                </Form.Field>
                                <Form.Field>
                                    <label>Дата рождения</label>
                                    <input type='date' placeholder='Email'/>
                                </Form.Field>
                                <Form.Field>
                                    <label>О себе</label>
                                    <input value={currentUser.about} placeholder='О себе'/>
                                </Form.Field>
                                <Button primary type='submit'>Сохранить</Button>
                            </Form>
                        </Grid.Column>
                        <Grid.Column>
                            <Header textAlign='center'>Аватар</Header>
                            <ImageUploader
                                withIcon
                                buttonText='Загрузить'
                                onChange={this.onDrop}
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