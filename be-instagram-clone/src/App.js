import logo from './logo.svg';
import './App.css';

// 라우터 돔을 이용해서 화면전환을 구성하고 싶다면? 
import { BrowserRouter, Routes, Route } from "react-router-dom" ;
import SignUpPage from './page/form/SignUpPage';
import SignInPage from './page/form/SingInPage';
import TodoList from './page/form/TodoList';
function App() {
  return (
    <div>
      <BrowserRouter>
        <h2>페이지 이동을 위한 라우터 연습</h2>
        <Routes>
          
          <Route path='/'             element={ <SignUpPage /> }></Route>  
          <Route path='/loginForm'    element={ <SignInPage /> }></Route>  
          <Route path='/todos/list'   element={ <TodoList /> }></Route>  
          
        </Routes>    
      </BrowserRouter>  
    </div>
  );
}

export default App;
