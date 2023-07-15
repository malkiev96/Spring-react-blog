import {Button, Icon} from "semantic-ui-react";
import {GITHUB_AUTH_URL, GOOGLE_AUTH_URL} from "../../util/Constants";
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
    </div>
)

export default OauthButtons;