import React from 'react'
import ReactDOM from 'react-dom'
import './main/js/index.css'
import 'semantic-ui-css/semantic.min.css'
import App from './main/js/app/App'
import registerServiceWorker from './main/js/registerServiceWorker'
import {BrowserRouter} from 'react-router-dom'

ReactDOM.render(
    <BrowserRouter>
        <App/>
    </BrowserRouter>,
    document.getElementById('root')
)

registerServiceWorker()
