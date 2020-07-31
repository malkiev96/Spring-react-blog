import React, {Component} from 'react';
import {Button, Form, Segment} from "semantic-ui-react";
import SimpleMDE from "react-simplemde-editor";
import "easymde/dist/easymde.min.css";
import Select from 'react-select'
import ImageUploader from "react-images-upload";
import {saveImages, createPost} from '../util/APIUtils';
import Alert from "react-s-alert";
import {Redirect} from "react-router-dom";

class Publish extends Component {

    constructor(props) {
        super(props);

        const allCategories = props.categories.map(({id, name}) => ({id: id, value: id, label: name}))
        const allTags = props.tags.map(({id, name}) => ({id: id, value: id, label: name}))

        this.state = {
            allCategories: allCategories,
            allTags: allTags,
            title: '',
            description: '',
            text: '',
            posted: false,
            category: null,
            tags: [],
            images: [],
            preview: null,
            previewLoading: false,
            loading: false,
            textError: false,
            categoryError: false,
            tagsError: false,
            createdPost: null
        }
        this.handleChange = this.handleChange.bind(this);
        this.submitForm = this.submitForm.bind(this);
    }

    handleChange(evt) {
        this.setState({
            ...this.state, [evt.target.name]: evt.target.value
        })
    }

    loadPreview(image) {
        if (image) {
            this.setState({previewLoading: true})
            saveImages(image).then(result => {
                this.setState({
                    preview: result['content'][0],
                    previewLoading: false
                })
            }).catch(error => {
                this.setState({previewLoading: false})
                Alert.error((error && error.message) || 'Не удалось загрузить изображение, попробуйте еще раз');
            })
        } else {
            this.setState({
                preview: null
            })
        }
    }

    submitForm() {
        const {
            previewLoading, loading, preview,
            title, description, text, posted, category, tags, images
        } = this.state

        if (title === '' || description === '')
            return;

        if (text === '') {
            this.setState({textError: true})
            return;
        }
        if (category === null) {
            this.setState({categoryError: true})
            return;
        }
        if (tags === null || tags.length === 0) {
            this.setState({tagsError: true})
            return;
        }
        if (previewLoading || loading) {
            return;
        }

        let tagIds = []
        tags.map(tag => tagIds.push(tag.id))
        let previewId = preview ? preview.id : null

        let postRequest = {
            title: title,
            description: description,
            text: text,
            posted: posted,
            previewId: previewId,
            categoryId: category.id,
            tagIds: tagIds
        }

        if (images.length !== 0) {
            this.setState({loading: true})
            saveImages(images).then(result => {
                this.setState({loading: false})
                let imageIds = []
                result['content'].map(img => imageIds.push(img.id))
                postRequest = {
                    ...postRequest,
                    imageIds: imageIds
                }
                this.savePost(postRequest)
            }).catch(error => {
                this.setState({loading: false})
                Alert.error((error && error.message) || 'Не удалось загрузить изображение, попробуйте еще раз');
            })
        } else {
            this.savePost(postRequest)
        }
    }

    savePost(postRequest) {
        this.setState({loading: true})
        createPost(postRequest).then(result => {
            this.setState({loading: false, createdPost: result})
            Alert.success("Публикация успешно сохранена")
        }).catch(error => {
            this.setState({loading: false})
            Alert.error((error && error.message) || 'Не удалось сохранить публикацию, попробуйте еще раз');
        })
    }

    render() {
        const {
            allCategories, allTags, title, description, text,
            textError, categoryError, tagsError, loading, createdPost
        } = this.state
        const {currentUser} = this.props.currentUser

        if (createdPost != null) return <Redirect to={{pathname: "/posts/" + createdPost.id}}/>

        return (
            <div>
                <Segment>
                    <Form loading={loading} error={textError || categoryError || tagsError} onSubmit={this.submitForm}>
                        <Form.Field required>
                            <label>Заголовок</label>
                            <Form.Input required placeholder={'Заголовок'}
                                        value={title}
                                        onChange={this.handleChange}
                                        name={'title'}/>
                        </Form.Field>
                        <Form.Field required>
                            <label>Краткое описание</label>
                            <Form.Input required placeholder={'Краткое описание'}
                                        value={description}
                                        onChange={this.handleChange}
                                        name={'description'}/>
                        </Form.Field>
                        <Form.Field>
                            <label>Превью к публикации</label>
                            <ImageUploader
                                withPreview
                                singleImage
                                onChange={(file => this.loadPreview(file))}
                                buttonText='Загрузить'
                                label='Формат: jpg, gif, png, Максимальный размер файла: 5Mb.'
                                imgExtension={['.jpg', '.gif', '.png', '.jpeg']}
                                maxFileSize={5242880}
                            />
                        </Form.Field>
                        <Form.Field required error={textError}>
                            <label>Текст</label>
                            <SimpleMDE value={text}
                                       onChange={(value) => this.setState({text: value, textError: false})}/>
                        </Form.Field>
                        <Form.Field required error={categoryError}>
                            <label>Категория</label>
                            <Select onChange={(value) => this.setState({category: value, categoryError: false})}
                                    options={allCategories}/>
                        </Form.Field>
                        <Form.Field required error={tagsError}>
                            <label>Теги</label>
                            <Select isMulti
                                    onChange={(values) => this.setState({tags: values, tagsError: false})}
                                    options={allTags}
                                    closeMenuOnSelect={false}/>
                        </Form.Field>
                        <Form.Field>
                            <label>Прикрепить изображения</label>
                            <ImageUploader
                                withPreview
                                onChange={(files => this.setState({images: files}))}
                                buttonText='Загрузить'
                                label='Формат: jpg, gif, png, Максимальный размер файла: 5Mb.'
                                imgExtension={['.jpg', '.gif', '.png', '.jpeg']}
                                maxFileSize={5242880}
                            />
                        </Form.Field>
                        {
                            currentUser.role === 'ROLE_ADMIN' &&
                            <Button onClick={() => this.setState({posted: false})}>
                                Сохранить без публикации
                            </Button>
                        }
                        {
                            currentUser.role === 'ROLE_ADMIN' &&
                            <Button primary onClick={() => this.setState({posted: true})}>
                                Опубликовать
                            </Button>
                        }
                        {
                            currentUser.role === 'ROLE_USER' &&
                            <Button onClick={() => this.setState({posted: false})}>
                                Сохранить без публикации
                            </Button>
                        }
                        {
                            currentUser.role === 'ROLE_USER' &&
                            <Button primary onClick={() => this.setState({posted: true})}>
                                Опубликовать после проверки
                            </Button>
                        }
                    </Form>
                </Segment>
            </div>
        )
    }
}

export default Publish;