import { useState , useEffect} from "react" ;

// const StateSample = () => {
    
//     /*
//     요구사항)
//     - 입장인원이 꽉차면 입장 버튼을 비활성화(disabled) 시키고
//     - 퇴장버튼이 눌리면 인원을 감소시키는데 입장인원이 0이면 버튼을 비활성화(disabled)  
//     */

//     const CAPACITY = 10 ; 

//     const [cnt     , setCnt]     = useState(0); 
//     const [isFull  , setIsFull]  = useState(false); 
//     const [isEmpty , setIsEmpty] = useState(false);  
    
//     const cntUpHandler = () => {
//         setCnt( cnt  => cnt + 1 );
//         // console.log("[debug] >>> cnt up btn click , " , cnt); 
//     }
//     const cntDownHandler = () => {
//         setCnt( cnt  => cnt - 1 );
//         // console.log("[debug] >>> cnt down btn click , " , cnt); 
//     }

//     // side effect 
//     useEffect(() => {
//         console.log("[debug] useEffect");
//         console.log("[debug] >>> cnt value  , " , cnt);
//         setIsFull( cnt >= CAPACITY) ;
//         setIsEmpty( cnt <= 0 );
//     }, [cnt] ) ;    

//     return (
//         <div>
//             <p>{ `입장인원 : ${cnt} 명` }</p>
//             <button type="button" onClick={cntUpHandler}    disabled={isFull}>입장</button>
//             <button type="button" onClick={cntDownHandler}  disabled={isEmpty}>퇴장</button>
//             { isFull && <p style={{color : "red" }}>정원이 가득찼습니다.</p> }
//         </div>
//     ) ;
        
    
// }



// 사용자정의 훅 사용 방법
import useCounter from "../../component/hook/useCounter";
const StateSample = () => {
    
    const CAPACITY = 10 ; 

    const [cnt, upCntHandler, downCntHandler] = useCounter();

    const [isFull  , setIsFull]  = useState(false); 
    const [isEmpty , setIsEmpty] = useState(false);  
    

    // side effect 
    useEffect(() => {
        console.log("[debug] useEffect");
        console.log("[debug] >>> cnt value  , " , cnt);
        setIsFull( cnt >= CAPACITY) ;
        setIsEmpty( cnt <= 0 );
    }, [cnt] ) ;    

    return (
        <div>
            <p>{ `입장인원 : ${cnt} 명` }</p>
            <button type="button" onClick={upCntHandler}    disabled={isFull}>입장</button>
            <button type="button" onClick={downCntHandler}  disabled={isEmpty}>퇴장</button>
            { isFull && <p style={{color : "red" }}>정원이 가득찼습니다.</p> }
        </div>
    ) ;
        
    
}

export default StateSample ;