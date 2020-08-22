import {Button, Icon} from "semantic-ui-react";
import {FACEBOOK_AUTH_URL, GITHUB_AUTH_URL, GOOGLE_AUTH_URL} from "../constants";
import React from "react";

const OauthButtons = ({title}) => (
    <div>
        <h3>{title}</h3>
        <Button color='google plus' href={GOOGLE_AUTH_URL}>
            <Icon name='google plus'/> Google
        </Button>
        <Button color='grey' href={GITHUB_AUTH_URL}>
            <Icon name='github'/> GitHub
        </Button>
        <Button color='facebook' href={FACEBOOK_AUTH_URL}>
            <Icon name='facebook'/> Facebook
        </Button>
    </div>
)

export default OauthButtons;