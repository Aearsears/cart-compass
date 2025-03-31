import axios from 'axios';

const API_URL = 'https://your-backend-api.com';

export const getPrices = async (product) => {
    const response = await axios.get(`${API_URL}/prices?product=${product}`);
    return response.data;
};
