import { useState, useEffect } from 'react';
import { getPrices, getStores, getPriceHistory } from './api/grocery';
import ShoppingList from './components/ShoppingList';

function App() {
    const [query, setQuery] = useState('');
    const [prices, setPrices] = useState([]);
    const [shoppingList, setShoppingList] = useState([]);
    const [stores, setStores] = useState([]);
    const [selectedStore, setSelectedStore] = useState('');
    const [location, setLocation] = useState('');
    const [sortOption, setSortOption] = useState('price');
    const [priceHistory, setPriceHistory] = useState([]);

    useEffect(() => {
        const fetchStores = async () => {
            const storeData = await getStores();
            setStores(storeData);
        };
        fetchStores();
    }, []);

    const searchPrices = async () => {
        const data = await getPrices(query, selectedStore, location);
        const history = await getPriceHistory(query);
        setPrices(data);
        setPriceHistory(history);
    };

    const sortedPrices = [...prices].sort(
        (a, b) => a[sortOption] - b[sortOption]
    );

    return (
        <div className="p-6">
            <h1 className="text-xl font-bold">Grocery Price Tracker</h1>
            <input
                type="text"
                placeholder="Search for a product..."
                className="border p-2 mt-4"
                value={query}
                onChange={(e) => setQuery(e.target.value)}
            />
            <select
                className="border p-2 ml-2"
                onChange={(e) => setSelectedStore(e.target.value)}
                value={selectedStore}>
                <option value="">All Stores</option>
                {stores.map((store) => (
                    <option
                        key={store}
                        value={store}>
                        {store}
                    </option>
                ))}
            </select>
            <select
                className="border p-2 ml-2"
                onChange={(e) => setLocation(e.target.value)}
                value={location}>
                <option value="">Select Region</option>
                <option value="North">North</option>
                <option value="South">South</option>
                <option value="East">East</option>
                <option value="West">West</option>
            </select>
            <select
                className="border p-2 ml-2"
                onChange={(e) => setSortOption(e.target.value)}
                value={sortOption}>
                <option value="price">Sort by Price</option>
                <option value="store">Sort by Store</option>
            </select>
            <button
                className="bg-blue-500 text-white px-4 py-2 ml-2"
                onClick={searchPrices}>
                Search
            </button>

            <ul className="mt-4">
                {sortedPrices.map((item, index) => (
                    <li
                        key={index}
                        className="border p-2 my-2 flex justify-between">
                        {item.store}: ${item.price.toFixed(2)}
                        <button
                            className="bg-green-500 text-white px-2 py-1 ml-2"
                            onClick={() =>
                                setShoppingList([...shoppingList, item])
                            }>
                            Add to List
                        </button>
                    </li>
                ))}
            </ul>

            <div className="mt-6">
                <h2 className="text-lg font-bold">Price History</h2>
                <ul>
                    {priceHistory.map((entry, index) => (
                        <li
                            key={index}
                            className="border p-2 my-2">
                            {entry.date}: ${entry.price.toFixed(2)}
                        </li>
                    ))}
                </ul>
            </div>

            <ShoppingList
                shoppingList={shoppingList}
                setShoppingList={setShoppingList}
            />
        </div>
    );
}

export default App;
