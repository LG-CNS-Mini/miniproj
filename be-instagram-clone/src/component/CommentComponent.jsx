const styles = {
    wrapper : {
        border : "1px solid grey" ,
        borderRadius : 16,
        display : "flex",
        flexDirection : "row",
        margin : 8,
        padding : 8
    },
    content : {
        display: "flex",
        flexDirection: "column",
        justifyContent : "center",
        marginLeft : 8 
    },
    image : {
        width : 50,
        height : 50,
        borderRadius: 25
    },
    
    nameTxt : {
        color : "green" , 
        fontSize : 16,
        fontWeight : "bold"
    },
    commentTxt : {
        color : "red" , 
        fontSize : 16
    }
} ;

function CommentComponent(props) {
    return (
        <div style={styles.wrapper}>
            <div>
                <img    style={styles.image}
                        src="https://upload.wikimedia.org/wikipedia/commons/8/89/Portrait_Placeholder.png"/>
            </div>
            <div style={styles.content}>
                <span style={styles.nameTxt}>{props.data.name}</span>
                <span style={styles.commentTxt}>{props.data.comment}</span>
            </div>
        </div>
    );
}

export default CommentComponent ;
