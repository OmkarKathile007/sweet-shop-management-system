import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';
import { LoginPage } from './pages/LoginPage';
import { RegisterPage } from './pages/RegisterPage';
import { SweetsPage } from './pages/SweetsPage';
import { OrdersPage } from './pages/OrdersPage';
import { AddSweetPage } from './pages/AddSweetPage';

function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/login" element={<LoginPage />} />
        <Route path="/register" element={<RegisterPage />} />
        <Route path="/sweets" element={<SweetsPage />} />
        <Route path="/orders" element={<OrdersPage />} />

        <Route path="/add-sweet" element={<AddSweetPage />} />
        
        <Route path="/" element={<Navigate to="/sweets" replace />} />
      </Routes>
    </BrowserRouter>
  );
}

export default App;