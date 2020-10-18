import React, {Component} from "react";
import {Alert} from "react-bootstrap";

export default class Message extends Component {

    render() {
        return (
            <Alert className={this.props.message.from === 100 ? 'justify-content-end' :'justify-content-start' } variant={this.props.message.from === 100 ? 'info' : 'primary'}>
                <p>from : {this.props.message.from}, to : {this.props.message.to},
                    message: {this.props.message.message}</p>
            </Alert>
        );
    }

}
