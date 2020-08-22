import React, {Component} from 'react';
import {Button, Checkbox, Form, Image, Loader, Segment} from "semantic-ui-react";
import SimpleMDE from "react-simplemde-editor";
import "easymde/dist/easymde.min.css";
import Select from 'react-select'
import ImageUploader from "react-images-upload";
import {saveImages} from '../util/ImageService';
import {createPost, getPostById} from '../util/PostService';
import Alert from "react-s-alert";
import {Redirect} from "react-router-dom";
import NotFound from "../common/NotFound";

const defaultState = () => ({
    title: '',
    description: '',
    text: '',
    posted: false,
    category: null,
    tags: [],
    images: [],
    imageIds: [],
    postImages: [],
    preview: null,
    previewLoading: false,
    loading: false,
    textError: false,
    categoryError: false,
    tagsError: false,
    createdPost: null,
    post: {
        post: null,
        postLoading: true
    }
})

class Publish extends Component {

    constructor(props) {
        super(props);
        const id = parseInt(this.props.match.params.id, 10)
        let edit = !!id
        let allCategories = []
        props.categories.forEach(cat => {
            let childs = cat.childs;
            allCategories.push({id: cat.id, value: cat.id, label: cat.name, isDisabled: childs.length !== 0})
            if (childs.length !== 0)
                childs.map(ch => allCategories.push({id: ch.id, value: ch.id, label: ch.name}))
        })
        const allTags = props.tags.map(({id, name}) => ({id: id, value: id, label: name}))

        this.state = {
            ...defaultState(),
            id: id || null,
            edit: edit,
            allCategories: allCategories,
            allTags: allTags
        }
        this.handleChange = this.handleChange.bind(this);
        this.submitForm = this.submitForm.bind(this);
        this.loadData = this.loadData.bind(this);
    }

    componentDidUpdate(prevProps) {
        if (this.props.location.pathname !== prevProps.location.pathname) {
            const id = parseInt(this.props.match.params.id, 10)
            let edit = !!id
            this.setState({
                ...defaultState(),
                id: id || null,
                edit: edit
            })
            if (edit) this.loadData(id)
        }
    }

    componentDidMount() {
        const {id, edit} = this.state
        if (edit) this.loadData(id)
    }

    handleChange(evt) {
        this.setState({
            ...this.state, [evt.target.name]: evt.target.value
        })
    }

    loadData(id) {
        getPostById(id).then(response => {
            const category = response.category;
            this.setState({
                post: {
                    post: response,
                    error: false,
                    postLoading: false
                },
                posted: response.status==='PUBLISHED',
                title: response.title,
                description: response.description,
                text: response.text,
                preview: response.preview,
                category: {id: category.id, value: category.id, label: category.name},
                tags: response.tags.map(({id, name}) => ({id: id, value: id, label: name})),
                postImages: response.images.map(({id, url}) => ({id: id, url: url})),
                imageIds: response.images.map(({id}) => id)
            })
        }).catch((error) => {
            this.setState({
                post: {
                    post: null,
                    error: true,
                    postLoading: false
                }
            })
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

    deleteImageId(id) {
        const postImages = this.state.postImages.filter(item => item.id !== id)
        this.setState({
            postImages: postImages,
            imageIds: postImages.map(({id}) => id)
        })
    }

    loadImages(files) {
        let image = []
        Array.from(files).forEach(file => image.push(file))
        if (image) {
            this.setState({loading: true})
            saveImages(image).then(result => {
                const images = result['content']
                const {postImages, imageIds} = this.state
                images.forEach(({id, url}) => {
                    postImages.push({id: id, url: url})
                    imageIds.push(id)
                })
                this.setState({
                    loading: false,
                    imageIds: imageIds,
                    postImages: postImages
                })
            }).catch(error => {
                this.setState({loading: false})
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
            previewLoading, loading, preview, id, imageIds,
            title, description, text, posted, category, tags,
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
        let postRequest = {
            id: id,
            title: title,
            description: description,
            text: text,
            posted: posted,
            previewId: preview ? preview.id : null,
            categoryId: category.id,
            imageIds: imageIds,
            tagIds: tags.map(tag => tag.id)
        }
        this.savePost(postRequest)
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
        const {postLoading, error} = this.state.post
        const {
            allCategories, allTags, title, description, text, edit, tags, preview, posted,
            textError, categoryError, tagsError, loading, createdPost, category, postImages
        } = this.state
        if (postLoading && edit) return <Loader active inline='centered'/>
        if (error) return <NotFound/>

        if (createdPost != null) return <Redirect to={{pathname: "/post/" + createdPost.id}}/>
        return <div>
            <Segment>
                <Form loading={loading} error={textError || categoryError || tagsError}
                      onSubmit={this.submitForm}>
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
                                    value={description} onChange={this.handleChange}
                                    name={'description'}/>
                    </Form.Field>
                    <Form.Field>
                        <label>Превью к публикации</label>
                        <ImageUploader withPreview={false} withIcon={false}
                                       singleImage onChange={file => this.loadPreview(file)}
                                       buttonText='Загрузить'
                                       label='Формат: jpg, gif, png, Максимальный размер файла: 5Mb.'
                                       imgExtension={['.jpg', '.gif', '.png', '.jpeg']}
                                       maxFileSize={5242880}
                        />
                        {
                            preview &&
                            <Image size='large' centered bordered
                                   label={{
                                       as: 'a', color: 'red', corner: 'right', icon: 'remove',
                                       onClick: () => this.setState({preview: null})
                                   }}
                                   src={preview.url}/>
                        }
                    </Form.Field>
                    <Form.Field required error={textError}>
                        <label>Текст</label>
                        <SimpleMDE value={text}
                                   onChange={(value) => this.setState({text: value, textError: false})}/>
                    </Form.Field>
                    <Form.Field required error={categoryError}>
                        <label>Категория</label>
                        <Select onChange={(value) => this.setState({category: value, categoryError: false})}
                                defaultValue={category} placeholder={'Выберите категорию'}
                                options={allCategories}/>
                    </Form.Field>
                    <Form.Field required error={tagsError}>
                        <label>Теги</label>
                        <Select isMulti
                                onChange={(values) => this.setState({tags: values, tagsError: false})}
                                defaultValue={tags} options={allTags} placeholder={'Выберите теги'}
                                closeMenuOnSelect={false}/>
                    </Form.Field>
                    <Form.Field>
                        <label>Прикрепить изображения</label>
                        <Form.Input
                            onChange={(e) => {
                                this.loadImages(e.target.files);
                                e.target.value = null
                            }}
                            type={'file'}
                        />
                    </Form.Field>
                    <Image.Group size='medium'>
                        {
                            postImages.map(({id, url}) => {
                                return <Image key={id} label={{
                                    as: 'a', color: 'red', corner: 'right', icon: 'remove',
                                    onClick: () => {
                                        this.deleteImageId(id)
                                    }
                                }} bordered src={url}/>
                            })
                        }
                    </Image.Group>
                    <Form.Input>
                        <Checkbox defaultChecked={posted}
                                  onChange={(e, data) => {
                                      this.setState({posted: data.checked})
                                  }}
                                  label='Опубликовать'/>
                    </Form.Input>
                    <Button style={{backgroundColor: '#175e6b'}} primary>Cохранить</Button>
                </Form>
            </Segment>
        </div>
    }
}

export default Publish;