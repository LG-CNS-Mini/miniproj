import { useState  } from "react";

const useCounter = () => {
    const [cnt, setCnt] = useState(0);
    
    const upCntHandler = () => {
        setCnt( cnt => cnt + 1) ;
    }
    const downCntHandler = () => {
        setCnt( cnt => Math.max(cnt - 1 , 0)) ;
    }

    return [cnt, upCntHandler, downCntHandler] ;    
}

export default useCounter ; 