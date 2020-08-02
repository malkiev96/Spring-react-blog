import React from 'react'
import {Menu} from "semantic-ui-react";
import {Link} from "react-router-dom";


const RightMenu = (props) => {
    const {tags} = props
    return (
        <div>
            <Menu vertical fluid>
                {
                    tags.map(tag => {
                        return (
                            <Menu.Item key={tag.id} as={Link} to={'/tags/' + tag.name} name={tag.name}/>
                        )
                    })
                }
            </Menu>
        </div>
    )
}

export default RightMenu