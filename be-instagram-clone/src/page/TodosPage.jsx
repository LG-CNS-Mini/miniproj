import {useState} from "react" ;

const TodosPage = () => {
    
    /// 변수, 함수
    // let txt = '';
    const [txt , setTxt] = useState('');
    const btnHandler = (msg) => {
        console.log("[debug] >>> btn click " , msg ); 
    }
    const txtHandler = (event) => {
        setTxt(event.target.value); 
    }

    /// template UI
    return (
        <div class="container">
            <h2>To-Do List</h2> 
            <form class="d-flex">
                <div class="flex-grow-1 mr-2">

                    <input  class="form-control" type="text"
                            placeholder="Input new todo"
                            value={txt}
                            onChange={(e) => txtHandler(e) } />
                </div>
                <div>
                    <button 
                        class="btn btn-primary" 
                        type="button"
                        onClick={ () => btnHandler(txt) }>Add</button>
                </div>
            </form>

            입력된 데이터 출력하는 영역
        </div>
    );
};

export default TodosPage ;
