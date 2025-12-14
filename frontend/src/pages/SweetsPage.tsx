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
  const [cart, setCart] = useState<Record<number, number>>({});
  const [user, setUser] = useState<UserData | null>(null);
  const [loading, setLoading] = useState(true);
  const [ordering, setOrdering] = useState(false);
  const [error, setError] = useState('');
  const [successMsg, setSuccessMsg] = useState('');
  
  // Search State
  const [searchTerm, setSearchTerm] = useState('');

  // --- ADMIN SPECIFIC STATE ---
  // Tracks which sweet is currently being edited
  const [editingId, setEditingId] = useState<number | null>(null);
  // Holds the form data for the sweet currently being edited
  const [editForm, setEditForm] = useState<Partial<Sweet>>({});
  // Holds the input values for restock (mapped by sweet ID)
  const [restockInputs, setRestockInputs] = useState<Record<number, string>>({});

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

  const handleSearch = async () => {
    setLoading(true);
    setError('');
    try {
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

  const handleLogout = () => {
    localStorage.clear();
    navigate('/login');
  };

  // --- CUSTOMER LOGIC ---
  const updateQuantity = (sweetId: number, change: number, max: number) => {
    setCart((prev) => {
      const currentQty = prev[sweetId] || 0;
      const newQty = currentQty + change;
      if (newQty < 0) return prev;
      if (newQty > max) return prev;
      if (newQty === 0) {
        const { [sweetId]: _, ...rest } = prev;
        return rest;
      }
      return { ...prev, [sweetId]: newQty };
    });
  };

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
      setCart({});
      fetchSweets();
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

  // --- ADMIN LOGIC: DELETE ---
  
  const handleDelete = async (id: number) => {
    if (!window.confirm("Are you sure you want to delete this sweet?")) return;
    
    try {
      await client.delete(`/api/sweets/${id}`);
      
      // If successful:
      setSweets(prev => prev.filter(s => s.id !== id));
      setSuccessMsg("Sweet deleted successfully.");
      setError(''); // Clear any old errors
      
    } catch (err) {
      // Check if it's the specific conflict error we defined in backend
      if (axios.isAxiosError(err) && err.response?.status === 409) {
        alert(err.response.data); // "Cannot delete... set Quantity to 0"
      } else {
        console.error(err);
        alert("Failed to delete sweet.");
      }
    }
  };
  


  // --- ADMIN LOGIC: EDIT (Update) ---
  const startEditing = (sweet: Sweet) => {
    setEditingId(sweet.id);
    setEditForm({
      name: sweet.name,
      price: sweet.price,
      
      // Note: We are not editing images as per requirement
    });
  };

  const cancelEditing = () => {
    setEditingId(null);
    setEditForm({});
  };

  const saveEdit = async (id: number) => {
    try {
      // Backend Endpoint: PUT /api/sweets/:id
      const { data } = await client.put<Sweet>(`/api/sweets/${id}`, editForm);
      
      // Update local state
      setSweets(prev => prev.map(s => s.id === id ? data : s));
      setEditingId(null);
      setSuccessMsg("Sweet updated successfully!");
    } catch (err) {
      console.error(err);
      alert("Failed to update sweet details.");
    }
  };

  // --- ADMIN LOGIC: RESTOCK ---
  const handleRestockInput = (id: number, value: string) => {
    setRestockInputs(prev => ({ ...prev, [id]: value }));
  };

  const handleRestockSubmit = async (id: number) => {
    const qtyStr = restockInputs[id];
    const qty = parseInt(qtyStr);

    if (!qty || qty <= 0) {
      alert("Please enter a valid positive quantity.");
      return;
    }

    try {
      // Backend Endpoint: POST /api/sweets/:id/restock?quantity=...
      const { data } = await client.post<Sweet>(`/api/sweets/${id}/restock`, null, {
        params: { quantity: qty }
      });

      // Update local state
      setSweets(prev => prev.map(s => s.id === id ? data : s));
      // Clear input
      setRestockInputs(prev => ({ ...prev, [id]: '' }));
      setSuccessMsg("Restock successful!");
    } catch (err) {
      console.error(err);
      alert("Failed to restock.");
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
        
        {/* --- Search Bar --- */}
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

        {/* --- Admin Add Button --- */}
        {user?.role === 'ADMIN' && (
          <div className="bg-yellow-50 border-l-4 border-yellow-400 p-4 mb-6 flex justify-between items-center">
            <div>
              <p className="font-bold text-yellow-700">Admin Controls</p>
              <p className="text-sm text-yellow-600">Manage your inventory below.</p>
            </div>
            <button 
              onClick={() => navigate('/add-sweet')}
              className="bg-yellow-500 hover:bg-yellow-600 text-white px-4 py-2 rounded shadow"
            >
              + Add New Sweet
            </button>
          </div>
        )}

        {/* --- Messages --- */}
        {error && <div className="bg-red-100 text-red-700 p-3 rounded mb-4">{error}</div>}
        {successMsg && <div className="bg-green-100 text-green-700 p-3 rounded mb-4">{successMsg}</div>}

        {/* --- Sweet Grid --- */}
        {sweets.length === 0 ? (
           <div className="text-center text-gray-500 py-10">No sweets found.</div>
        ) : (
          <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
            {sweets.map((sweet) => {
              const currentQty = cart[sweet.id] || 0;
              const isOutOfStock = sweet.quantity === 0;
              const isEditing = editingId === sweet.id;

              return (
                <div key={sweet.id} className={`bg-white rounded-lg shadow p-6 relative flex flex-col justify-between ${isOutOfStock && !isEditing ? 'opacity-75 bg-gray-100' : ''}`}>
                  
                  {/* --- CARD CONTENT --- */}
                  {isEditing ? (
                    /* --- EDIT MODE --- */
                    <div className="flex flex-col gap-3">
                        <label className="text-xs font-bold text-gray-500">Name</label>
                        <input 
                            className="border p-2 rounded"
                            value={editForm.name || ''}
                            onChange={e => setEditForm({...editForm, name: e.target.value})}
                        />
                        
                        <label className="text-xs font-bold text-gray-500">Price</label>
                        <input 
                            className="border p-2 rounded"
                            type="number"
                            value={editForm.price || ''}
                            onChange={e => setEditForm({...editForm, price: parseFloat(e.target.value)})}
                        />

                        <label className="text-xs font-bold text-gray-500">Description</label>
                        

                        <div className="flex gap-2 mt-2">
                            <button onClick={() => saveEdit(sweet.id)} className="bg-green-600 text-white px-3 py-1 rounded w-full">Save</button>
                            <button onClick={cancelEditing} className="bg-gray-400 text-white px-3 py-1 rounded w-full">Cancel</button>
                        </div>
                    </div>
                  ) : (
                    /* --- VIEW MODE --- */
                    <>
                      {/* Admin: Delete Button */}
                      {user?.role === 'ADMIN' && (
                        <button
                          onClick={() => handleDelete(sweet.id)}
                          className="absolute top-2 right-2 text-red-400 hover:text-red-600 p-1 font-bold text-lg"
                          title="Delete Sweet"
                        >
                          ✕
                        </button>
                      )}

                      <div>
                        <div className="flex justify-between items-start pr-8">
                          <h2 className="text-xl font-bold text-gray-800">{sweet.name}</h2>
                        </div>
                        
                        {/* <p className="text-gray-600 mt-2 text-sm italic">{sweet.description}</p> */}
                        
                        <div className="mt-3">
                            <p className="text-gray-800 font-medium">Price: <span className="text-green-700 font-bold">₹{sweet.price.toFixed(2)}</span></p>
                            <p className={`text-sm mt-1 ${isOutOfStock ? 'text-red-600 font-bold' : 'text-blue-600'}`}>
                                Stock: {sweet.quantity}
                            </p>
                        </div>
                      </div>

                      {/* --- CONTROLS SECTION --- */}
                      <div className="mt-4 pt-4 border-t border-gray-100">
                        
                        {user?.role === 'ADMIN' ? (
                            /* --- ADMIN CONTROLS --- */
                            <div className="flex flex-col gap-3">
                                {/* Edit Button */}
                                <button 
                                    onClick={() => startEditing(sweet)}
                                    className="bg-blue-500 hover:bg-blue-600 text-white py-1 px-3 rounded text-sm w-full"
                                >
                                    Edit Details
                                </button>
                                
                                {/* Restock Section */}
                                <div className="flex items-center gap-2">
                                    <input 
                                        type="number"
                                        placeholder="Add Qty"
                                        className="border rounded p-1 w-24 text-sm"
                                        value={restockInputs[sweet.id] || ''}
                                        onChange={(e) => handleRestockInput(sweet.id, e.target.value)}
                                    />
                                    <button 
                                        onClick={() => handleRestockSubmit(sweet.id)}
                                        className="bg-orange-500 hover:bg-orange-600 text-white py-1 px-3 rounded text-sm flex-1"
                                    >
                                        Restock
                                    </button>
                                </div>
                            </div>
                        ) : (
                            /* --- USER CONTROLS (Cart) --- */
                            <div className="flex items-center justify-between">
                                <span className="text-sm text-gray-500">Add to Cart:</span>
                                <div className="flex items-center border rounded bg-white">
                                <button 
                                    onClick={() => updateQuantity(sweet.id, -1, sweet.quantity)}
                                    disabled={currentQty === 0 || isOutOfStock}
                                    className="px-3 py-1 bg-gray-100 hover:bg-gray-200 disabled:opacity-50 text-gray-700"
                                >
                                    -
                                </button>
                                <span className="px-3 py-1 font-medium w-8 text-center">{currentQty}</span>
                                <button 
                                    onClick={() => updateQuantity(sweet.id, 1, sweet.quantity)}
                                    disabled={currentQty >= sweet.quantity || isOutOfStock}
                                    className="px-3 py-1 bg-gray-100 hover:bg-gray-200 disabled:opacity-50 text-gray-700"
                                >
                                    +
                                </button>
                                </div>
                            </div>
                        )}
                      </div>
                    </>
                  )}
                </div>
              );
            })}
          </div>
        )}
      </main>

      {/* --- Sticky Footer for Order (User Only) --- */}
      {user?.role === 'USER' && Object.keys(cart).length > 0 && (
        <div className="fixed bottom-0 left-0 right-0 bg-white border-t shadow-lg p-4 z-50">
          <div className="max-w-7xl mx-auto flex justify-between items-center">
            <div>
              <p className="font-bold text-lg">
                Total Items: {Object.values(cart).reduce((a, b) => a + b, 0)}
              </p>
            </div>
            <button
              onClick={handlePlaceOrder}
              disabled={ordering}
              className="bg-green-600 hover:bg-green-700 text-white font-bold py-2 px-6 rounded shadow disabled:opacity-70"
            >
              {ordering ? 'Processing...' : 'Place Order'}
            </button>
          </div>
        </div>
      )}
    </div>
  );
};