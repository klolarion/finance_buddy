
import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import Header from './components/Header'; // 헤더 컴포넌트 추가
import LoginPage from './pages/LoginPage';
import SocialLoginPage from './pages/SocialLoginPage';
import SignupPage from './pages/SignupPage';
import InputProfilePage from './pages/InputProfilePage';
import IndexPage from './pages/IndexPage';
import MyPage from './pages/MyPage';

const App = () => {
  return (
      <Router>
        <Header />
        <Routes>
          <Route path="/" element={<LoginPage />} />
          <Route path="/social-login" element={<SocialLoginPage />} />
          <Route path="/signup" element={<SignupPage />} />
          <Route path="/input-profile" element={<InputProfilePage />} />
          <Route path="/index" element={<IndexPage />} />
          <Route path="/my-page" element={<MyPage />} />
        </Routes>
      </Router>
  );
};

export default App;
