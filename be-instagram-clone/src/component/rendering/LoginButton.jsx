


const LoginButton = (props) => {

    const loginHandler = (setIsFlag) => {
        console.log("[debug] loginHandler click"); 
        setIsFlag( isFlag => isFlag = true ); 
    } ;

    return (
        <button type="button" onClick={() => loginHandler(props.login) }>
            로그인
        </button>
    );
}

export default LoginButton ;