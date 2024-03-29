import React from 'react'
import ReactDOM from 'react-dom'
import 'semantic-ui-css/semantic.min.css'
import App from './main/js/component/App'
import registerServiceWorker from './main/js/util/registerServiceWorker'
import {BrowserRouter} from 'react-router-dom'

ReactDOM.render(
    <BrowserRouter>
      <App/>
    </BrowserRouter>,
    document.getElementById('root')
)

registerServiceWorker()
