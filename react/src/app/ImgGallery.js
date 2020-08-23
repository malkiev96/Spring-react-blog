import React, {Component} from 'react';
import {getImages} from "../service/ImageService";
import {Loader, Segment} from "semantic-ui-react";
import Gallery from 'react-photo-gallery';

class ImgGallery extends Component {

    constructor(props) {
        super(props);
        this.state = {
            images: {
                loading: true,
                images: [],
                page: null
            }
        }
    }

    componentDidMount() {
        this.loadImages()
    }

    loadImages() {
        getImages(1, 10, 'uploadedDate,desc').then(response => {
            this.setState({
                images: {
                    images: response['content'],
                    page: response['page'],
                    loading: false
                }
            })
        }).catch(() => {
            this.setState({
                images: {
                    images: [],
                    loading: false
                }
            })
        })
    }

    render() {
        const {images, loading} = this.state.images
        if (loading) return <Loader active inline='centered'/>

        const imgViews = images.map(img => ({
            src: img.url,
            width: img.width,
            height: img.height
        }))

        return (
            <div>
                <Segment>
                    <Gallery photos={imgViews}/>
                </Segment>
            </div>
        )
    }
}

export default ImgGallery;