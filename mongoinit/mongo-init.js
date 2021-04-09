db.createUser(
    {
        user: "user",
        pwd: "user",
        roles: [
            {
                role: "dbOwner",
                db: "botdb"
            }
        ]
    }
);