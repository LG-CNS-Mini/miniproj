import React from 'react';
import ReactDOM from 'react-dom/client';
import './index.css';
import App from './App';
import reportWebVitals from './reportWebVitals';

// import TestPage from "./page/TestPage" ;
// const root = ReactDOM.createRoot(document.getElementById('root'));
// root.render(
//   <React.StrictMode>
//     <TestPage />
//   </React.StrictMode>
// );


// react-router-dom 이동한 화면전환
// const root = ReactDOM.createRoot(document.getElementById('root'));
// root.render(

//   <App />
  
// );


// import LibraryPage from "./page/LibraryPage" ;
// const root = ReactDOM.createRoot(document.getElementById('root'));
// root.render(
//   <React.StrictMode>
//     <LibraryPage />
//   </React.StrictMode>
// );


// import CommentPage from "./page/CommentPage" ;
// const root = ReactDOM.createRoot(document.getElementById('root'));
// root.render(
//   <React.StrictMode>
//     <CommentPage />
//   </React.StrictMode>
// );

// import TodosPage from "./page/TodosPage" ;
// const root = ReactDOM.createRoot(document.getElementById('root'));
// root.render(
//   <React.StrictMode>
//     <TodosPage />
//   </React.StrictMode>
// );

// import UserPage from "./page/rendering/UserPage" ;
// const root = ReactDOM.createRoot(document.getElementById('root'));
// root.render(
//   <React.StrictMode>
//     <UserPage />
//   </React.StrictMode>
// );


// import StateSample from "./page/state/StateSample" ;
// const root = ReactDOM.createRoot(document.getElementById('root'));
// root.render(
//   <React.StrictMode>
//     <StateSample />
//   </React.StrictMode>
// );


// import NotificationPage from "./page/hook/NotificationPage" ;
// const root = ReactDOM.createRoot(document.getElementById('root'));
// root.render(
  
//   <NotificationPage />
  
// );


// import SignUpPage from "./page/form/SignUpPage" ;
// const root = ReactDOM.createRoot(document.getElementById('root'));
// root.render(
//     <SignUpPage />
// );


// context를 활용한 전역변수 사용
// import ContextApp from "./page/ContextApp" ;
// const root = ReactDOM.createRoot(document.getElementById('root'));
// root.render(
//   <ContextApp />
// );


import BlogApp from "./BlogApp" ; 
const root = ReactDOM.createRoot(document.getElementById('root'));
root.render(
  <BlogApp />
);


// import WeatherApp from "./WeatherApp" ;
// const root = ReactDOM.createRoot(document.getElementById('root'));
// root.render(
//   <WeatherApp />
// );

// import LoginPage from './page/LoginPage';
// const root = ReactDOM.createRoot(document.getElementById('root'));
// root.render(
  
//   <LoginPage />
  
// );

// If you want to start measuring performance in your app, pass a function
// to log results (for example: reportWebVitals(console.log))
// or send to an analytics endpoint. Learn more: https://bit.ly/CRA-vitals
reportWebVitals();
