

import UserGreeting from './UserGreeting';
import GuestGreeting from './GuestGreeting';

const Greeting = (props) => {
    const isFlag = props.isFlag ; 
    // if(isFlag) {
    //     return <UserGreeting />
    // } else {
    //     return <GuestGreeting />
    // }
    {
        return isFlag ?  <UserGreeting /> : <GuestGreeting />
    }
}

export default Greeting ;

