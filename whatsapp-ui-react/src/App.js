import React, {Component} from 'react';
import './App.css';
import axios from 'axios';
import {DEV_URLS, PROD_URLS} from './constants/Urls';
import Message from "./components/Message/Message";
import 'bootstrap/dist/css/bootstrap.min.css';

class App extends Component {

    state = {
        jwt: null,
        profile: null,
        username: 'foo',
        password: 'foo',
        socket: null,
        message: 'hello',
        to: '101',
        messageList: []
    }

    URLS = DEV_URLS;

    loginClickHandler = () => {
        const uri = this.URLS.LOGIN_URL;
        const payload = {username: this.state.username, password: this.state.password};
        const headers = {'Content-Type': 'application/json'};
        console.log(uri)
        axios.post(uri, payload, {headers})
            .then(response => {
                console.log(response);
                if (response === null || response.data === null || response.data.responseStatus === null || response.data.responseStatus.code !== '200') {
                    console.log('Invalid credentials..!!')
                } else {
                    this.setState({jwt: response.data.jwt, profile: response.data.userProfile})
                    this.createTokenHandler();
                }
                console.log(this.state);
            })
            .catch(error => {
                console.error('There was an error!', error);
            });
    }

    usernameChangeHandler = (e) => {
        this.setState({username: e.target.value})
    }

    passwordChangeHandler = (e) => {
        this.setState({password: e.target.value})
    }

    createTokenHandler = () => {
        const uri = this.URLS.CREATE_TOKEN_URL;
        const headers = {'Authorization': `Bearer ${this.state.jwt}`};
        axios.post(uri, {}, {headers})
            .then(response => {
                console.log(response);
                this.connectSocket(response.data.accessToken);
            })
            .catch(error => {
                console.error('There was an error!', error);
            });
    }

    connectSocket = (token) => {
        const ws = new WebSocket(this.URLS.SOCKET_CONNECT_URL + token)
        let connectInterval;
        const timeout = 250;
        ws.onopen = () => {
            console.log("Socket connected..!!")
            this.setState({socket: ws});
        }
        // websocket onclose event listener
        ws.onclose = e => {
            console.log(`Socket is closed. Reconnect will be attempted in ${timeout} second.`, e.reason);
            connectInterval = setTimeout(this.check, Math.min(10000, timeout)); //call check function after timeout
        };
        // websocket onerror event listener
        ws.onerror = err => {
            console.error("Socket encountered error: ", err.message, "Closing socket");
            ws.close();
        };

        ws.onmessage = evt => {
            const message = JSON.parse(evt.data)
            console.log(message)
            let messages = [...this.state.messageList]
            messages.push(message);
            this.setState({messageList: messages});
        }
    }

    check = () => {
        const {socket} = this.state;
        if (!socket || socket.readyState === WebSocket.CLOSED) this.createTokenHandler(); //check if websocket instance is closed, if so call `connect` function.
    };

    sendMessage = () => {
        const data = {message: this.state.message, from: this.state.profile.id, to: this.state.to};
        console.log(data);
        let messages = [...this.state.messageList]
        messages.push(data);
        this.setState({messageList: messages});
        const {socket} = this.state // websocket instance passed as props to the child component.
        try {
            socket.send(JSON.stringify(data));
        } catch (error) {
            console.log(error) // catch error
        }
    }

    messageChangeHandler = (event) => {
        this.setState({message: event.target.value});
    }

    chatWithChangeHandler = (event) => {
        this.setState({to: event.target.value});
    }

    render() {
        return (
            <div className="App Container">
                username : <input type="text" name="login" value={this.state.username}
                                  onChange={this.usernameChangeHandler}/>
                password : <input type="password" name="password" value={this.state.password}
                                  onChange={this.passwordChangeHandler}/>
                chat with? : <input type="text" name="chatWith" value={this.state.to}
                                    onChange={this.chatWithChangeHandler}/>
                <button onClick={this.loginClickHandler}>Connect</button>
                <br/>
                message : <input type="text" name="message" value={this.state.message}
                                 onChange={this.messageChangeHandler}/><br/>
                <button onClick={this.sendMessage}>Send</button>
                <br/>
                {this.state.messageList.map((message, index) => {
                    return (<Message key={index} message={message}/>)
                })}
            </div>
        )
    }
}

export default App;
