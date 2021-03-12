import React, {Component} from 'react';
import TagInfo from "./TagInfo";
import MessageInfo from "./MessageInfo";
import CategoryInfo from "./CategoryInfo";
import {Tab, TabList, TabPanel, Tabs} from "react-tabs";
import UserInfo from "./UserInfo";

class Admin extends Component {

    componentDidMount() {
        document.title = 'Админка'
    }

    render() {
        return (
            <Tabs>
                <TabList>
                    <Tab>Сообщения</Tab>
                    <Tab>Категории</Tab>
                    <Tab>Теги</Tab>
                    <Tab>Пользователи</Tab>
                </TabList>
                <TabPanel>
                    <MessageInfo/>
                </TabPanel>
                <TabPanel>
                    <CategoryInfo/>
                </TabPanel>
                <TabPanel>
                    <TagInfo tags={this.props.tags}/>
                </TabPanel>
                <TabPanel>
                    <UserInfo/>
                </TabPanel>
            </Tabs>
        )
    }
}

export default Admin;