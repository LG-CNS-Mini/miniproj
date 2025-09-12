import { useState } from "react";
import api from "../../api/axios" ;
import { useNavigate } from "react-router-dom";

const SignInPage  = () => {

    const moveUrl = useNavigate(); 

    const [email, setEmail]   = useState('');
    const [passwd, setPasswd] = useState('');
    
    const emailHandler = (e) => {
        setEmail(e.target.value); 
        console.log("[debug] >>> email = " , email);        
    }
    const passwdHandler = (e) => {
        setPasswd(e.target.value); 
        console.log("[debug] >>> passwd = " , passwd);        
    }

    const submitHandler = async (email, passwd) => {
        // 인증(Authentication) -  JWT(Json Web Token)
        // select * from from table where email=? and passwd=? ;
        // access token(header)  
        // 다시 요청시 해당 토큰을 axios header 담아서 전달해야함!!!
        /*
            api.get(`/login?email=?&passwd=?` , {
                headers : {
                    Authorization : `Bearer ${token}`
                }
            })
        */
        // const response = await api.get(`/users?email=${email}&passwd=${passwd}`);
        await api.get(`/todos`)
                .then( response => {
                    moveUrl('/todos/list' , {
                        state : response.data 
                    })
                })
                .catch( err => {
                    console.log("[debug] >>> " , err); 
                });
        
        

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
                </label>
                <br/>
                <button type="button"
                        onClick={() => submitHandler(email, passwd) }>로그인</button>
            </form>           
        </div>
    ) ;
}

export default SignInPage ;
