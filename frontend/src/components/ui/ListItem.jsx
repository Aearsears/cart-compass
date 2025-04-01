import React from 'react';
import { Card } from './card'; // Import Card from ShadCN
import { Button } from './button'; // Import Button from ShadCN

export function ListItem({ item, onAdd }) {
  return (
    <Card className="flex justify-between items-center p-3">
      <span>{item.store}: ${item.price.toFixed(2)}</span>
      <Button variant="default" onClick={() => onAdd(item)}>Add to List</Button>
    </Card>
  );
}
