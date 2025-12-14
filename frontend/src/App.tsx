import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';
import { LoginPage } from './pages/LoginPage';
import { SweetsPage } from './pages/SweetsPage';

function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/login" element={<LoginPage />} />
        <Route path="/sweets" element={<SweetsPage />} />
        
        {/* Default redirect */}
        <Route path="/" element={<Navigate to="/sweets" replace />} />
      </Routes>
    </BrowserRouter>
  );
}

export default App;