import { useContext } from "react";
import ctx            from "./util/contextMode"; 
const ContextHeader = () => {
    const  {isMode} = useContext(ctx);
    return(
        <div>
            <header style={{
                backgroundColor : isMode ? "black" : "white",
                color : isMode ? "white" : "black"
            }}>

                <h1>즐거운 한 주의 시작입니다~~~</h1>
            </header>
            
        </div>
    );
}

export default ContextHeader ;