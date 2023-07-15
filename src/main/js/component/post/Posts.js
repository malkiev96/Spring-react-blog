import React, {Component} from 'react';
import PostsView from "./PostsView";
import {getPosts} from "../../api/Posts";
import {Button, Pagination, Segment} from "semantic-ui-react";
import {SORT_DATE, SORT_TYPE} from "../../util/Constants";

class Posts extends Component {

  constructor(props) {
    super(props);
    const page = this.props.match.params.page || 1
    const tagCode = this.props.match.params.tagCode
    const categoryCode = this.props.match.params.categoryCode
    this.state = {
      page: page,
      tagCode: tagCode,
      categoryCode: categoryCode,
      size: 10,
      sort: localStorage.getItem(SORT_TYPE) || SORT_DATE,
      selectedTags: tagCode ? [tagCode] : [],
      posts: {
        posts: [],
        last: true,
        totalPages: 0,
        totalElements: 0,
        size: 10,
        number: 0,
        loading: true
      }
    }
    this.onTagClick = this.onTagClick.bind(this);
    this.loadPosts = this.loadPosts.bind(this);
    this.pageChange = this.pageChange.bind(this);
    this.getIds = this.getIds.bind(this);
  }

  componentDidUpdate(prevProps) {
    if (this.props.location.pathname !== prevProps.location.pathname) {
      const page = this.props.match.params.page || 1
      const tagCode = this.props.match.params.tagCode
      const categoryCode = this.props.match.params.categoryCode
      this.setState({page: page, tagCode: tagCode, categoryCode: categoryCode})
      this.loadPosts(page, this.state.size, this.state.sort, tagCode, categoryCode)
    }
  }

  componentDidMount() {
    document.title = 'Публикации'
    const {page, size, sort, tagCode, categoryCode} = this.state;
    this.loadPosts(page, size, sort, tagCode, categoryCode)
  }

  onTagClick(code) {
    const {page, size, sort, categoryCode} = this.state;
    const tags = this.state.selectedTags;
    const selectedTags = tags.includes(code)
        ? tags.filter(i => i !== code)
        : tags.concat(code);
    this.setState({
      selectedTags: selectedTags
    })

    this.loadPosts(page, size, sort,
        selectedTags.length === 0 ? null : selectedTags, categoryCode)
  }

  loadPosts(page, size, sort, tagCode, categoryCode) {
    this.setState({
      posts: {
        posts: [],
        loading: true
      }
    })
    const catIds = this.getIds(categoryCode)
    getPosts(page, size, sort, tagCode, catIds).then(response => {
      this.setState({
        posts: {
          posts: response.content,
          last: response.last,
          totalPages: response.totalPages,
          totalElements: response.totalElements,
          size: response.size,
          number: response.number,
          loading: false
        }
      })
    }).catch(error => {
      this.setState({
        posts: {
          posts: [],
          last: true,
          totalPages: 0,
          totalElements: 0,
          size: 10,
          number: 0,
          loading: false
        }
      })
    })
  }

  getIds(categoryCode) {
    if (categoryCode) {
      let exist = false
      let ids = []
      this.props.categories.forEach(cat => {
        if (cat.code === categoryCode) {
          ids.push(cat.id)
          cat.children.forEach(child => ids.push(child.id))
          exist = true
        }
        cat.children.forEach(child => {
          if (child.code === categoryCode) {
            ids.push(child.id)
            exist = true
          }
        })
      })
      if (exist) {
        return ids.toString().split(",")
      }
    }
    return false
  }

  render() {
    const {posts, selectedTags} = this.state
    return (
        <div>
          <Segment>
            {
              this.props.tags.map(tag => {
                const selected = selectedTags.includes(tag.code)
                return (
                    <Button key={tag.id}
                            primary={selected}
                            basic={!selected}
                            size={"tiny"}
                            style={{backgroundColor: '#175e6b'}}
                            onClick={() => this.onTagClick(tag.code)}>{tag.name}
                    </Button>
                )
              })
            }
          </Segment>
          <PostsView posts={posts}/>
          {
              posts.totalPages > 1 &&
              <Pagination style={{marginTop: '15px'}}
                          activePage={posts.number + 1}
                          firstItem={null}
                          lastItem={null}
                          onPageChange={this.pageChange}
                          totalPages={posts.totalPages}/>
          }
        </div>
    )
  }

  pageChange(e, {activePage}) {
    this.setState({page: activePage})
    const {tagCode, categoryCode} = this.state
    if (tagCode) {
      this.props.history.push(`/tags/${tagCode}/${activePage}`)
    } else if (categoryCode) {
      this.props.history.push(
          `/category/${categoryCode}/${activePage}`)
    } else {
      this.props.history.push(`/posts/${activePage}`)
    }
  }
}

export default Posts;