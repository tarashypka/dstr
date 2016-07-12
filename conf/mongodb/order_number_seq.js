// Add sequence object to auto-increment order number on each insert

db.orders.insert(
    { _id : "orderNumber", seq : NumberLong(0) }
);