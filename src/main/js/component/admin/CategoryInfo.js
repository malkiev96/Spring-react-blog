import React, {Component} from 'react';
import {Button, Form, Message, Modal, Segment, Table} from 'semantic-ui-react'
import Alert from "react-s-alert";
import {
  createOrEditCategory,
  deleteCategory,
  getAllCategories
} from "../../api/Categories";
import Select from "react-select";

class CategoryInfo extends Component {

  constructor(props) {
    super(props);
    this.state = {
      categories: [],
      showModal: false,
      catName: '',
      catCode: '',
      editCatId: null,
      allCategories: [],
      parent: null
    }
    this.onDeleteCat = this.onDeleteCat.bind(this);
    this.handleChange = this.handleChange.bind(this);
    this.submitForm = this.submitForm.bind(this);
  }

  openModal = () => this.setState({showModal: true})
  closeModal = () => this.setState({
    showModal: false,
    catName: '',
    catCode: '',
    editCatId: null,
    parent: null
  })

  componentDidMount() {
    this.loadCategories()
  }

  handleChange(evt) {
    this.setState({
      ...this.state, [evt.target.name]: evt.target.value
    })
  }

  submitForm() {
    const {catName, catCode, editCatId, parent} = this.state
    createOrEditCategory(catCode, catName, editCatId, parent).then(() => {
      this.loadCategories()
      Alert.success("Категория успешно сохранена")
    }).catch((err) => {
      Alert.error((err && err.message)
          || 'Не удалось сохранить категорию, попробуйте еще раз');
    })
    this.closeModal()
  }

  loadCategories() {
    getAllCategories().then(result =>
        this.setState({
          categories: result,
          allCategories: result.map(
              ({id, name}) => ({id: id, value: id, label: name}))
        })).catch(() =>
        this.setState({
          categories: [],
          allCategories: []
        }))
  }

  onDeleteCat(id) {
    deleteCategory(id).then(() => {
      this.loadCategories()
      Alert.success("Категория успешно удалена")
    }).catch(() => {
      Alert.error('Не удалось удалить категорию, попробуйте еще раз');
    })
  }

  render() {
    const {categories, showModal, catName, catCode, allCategories} = this.state

    return (
        <Segment>
          {
            categories?.length > 0 ?
                <Table celled>
                  <Table.Header>
                    <Table.Row>
                      <Table.HeaderCell>Название</Table.HeaderCell>
                      <Table.HeaderCell>Код для URL</Table.HeaderCell>
                      <Table.HeaderCell>Родитель</Table.HeaderCell>
                      <Table.HeaderCell>Редактировать</Table.HeaderCell>
                      <Table.HeaderCell>Удалить</Table.HeaderCell>
                    </Table.Row>
                  </Table.Header>

                  <Table.Body>
                    {
                      categories.map((cat, index) => (
                          <Table.Row key={index}>
                            <Table.Cell>{cat.name}</Table.Cell>
                            <Table.Cell>{cat.code}</Table.Cell>
                            <Table.Cell>{cat.parent?.name}</Table.Cell>
                            <Table.Cell>
                              <Button onClick={() => {
                                this.setState({
                                  editCatId: cat.id,
                                  catName: cat.name,
                                  catCode: cat.code
                                })
                                this.openModal()
                              }} primary icon='edit'/>
                            </Table.Cell>
                            <Table.Cell>
                              <Button onClick={() => {
                                if (window.confirm(
                                    "Are you sure?")) {
                                  this.onDeleteCat(cat.id)
                                }
                              }} negative icon='delete'/>
                            </Table.Cell>
                          </Table.Row>
                      ))
                    }
                  </Table.Body>
                </Table> :
                <Message>Категорий нет</Message>
          }
          <Modal
              onClose={this.closeModal}
              onOpen={this.openModal}
              open={showModal}
              size={'small'}
              trigger={<Button primary style={{backgroundColor: '#175e6b'}}>Добавить
                категорию</Button>}
          >
            <Modal.Content>
              <Modal.Description>
                <Form onSubmit={this.submitForm}>
                  <Form.Field required>
                    <label>Название</label>
                    <Form.Input required placeholder={'Название'}
                                value={catName}
                                onChange={this.handleChange}
                                name={'catName'}/>
                  </Form.Field>
                  <Form.Field required>
                    <label>Код</label>
                    <Form.Input required placeholder={'Код'}
                                value={catCode} onChange={this.handleChange}
                                name={'catCode'}/>
                  </Form.Field>
                  <Form.Field>
                    <label>Родитель</label>
                    <Select onChange={(p) => this.setState({parent: p.id})}
                            options={allCategories}/>
                  </Form.Field>
                  <Button onClick={this.closeModal}>Отмена</Button>
                  <Button style={{backgroundColor: '#175e6b'}}
                          primary>Сохранить</Button>
                </Form>
              </Modal.Description>
            </Modal.Content>
          </Modal>
        </Segment>
    )
  }
}

export default CategoryInfo;