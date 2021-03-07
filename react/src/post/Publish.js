import React, {Component} from 'react';
import {Button, Checkbox, Form, Icon, Image, Segment} from "semantic-ui-react";
import SimpleMDE from "react-simplemde-editor";
import "easymde/dist/easymde.min.css";
import Select from 'react-select'
import ImageUploader from "react-images-upload";
import {deleteDocument, FILE_TYPE, getDocumentSrc, IMAGE_TYPE, saveDocument} from '../service/DocumentService';
import {createPost, getPostById} from '../service/PostService';
import Alert from "react-s-alert";
import {Redirect} from "react-router-dom";
import NotFound from "../common/notFound/NotFound";
import {BASE_API} from "../util/Constants";
import DataLoader from "../common/DataLoader";

const defaultState = () => ({
    title: '',
    description: '',
    text: '',
    posted: false,
    category: null,
    tags: [],
    images: [],
    documents: [],
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
        document.title = 'Новая публикация'
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
            document.title = 'Редактирование - ' + response.title
            this.setState({
                post: {
                    post: response,
                    error: false,
                    postLoading: false
                },
                posted: response.status === 'PUBLISHED',
                title: response.title,
                description: response.description,
                text: response.text,
                preview: response.preview,
                category: {id: category.id, value: category.id, label: category.name},
                tags: response.tags.content.map(({id, name}) => ({id: id, value: id, label: name})),
                images: response.documents.content.filter(img => img.type === 'IMAGE'),
                documents: response.documents.content.filter(img => img.type !== 'IMAGE')
            })
        }).catch(() => {
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
            saveDocument(image, IMAGE_TYPE).then(result => {
                this.setState({
                    preview: result,
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
        deleteDocument(id).catch(error => {
            Alert.error((error && error.message) || 'Не удалось удалить изображение')
        })
        this.setState({
            images: this.state.images.filter(img => img.id !== id)
        })
    }

    deleteDocumentId(id) {
        deleteDocument(id).catch(error => {
            Alert.error((error && error.message) || 'Не удалось удалить файл')
        })
        this.setState({
            documents: this.state.documents.filter(img => img.id !== id)
        })
    }

    loadImages(files) {
        let image = []
        Array.from(files).forEach(file => image.push(file))
        const types = ['image/png', 'image/jpeg', 'image/gif']
        if (types.every(type => image[0].type !== type)) {
            Alert.error("Прикрепленный файл не является изображением");
            return;
        }
        this.setState({loading: true})
        saveDocument(image, IMAGE_TYPE).then(result => {
            const images = this.state.images
            images.push(result)
            this.setState({
                loading: false,
                images: images
            })
        }).catch(error => {
            this.setState({loading: false})
            Alert.error((error && error.message) || 'Не удалось загрузить изображение, попробуйте еще раз');
        })
    }

    loadDocuments(files) {
        let document = []
        Array.from(files).forEach(file => document.push(file))
        if (!document[0].type.includes("application")) {
            Alert.error("Недопустимый формат файла");
            return;
        }
        this.setState({loading: true})
        saveDocument(document, FILE_TYPE).then(result => {
            const documents = this.state.documents
            documents.push(result)
            this.setState({
                loading: false,
                documents: documents
            })
        }).catch(error => {
            this.setState({loading: false})
            Alert.error((error && error.message) || 'Не удалось загрузить файл, попробуйте еще раз');
        })
    }

    submitForm() {
        const {
            previewLoading, loading, preview, id, images, documents,
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
        const documentsIds = []
        let postRequest = {
            id: id,
            title: title,
            description: description,
            text: text,
            posted: posted,
            previewId: preview ? preview.id : null,
            categoryId: category.id,
            documentIds: documentsIds.concat(images.map(img => img.id), documents.map(doc => doc.id)),
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
            textError, categoryError, tagsError, loading, createdPost, category, images, documents
        } = this.state
        if (postLoading && edit) return <DataLoader/>
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
                                       onClick: () => {
                                           deleteDocument(preview.id).catch(err => {
                                               Alert.error((err && err.message) ||
                                                   'Не удалось удалить изображение');
                                           })
                                           this.setState({
                                               preview: null
                                           })
                                       }
                                   }}
                                   src={`${BASE_API}/documents/${preview.id}/download`}/>
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
                            images.map(({id}) => {
                                return <Image key={id} label={{
                                    as: 'a', color: 'red', corner: 'right', icon: 'remove',
                                    onClick: () => {
                                        this.deleteImageId(id)
                                    }
                                }} bordered src={`${BASE_API}/documents/${id}/download`}/>
                            })
                        }
                    </Image.Group>
                    <Form.Field>
                        <label>Прикрепить документы</label>
                        <Form.Input
                            onChange={(e) => {
                                this.loadDocuments(e.target.files);
                                e.target.value = null
                            }}
                            type={'file'}
                        />
                    </Form.Field>
                    {
                        documents && documents.map((doc, index) =>
                            <Segment id={index} stacked loading={loading}>
                                <a href={getDocumentSrc(doc.id)}>{doc.filename}</a>
                                <Button negative size='mini' circular icon
                                        floated="right"
                                        onClick={() => this.deleteDocumentId(doc.id)}>
                                    <Icon name='remove'/>
                                </Button>
                            </Segment>
                        )
                    }
                    <Form.Input>
                        <Checkbox defaultChecked={posted}
                                  onChange={(e, data) => {
                                      this.setState({posted: data.checked})
                                  }}
                                  label='Опубликовать'/>
                    </Form.Input>
                    <Button style={{backgroundColor: '#175e6b'}} primary>Сохранить</Button>
                </Form>
            </Segment>
        </div>
    }
}

export default Publish;