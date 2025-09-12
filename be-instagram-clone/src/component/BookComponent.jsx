
function BookComponent(props) {
    return (
        <div>
            <div>책 이름 : {props.data.name} , 책 가격 : {props.data.price}</div>
        </div>
    );
}


export default BookComponent;