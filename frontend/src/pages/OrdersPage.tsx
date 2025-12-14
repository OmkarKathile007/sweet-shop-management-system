import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { client } from '../api/client';
import { type OrderResponse } from '../types';

export const OrdersPage: React.FC = () => {
  const navigate = useNavigate();
  const [orders, setOrders] = useState<OrderResponse[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  useEffect(() => {
    fetchOrders();
  }, []);

  const fetchOrders = async () => {
    try {
      const { data } = await client.get<OrderResponse[]>('/api/orders');
      setOrders(data);
    } catch (err) {
      setError('Failed to load order history.');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="min-h-screen bg-gray-50 pb-20">
      <header className="bg-white shadow mb-8">
        <div className="max-w-7xl mx-auto py-6 px-4 sm:px-6 lg:px-8 flex justify-between items-center">
          <h1 className="text-3xl font-bold text-gray-900">My Orders</h1>
          <button 
            onClick={() => navigate('/sweets')}
            className="text-blue-600 hover:text-blue-800 font-medium"
          >
            ← Back to Shop
          </button>
        </div>
      </header>

      <main className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        {loading && <div className="text-center">Loading history...</div>}
        {error && <div className="bg-red-100 text-red-700 p-4 rounded mb-4">{error}</div>}
        
        {!loading && orders.length === 0 && (
          <div className="text-center text-gray-500 py-10 bg-white rounded shadow">
            You haven't placed any orders yet.
          </div>
        )}

        <div className="space-y-4">
          {orders.map((order) => (
            <div key={order.id} className="bg-white shadow overflow-hidden sm:rounded-lg p-6 border-l-4 border-blue-500">
              <div className="flex justify-between items-center">
                <div>
                  <h3 className="text-lg leading-6 font-medium text-gray-900">
                    Order #{order.id}
                  </h3>
                  <p className="mt-1 max-w-2xl text-sm text-gray-500">
                    Placed on: {new Date(order.orderDate).toLocaleString()}
                  </p>
                </div>
                <div className="text-right">
                  <p className="text-2xl font-bold text-gray-900">
                    ₹{(order.totalPrice || 0).toFixed(2)}
                  </p>
                  <span className="inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium bg-green-100 text-green-800">
                    Completed
                  </span>
                </div>
              </div>
            </div>
          ))}
        </div>
      </main>
    </div>
  );
};