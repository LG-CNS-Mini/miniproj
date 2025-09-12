import { useState } from "react";
import api from "../../api/axios" ;

// 화면전환을 위해서 임포트 
import { useNavigate } from "react-router-dom";

const SignUpPage = () => {
    
    const [email, setEmail]   = useState('');
    const [passwd, setPasswd] = useState('');
    const [gender, setGender] = useState('');

    // 컴포넌트 이동을 위한 useNavigate() 혹 사용
    const moveUrl = useNavigate(); 

    
    const emailHandler = (e) => {
        setEmail(e.target.value); 
        console.log("[debug] >>> email = " , email);        
    }
    const passwdHandler = (e) => {
        setPasswd(e.target.value); 
        console.log("[debug] >>> passwd = " , passwd);        
    }
    const genderHandler = (e) => {
        console.log("[debug] 1>>> gender = " , gender);
        setGender(e.target.value); 
    }
    const submitHandler = async (email, passwd, gender) => {
        console.log("[debug] >>> gender = " , email, passwd, gender);
        
        /*
        fetch api , axios 통신을 통해서 데이터를 전달받고 
        필요한 경우 화면 전환 및 전달받은 데이터를 
        props 전달 또는 
        context api 나 외부전역상태관리 라이브러리를 이용해서 공유할 수 있다. 
        */

        

        /*
        post   : 입력
        get    : 데이터 가져올 때
        
        // 204 : 요청은 성공했고 서버로부터 전달되는 콘텐츠가 없을 때 
        put    : 데이터 수정(리소스 전체를 수정하거나 새로 생성)
        delete : 데이터 삭제  
        patch  : 데이터 일부 수정
        */
        const data = {
            email  : email ,
            passwd : passwd ,
            gender : gender
        } 
        try {
            const response = await api.post('users', data) ;
            console.log("[debug] >>> axios /users post ");
            console.log("[debug] >>> response ");
            console.log("[debug] >>> " , response);
            if( response.status == 201) {
                // router-dom 이용해서 컴포넌트 transition 
                moveUrl('/loginForm'); 
            }
        } catch(error) {

        } finally {

        }
        
         

    }
    return (
        <div>
            <form>
                <label>
                    이메일 :  
                    <input  type="text" 
                            placeholder="email"
                            value={email}
                            onChange={emailHandler} /><br/>
                    패스워드 :  
                    <input  type="text" 
                            placeholder="password"
                            value={passwd}
                            onChange={passwdHandler} /><br/>
                    성별 : 
                    <select value={gender}
                            onChange={genderHandler}>

                        <option value="남자">남자</option>
                        <option value="여자">여자</option>
                    </select>
                </label>
                <br/>
                <button type="button"
                        onClick={() => submitHandler(email, passwd, gender) }>가입</button>
            </form>           
        </div>
    ) ;
}

export default SignUpPage ; 
