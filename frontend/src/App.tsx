

import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import Header from './components/Header'; // 헤더 컴포넌트 추가
import LoginPage from './pages/LoginPage';
import SignupPage from './pages/SigninPage';
import Index from './pages/Index';

const App = () => {
  return (
      <Router>
        <Header />
        <Routes>
          <Route path="/" element={<Index />} />
          <Route path="/login" element={<LoginPage />} />
          <Route path="/signup" element={<SignupPage />} />
        </Routes>
      </Router>
  );
};

export default App;
