import React from "react";

const styles = {
    wrapper : {
        border : "1px solid grey" ,
        borderRadius : 16,
        display : "flex",
        flexDirection : "row",
        margin : 8,
        padding : 8
    },
    msgTxt : {
        color : "red" , 
        fontSize : 16
    }
} ;

class Notification extends React.Component {
    constructor(props) {
        super(props);
        this.state = {};
    }
    componentDidMount() {
        console.log(`[debug] Notification componentDidMount ${this.props.id}`);
    }
    componentDidUpdate() {
        console.log(`[debug] Notification componentDidUpdate ${this.props.id}`);
    }
    componentWillUnmount() {
        console.log(`[debug] Notification componentWillUnmount ${this.props.id}`);
    }
    shouldComponentUpdate(nextProps) {
        return nextProps.msg !== this.props.msg;
    }
    render() {
        return (
            <div style={styles.wrapper}>
                <span style={styles.msgTxt}>
                    {this.props.msg}
                 </span>
            </div>
        );
    }
}

export default Notification ;