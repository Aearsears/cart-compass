import { useState, useEffect } from 'react';
import { getPrices } from './api/grocery';
import ShoppingList from './components/ShoppingList';

function App() {
    const [query, setQuery] = useState('');
    const [prices, setPrices] = useState([]);
    const [shoppingList, setShoppingList] = useState([]);

    const searchPrices = async () => {
        const data = await getPrices(query);
        setPrices(data);
    };

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
            <button
                className="bg-blue-500 text-white px-4 py-2 ml-2"
                onClick={searchPrices}>
                Search
            </button>
            <ul className="mt-4">
                {prices.map((item, index) => (
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

            <ShoppingList
                shoppingList={shoppingList}
                setShoppingList={setShoppingList}
            />
        </div>
    );
}

export default App;
