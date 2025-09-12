import CommentComponent from '../component/CommentComponent' ;

const comments = [
    {
        name : "임섭순",
        comment : "리액트 재미있어요~~"
    },
    {
        name : "이현진",
        comment : "저도 리액트를 배워보고 싶었어요~~"
    },
    {
        name : "섭섭해",
        comment : "목이 아프당 ㅠㅠ"
    }
] ;
function CommentPage() {
    return (
        <div>
            {
                comments.map( comment => {
                    return (
                        <CommentComponent data={comment} />
                    )
                })
            }
           
        </div>    
    );
}

export default CommentPage ;