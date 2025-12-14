import React, { useEffect, useState } from 'react';
import api from '../services/api';
import { Sweet } from '../types';
import SweetCard from '../components/SweetCard';
import { Loader } from 'lucide-react';

const Menu: React.FC = () => {
  const [sweets, setSweets] = useState<Sweet[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  useEffect(() => {
    fetchSweets();
  }, []);

  const fetchSweets = async () => {
    try {
      const response = await api.get<Sweet[]>('/sweets');
      setSweets(response.data);
    } catch (err) {
      setError('Failed to load menu. Please try again.');
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  if (loading) return (
    <div className="flex justify-center items-center min-h-[50vh]">
      <Loader className="animate-spin text-primary-500" size={40} />
    </div>
  );

  if (error) return (
    <div className="text-center text-red-500 py-10 bg-red-50 rounded-xl">
      {error}
    </div>
  );

  return (
    <div className="space-y-8">
      <div className="text-center max-w-2xl mx-auto">
        <h1 className="text-4xl font-bold text-chocolate-900 mb-4">Our Sweet Menu</h1>
        <p className="text-gray-600">Handcrafted with love and tradition. Choose your favorites.</p>
      </div>

      {sweets.length === 0 ? (
        <div className="text-center text-gray-500 py-10">No sweets found.</div>
      ) : (
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-8">
          {sweets.map(sweet => (
            <SweetCard key={sweet.id} sweet={sweet} />
          ))}
        </div>
      )}
    </div>
  );
};

export default Menu;