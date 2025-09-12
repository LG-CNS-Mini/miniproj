import { useState } from "react";
import Greeting from "../../component/rendering/Greeting";
import LogoutButton from "../../component/rendering/LogoutButton";
import LoginButton from "../../component/rendering/LoginButton";

const UserPage = () => {

    const [isFlag , setIsFlag] = useState(false);
    
    return (
        <div>
            <Greeting isFlag={isFlag} />

            {
                isFlag  ? <LogoutButton login={setIsFlag}/> 
                        : <LoginButton  login={setIsFlag}/>  
            }
        </div>
    );
}

export default UserPage ;
