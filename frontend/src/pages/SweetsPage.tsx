import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { client } from '../api/client';
import { type Sweet, type OrderRequest, type ApiError } from '../types';
import axios from 'axios';

interface UserData {
  username: string;
  role: 'USER' | 'ADMIN';
}

export const SweetsPage: React.FC = () => {
  const navigate = useNavigate();
  const [sweets, setSweets] = useState<Sweet[]>([]);
  const [cart, setCart] = useState<Record<number, number>>({}); // sweetId -> quantity
  const [user, setUser] = useState<UserData | null>(null);
  const [loading, setLoading] = useState(true);
  const [ordering, setOrdering] = useState(false);
  const [error, setError] = useState('');
  const [successMsg, setSuccessMsg] = useState('');
  
  // NEW: State for Search
  const [searchTerm, setSearchTerm] = useState('');

  // 1. Initialize User & Fetch Data
  useEffect(() => {
    const storedUser = localStorage.getItem('user');
    if (!storedUser) {
      navigate('/login');
      return;
    }
    setUser(JSON.parse(storedUser));
    fetchSweets();
  }, [navigate]);

  const fetchSweets = async () => {
    try {
      const { data } = await client.get<Sweet[]>('/api/sweets');
      setSweets(data);
    } catch (err) {
      setError('Failed to load sweets.');
    } finally {
      setLoading(false);
    }
  };

  // NEW: Search Handler
  const handleSearch = async () => {
    setLoading(true);
    setError('');
    try {
      // Calls the new backend endpoint
      const { data } = await client.get<Sweet[]>('/api/sweets/search', {
        params: { name: searchTerm }
      });
      setSweets(data);
    } catch (err) {
      console.error(err);
      setError('Search failed. Please try again.');
    } finally {
      setLoading(false);
    }
  };

  // 2. Handle Logout
  const handleLogout = () => {
    localStorage.clear();
    navigate('/login');
  };

  // 3. Handle Quantity Changes
  const updateQuantity = (sweetId: number, change: number, max: number) => {
    setCart((prev) => {
      const currentQty = prev[sweetId] || 0;
      const newQty = currentQty + change;
      
      if (newQty < 0) return prev;
      if (newQty > max) return prev;
      
      // If quantity is 0, remove from cart tracking
      if (newQty === 0) {
        const { [sweetId]: _, ...rest } = prev;
        return rest;
      }

      return { ...prev, [sweetId]: newQty };
    });
  };

  // 4. Place Order API Call
  const handlePlaceOrder = async () => {
    setOrdering(true);
    setError('');
    setSuccessMsg('');

    const items = Object.entries(cart).map(([sweetId, quantity]) => ({
      sweetId: Number(sweetId),
      quantity,
    }));

    if (items.length === 0) {
      setError('Your cart is empty.');
      setOrdering(false);
      return;
    }

    try {
      const payload: OrderRequest = { items };
      await client.post('/api/orders', payload);
      setSuccessMsg('Order placed successfully!');
      setCart({}); // Clear cart
      fetchSweets(); // Refresh stock
    } catch (err) {
      if (axios.isAxiosError(err) && err.response) {
        const apiError = err.response.data as ApiError;
        setError(apiError.message || 'Failed to place order.');
      } else {
        setError('An unexpected error occurred.');
      }
    } finally {
      setOrdering(false);
    }
  };

  const handleDelete = async (id: number) => {
    if (!window.confirm("Are you sure you want to delete this sweet?")) return;
    try {
      await client.delete(`/api/sweets/${id}`);
      setSweets(prev => prev.filter(s => s.id !== id)); // Remove from UI immediately
    } catch (err) {
      alert("Failed to delete sweet");
    }
  };

  if (loading) return <div className="p-10 text-center">Loading sweets...</div>;

  return (
    <div className="min-h-screen bg-gray-50 pb-20">
      {/* --- Header --- */}
      <header className="bg-white shadow">
        <div className="max-w-7xl mx-auto py-6 px-4 sm:px-6 lg:px-8 flex justify-between items-center">
          <div>
            <h1 className="text-3xl font-bold text-gray-900">Sweet Shop</h1>
            <p className="text-sm text-gray-500">Welcome, {user?.username} ({user?.role})</p>
          </div>
          
          <div className="flex gap-4">
            <button 
              onClick={() => navigate('/orders')}
              className="text-gray-600 hover:text-blue-600 font-medium transition-colors"
            >
              My Orders
            </button>
            <button 
              onClick={handleLogout}
              className="text-red-600 hover:text-red-800 font-medium"
            >
              Logout
            </button>
          </div>
        </div>
      </header>

      <main className="max-w-7xl mx-auto py-6 px-4 sm:px-6 lg:px-8">
        
        {/* --- NEW: Search Bar --- */}
        <div className="mb-8 flex gap-2">
          <input 
            type="text" 
            value={searchTerm}
            onChange={(e) => setSearchTerm(e.target.value)}
            placeholder="Search sweets by name..."
            className="border p-3 rounded-lg w-full max-w-md shadow-sm focus:ring-2 focus:ring-blue-500 outline-none"
          />
          <button 
            onClick={handleSearch}
            className="bg-blue-600 text-white px-6 py-2 rounded-lg hover:bg-blue-700 font-medium shadow-sm"
          >
            Search
          </button>
          <button 
            onClick={() => { setSearchTerm(''); fetchSweets(); }} 
            className="bg-white border border-gray-300 text-gray-700 px-4 py-2 rounded-lg hover:bg-gray-50 font-medium shadow-sm"
          >
            Reset
          </button>
        </div>

        {/* --- Admin UI Section (Role Check) --- */}
        {user?.role === 'ADMIN' && (
          <div className="bg-yellow-50 border-l-4 border-yellow-400 p-4 mb-6">
            <div className="flex justify-between items-center">
              <div>
                <p className="font-bold text-yellow-700">Admin Controls</p>
                <p className="text-sm text-yellow-600">You have permission to manage sweets.</p>
              </div>
              <button 
                onClick={() => navigate('/add-sweet')}
                className="bg-yellow-500 hover:bg-yellow-600 text-white px-4 py-2 rounded shadow"
              >
                + Add New Sweet
              </button>
            </div>
          </div>
        )}

        {/* --- Messages --- */}
        {error && <div className="bg-red-100 text-red-700 p-3 rounded mb-4">{error}</div>}
        {successMsg && <div className="bg-green-100 text-green-700 p-3 rounded mb-4">{successMsg}</div>}

        {/* --- Sweet Grid --- */}
        {sweets.length === 0 ? (
           <div className="text-center text-gray-500 py-10">No sweets found matching your search.</div>
        ) : (
          <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
            {sweets.map((sweet) => {
              const currentQty = cart[sweet.id] || 0;
              const isOutOfStock = sweet.quantity === 0;

              return (
                <div key={sweet.id} className={`bg-white rounded-lg shadow p-6 relative ${isOutOfStock ? 'opacity-75 bg-gray-100' : ''}`}>
                  
                  {/* --- Delete Button (Top Right) --- */}
                  {user?.role === 'ADMIN' && (
                    <button
                      onClick={() => handleDelete(sweet.id)}
                      className="absolute top-2 right-2 text-red-400 hover:text-red-600 p-1"
                      title="Delete Sweet"
                    >
                      ✕
                    </button>
                  )}
                  
                  <div className="flex justify-between items-start">
                    <h2 className="text-xl font-bold text-gray-800">{sweet.name}</h2>
                    <span className="bg-blue-100 text-blue-800 text-xs font-semibold px-2.5 py-0.5 rounded">
                      {sweet.category}
                    </span>
                  </div>
                  
                  <p className="text-gray-600 mt-2">Price: <span className="font-semibold">${sweet.price.toFixed(2)}</span></p>
                  <p className={`text-sm mt-1 ${isOutOfStock ? 'text-red-600 font-bold' : 'text-green-600'}`}>
                    {isOutOfStock ? 'Out of Stock' : `Available: ${sweet.quantity}`}
                  </p>

                  {/* Quantity Controls */}
                  <div className="mt-4 flex items-center justify-between">
                    <div className="flex items-center border rounded">
                      <button 
                        onClick={() => updateQuantity(sweet.id, -1, sweet.quantity)}
                        disabled={currentQty === 0 || isOutOfStock}
                        className="px-3 py-1 bg-gray-200 hover:bg-gray-300 disabled:opacity-50"
                      >
                        -
                      </button>
                      <span className="px-4 py-1 font-medium">{currentQty}</span>
                      <button 
                        onClick={() => updateQuantity(sweet.id, 1, sweet.quantity)}
                        disabled={currentQty >= sweet.quantity || isOutOfStock}
                        className="px-3 py-1 bg-gray-200 hover:bg-gray-300 disabled:opacity-50"
                      >
                        +
                      </button>
                    </div>
                  </div>
                </div>
              );
            })}
          </div>
        )}
      </main>

      {/* --- Sticky Footer for Order --- */}
      {Object.keys(cart).length > 0 && (
        <div className="fixed bottom-0 left-0 right-0 bg-white border-t shadow-lg p-4">
          <div className="max-w-7xl mx-auto flex justify-between items-center">
            <div>
              <p className="font-bold text-lg">
                Total Items: {Object.values(cart).reduce((a, b) => a + b, 0)}
              </p>
            </div>
            <button
              onClick={handlePlaceOrder}
              disabled={ordering}
              className="bg-green-600 hover:bg-green-700 text-white font-bold py-2 px-6 rounded shadow"
            >
              {ordering ? 'Processing...' : 'Place Order'}
            </button>
          </div>
        </div>
      )}
    </div>
  );
};