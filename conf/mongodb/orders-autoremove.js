// Delete orders that are older than 100 days

db.orders.createIndex( 
    { "date" : 1 },
    { expireAfterSeconds : 60 * 60 * 24 * 100 }
);
