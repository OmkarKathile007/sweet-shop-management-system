import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { client } from '../api/client';
import axios from 'axios';
import {type ApiError } from '../types';

export const AddSweetPage: React.FC = () => {
  const navigate = useNavigate();
  const [formData, setFormData] = useState({
    name: '',
    category: '',
    price: '',
    quantity: '',
    description: ''
  });
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);

  const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>) => {
    setFormData({
      ...formData,
      [e.target.name]: e.target.value
    });
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setLoading(true);
    setError('');

    try {
      // Convert strings to numbers for API
      const payload = {
        ...formData,
        price: parseFloat(formData.price),
        quantity: parseInt(formData.quantity)
      };

      await client.post('/api/sweets', payload);
      navigate('/sweets'); // Go back to shop after success
    } catch (err) {
      if (axios.isAxiosError(err) && err.response) {
        const apiError = err.response.data as ApiError;
        setError(apiError.message || 'Failed to add sweet');
      } else {
        setError('An unexpected error occurred');
      }
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="min-h-screen bg-gray-50 flex items-center justify-center p-4">
      <div className="bg-white p-8 rounded shadow-md w-full max-w-lg">
        <div className="flex justify-between items-center mb-6">
          <h2 className="text-2xl font-bold text-gray-800">Add New Sweet</h2>
          <button onClick={() => navigate('/sweets')} className="text-gray-500 hover:text-gray-700">Cancel</button>
        </div>

        {error && <div className="bg-red-100 text-red-700 p-3 rounded mb-4">{error}</div>}

        <form onSubmit={handleSubmit} className="space-y-4">
          <div>
            <label className="block text-gray-700 font-bold mb-1">Sweet Name</label>
            <input name="name" onChange={handleChange} required className="w-full border rounded p-2" placeholder="e.g. Motichoor Laddu" />
          </div>

          <div>
            <label className="block text-gray-700 font-bold mb-1">Category</label>
            <input name="category" onChange={handleChange} required className="w-full border rounded p-2" placeholder="e.g. Laddu" />
          </div>

          <div className="grid grid-cols-2 gap-4">
            <div>
              <label className="block text-gray-700 font-bold mb-1">Price ($)</label>
              <input name="price" type="number" step="0.01" onChange={handleChange} required className="w-full border rounded p-2" />
            </div>
            <div>
              <label className="block text-gray-700 font-bold mb-1">Quantity</label>
              <input name="quantity" type="number" onChange={handleChange} required className="w-full border rounded p-2" />
            </div>
          </div>

          <div>
            <label className="block text-gray-700 font-bold mb-1">Description</label>
            <textarea name="description" onChange={handleChange} rows={3} className="w-full border rounded p-2" placeholder="Describe the taste..."></textarea>
          </div>

          <button
            type="submit"
            disabled={loading}
            className="w-full bg-yellow-500 hover:bg-yellow-600 text-white font-bold py-3 rounded transition-colors"
          >
            {loading ? 'Adding...' : 'Add Sweet to Shop'}
          </button>
        </form>
      </div>
    </div>
  );
};