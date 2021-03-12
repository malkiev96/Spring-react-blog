import React, {Component} from 'react';
import {Button, Form, Header, Message, Modal, Segment, Table} from 'semantic-ui-react'
import Alert from "react-s-alert";
import {createTag, deleteTag, getTags} from "../service/TagService";

class TagInfo extends Component {

    constructor(props) {
        super(props);
        this.state = {
            tags: props.tags,
            showModal: false,
            tagName: '',
            tagCode: '',
            editTagId: null
        }
        this.onDeleteTag = this.onDeleteTag.bind(this);
        this.handleChange = this.handleChange.bind(this);
        this.submitForm = this.submitForm.bind(this);
    }

    openModal = () => this.setState({showModal: true})
    closeModal = () => this.setState({
        showModal: false,
        tagName: '',
        tagCode: '',
        editTagId: null
    })

    handleChange(evt) {
        this.setState({
            ...this.state, [evt.target.name]: evt.target.value
        })
    }

    submitForm() {
        const {tagName, tagCode, editTagId} = this.state
        createTag(tagCode, tagName, editTagId).then(() => {
            this.loadTags()
            Alert.success("Тег успешно сохранен")
        }).catch((err) => {
            Alert.error((err && err.message) || 'Не удалось сохранить тег, попробуйте еще раз');
        })
        this.closeModal()
    }

    loadTags() {
        getTags().then(result =>
            this.setState({
                tags: result.content
            })).catch(() =>
            this.setState({
                tags: []
            }))
    }

    onDeleteTag(id) {
        deleteTag(id).then(() => {
            this.loadTags()
            Alert.success("Тег успешно удален")
        }).catch(() => {
            Alert.error('Не удалось удалить тег, попробуйте еще раз');
        })
    }

    render() {
        const {tags, showModal, tagName, tagCode} = this.state

        return (
            <Segment>
                {
                    tags.length > 0 ?
                        <Table celled>
                            <Table.Header>
                                <Table.Row>
                                    <Table.HeaderCell>Название</Table.HeaderCell>
                                    <Table.HeaderCell>Код для URL</Table.HeaderCell>
                                    <Table.HeaderCell>Редактировать</Table.HeaderCell>
                                    <Table.HeaderCell>Удалить</Table.HeaderCell>
                                </Table.Row>
                            </Table.Header>

                            <Table.Body>
                                {
                                    tags.map((tag, index) => (
                                        <Table.Row key={index}>
                                            <Table.Cell>{tag.name}</Table.Cell>
                                            <Table.Cell>{tag.code}</Table.Cell>
                                            <Table.Cell>
                                                <Button onClick={() => {
                                                    this.setState({
                                                        editTagId: tag.id,
                                                        tagName: tag.name,
                                                        tagCode: tag.code
                                                    })
                                                    this.openModal()
                                                }} primary icon='edit'/>
                                            </Table.Cell>
                                            <Table.Cell>
                                                <Button onClick={() => {
                                                    if (window.confirm("Are you sure?")) this.onDeleteTag(tag.id)
                                                }} negative icon='delete'/>
                                            </Table.Cell>
                                        </Table.Row>
                                    ))
                                }
                            </Table.Body>
                        </Table> :
                        <Message>Тегов нет</Message>
                }
                <Modal
                    onClose={this.closeModal}
                    onOpen={this.openModal}
                    open={showModal}
                    size={'small'}
                    trigger={<Button primary style={{backgroundColor: '#175e6b'}}>Добавить тег</Button>}
                >
                    <Modal.Content>
                        <Modal.Description>
                            <Form onSubmit={this.submitForm}>
                                <Form.Field required>
                                    <label>Название</label>
                                    <Form.Input required placeholder={'Название'}
                                                value={tagName}
                                                onChange={this.handleChange}
                                                name={'tagName'}/>
                                </Form.Field>
                                <Form.Field required>
                                    <label>Код</label>
                                    <Form.Input required placeholder={'Код'}
                                                value={tagCode} onChange={this.handleChange}
                                                name={'tagCode'}/>
                                </Form.Field>
                                <Button onClick={this.closeModal}>Отмена</Button>
                                <Button style={{backgroundColor: '#175e6b'}} primary>Сохранить</Button>
                            </Form>
                        </Modal.Description>
                    </Modal.Content>
                </Modal>
            </Segment>
        )
    }
}

export default TagInfo;