import { useContext, useState } from 'react'
import reactLogo from './assets/react.svg'
import viteLogo from '/vite.svg'
import './App.css'

function App() {
  const [isLogin, setIsLogin] = useContext(false);

  return (
    <>
      isLogin? <AuthedApp /> : <UnAuthedApp />
    </>
  )
}

export default App
