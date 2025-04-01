import axios from 'axios';

const API_URL = 'https://your-backend-api.com';

export const getPrices = async (query, store = '', location = '') => {
    // Simulated API call - replace with actual backend call
    return [
        { store: 'Walmart', price: 2.99 },
        { store: 'Costco', price: 2.79 },
        { store: 'Target', price: 3.19 }
    ].filter((item) => !store || item.store === store); // Filter by store if selected
};

export const getStores = async () => {
    // Simulated API call to fetch available stores
    return ['Walmart', 'Costco', 'Target'];
};

export const getPriceHistory = async (query) => {
    // Simulated API call - replace with actual backend logic
    return [
        { date: '2025-03-28', price: 3.1 },
        { date: '2025-03-29', price: 3.05 },
        { date: '2025-03-30', price: 2.99 }
    ];
};
