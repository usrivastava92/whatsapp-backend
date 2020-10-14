import React from "react";

const charComponent = (props) => {
    return props.input.split('').map((char, index) => {
        return <button key={index} onClick={() => props.clickHandler(index)}>{char}</button>
    });
};

export default charComponent;
