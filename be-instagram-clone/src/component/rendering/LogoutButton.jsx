

const LogoutButton = (props) => {
    const logoutHandler = (setIsFlag) => {
        console.log("[debug] logoutHandler click"); 
        setIsFlag( isFlag => isFlag = false ); 
    } ;

    return (
        <button type="button" onClick={() => logoutHandler(props.login)}>
            로그아웃
        </button>
    );
}

export default LogoutButton ;