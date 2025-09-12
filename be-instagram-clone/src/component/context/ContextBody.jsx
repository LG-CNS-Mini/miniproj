import { useContext } from "react";
import ctx            from "./util/contextMode"; 

const ContextBody = () => {
    const  {isMode} = useContext(ctx);

    return(
        <div>
            <div style={{
                backgroundColor : isMode ? "black" : "white",
                color : isMode ? "white" : "black"
            }}>
                <h1>수강생분들 다들 화이팅!!!</h1>
            </div>
            
        </div>
    );
}

export default ContextBody ;