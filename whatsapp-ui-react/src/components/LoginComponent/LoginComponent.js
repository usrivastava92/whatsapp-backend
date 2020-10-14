import React, {Component} from "react";

class LoginComponent extends Component {

    render() {
        return (
            <form action={this.props.uri} method="POST">
                username : <input type="input" name="login" value="foo"/><br/>
                password : <input type="password" name="password" value="foo"/><br/>
                <input type="SUBMIT" value="LOGIN"/>
            </form>
        );
    }

}

export default LoginComponent;