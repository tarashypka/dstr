// Document that should be updated on each new document insert

db.orders.insert( 
        {
            "date" : new Date(), 
            "nOrders" : NumberInt(0)
        }
);
