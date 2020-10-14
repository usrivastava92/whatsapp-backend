import React from "react";

const validationComponent = (props) => {
    return (
        <div>
            <input type="text" onChange={props.textChangeHandler} value={props.input}/>
            <span> Length : {props.input.length}</span>
            <h1>{props.input.length > 5 ? "text too long!" : "text too short!"}</h1>
        </div>
    );
};

export default validationComponent;
