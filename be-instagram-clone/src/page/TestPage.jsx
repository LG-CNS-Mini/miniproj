import React, { useState, useEffect, useMemo, useRef, useCallback   } from 'react';

// function TestPage() {
//   const [count, setCount] = useState(0);
//   const [input, setInput] = useState('');

//   // 복잡한 계산 함수 (여기선 단순히 시간 지연)
//   const expensiveCalculation = (num) => {
//     console.time('📊 expensiveCalculation'); // 시간 측정 시작
//     let result = 0;
//     for (let i = 0; i < 1000000000; i++) {
//       result += num;
//     }
//     console.timeEnd('📊 expensiveCalculation'); // 시간 측정 끝
//     return result;
//   };

//   // useMemo를 사용해 count가 변경될 때만 계산 실행
//   const memoizedValue = useMemo(() => {
//     console.log("[debug] >>> useMemo  , ");
//     return expensiveCalculation(count);
//   }, [count]);

//   return (
//     <div>
//       <h2>useMemo 예제</h2>
//       <p>계산된 값: {memoizedValue}</p>
//       <button onClick={() => setCount(count + 1)}>카운트 증가</button>
//       <input
//         type="text"
//         value={input}
//         onChange={(e) => setInput(e.target.value)}
//         placeholder="입력값"
//       />
//     </div>
//   );
// }

// 기본 useRef 예제 – DOM 요소 접근
// function TestPage() {
//   const inputRef = useRef(null);

//   const handleFocus = () => {
//     // input DOM 요소에 직접 접근하여 포커스
//     inputRef.current.focus();
//   };

//   return (
//     <div>
//       <h2>useRef로 input 포커스</h2>
//       <input ref={inputRef} type="text" placeholder="여기에 입력" />
//       <button onClick={handleFocus}>포커스 주기</button>
//     </div>
//   );
// }

// 렌더링과 무관한 값 저장용 useRef 예제
// function TestPage() {
//   const [text, setText] = useState('');
//   const renderCount = useRef(1); // 렌더링될 때 증가시키는 변수

//   useEffect(() => {
//     renderCount.current += 1;
//     console.log(">>>>>>>>>>>>>> useEffect");
//   });

//   return (
//     <div>
//       <h2>렌더링 횟수: {renderCount.current}</h2>
//       <input
//         type="text"
//         value={text}
//         onChange={(e) => setText(e.target.value)}
//         placeholder="무언가 입력하세요"
//       />
//     </div>
//   );
// }


// useCallback
const Child = React.memo(({ onClick }) => {
  console.log('🔄 Child 렌더링');
  return <button onClick={onClick}>자식 버튼</button>;
});

function TestPage() {
  const [count, setCount] = useState(0);
  const [text, setText] = useState('');

  // useCallback 없이 만들면 매번 새로운 함수가 생성되어 Child가 매번 리렌더링됨
  const handleClick = useCallback(() => {
    console.log('자식 버튼 클릭');
  }, []); // 의존성 배열이 비어 있으므로 처음 생성된 함수가 재사용됨

  return (
    <div>
      <h2>useCallback 예제</h2>
      <p>카운트: {count}</p>
      <button onClick={() => setCount(count + 1)}>카운트 증가</button>
      <input
        type="text"
        value={text}
        onChange={(e) => setText(e.target.value)}
        placeholder="텍스트 입력"
      />
      <Child onClick={handleClick} />
    </div>
  );
}
export default TestPage;