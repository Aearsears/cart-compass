import { useState, useEffect } from 'react';

function ShoppingList({ shoppingList, setShoppingList }) {
    const [quantities, setQuantities] = useState([]);

    useEffect(() => {
        const savedList = JSON.parse(localStorage.getItem('shoppingList'));
        if (savedList) {
            setShoppingList(savedList);
        }
    }, [setShoppingList]);

    useEffect(() => {
        localStorage.setItem('shoppingList', JSON.stringify(shoppingList));
    }, [shoppingList]);

    const handleQuantityChange = (index, quantity) => {
        const newQuantities = [...quantities];
        newQuantities[index] = quantity;
        setQuantities(newQuantities);
    };

    const removeFromList = (index) => {
        setShoppingList(shoppingList.filter((_, i) => i !== index));
        setQuantities(quantities.filter((_, i) => i !== index));
    };

    const totalCost = shoppingList.reduce((sum, item, index) => {
        const quantity = quantities[index] || 1; // Default to 1 if no quantity is set
        return sum + item.price * quantity;
    }, 0);

    return (
        <div>
            <h2 className="text-lg font-bold mt-6">Shopping List</h2>
            <ul>
                {shoppingList.map((item, index) => (
                    <li
                        key={index}
                        className="border p-2 my-2 flex justify-between">
                        <div>
                            {item.store}: ${item.price.toFixed(2)}
                        </div>
                        <div className="flex items-center">
                            <input
                                type="number"
                                value={quantities[index] || 1}
                                min="1"
                                onChange={(e) =>
                                    handleQuantityChange(index, e.target.value)
                                }
                                className="border px-2 py-1 w-16 text-center"
                            />
                            <button
                                className="bg-red-500 text-white px-2 py-1 ml-2"
                                onClick={() => removeFromList(index)}>
                                Remove
                            </button>
                        </div>
                    </li>
                ))}
            </ul>
            <h3 className="font-bold mt-4">Total: ${totalCost.toFixed(2)}</h3>
        </div>
    );
}

export default ShoppingList;
