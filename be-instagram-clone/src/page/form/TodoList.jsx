import { Link, useLocation } from "react-router-dom";


const TodoList = () => {
    const location = useLocation(); 
    const ary = location.state ; 
    console.log("[debug] >>>  todo location.state " , ary ); 
    return (
        <div>

            <Link to="/">첫 화면으로 이동</Link>
            
            <hr/>
            
            <ol>
                {
                    ary.map( (todo, idx) => {
                        return (
                            <li key={idx}>{todo.content}</li>
                        )
                    })   
                }
            </ol>
        </div>
    );
}

export default TodoList ;
