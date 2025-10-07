const mysql = require('mysql2/promise');

const pool = mysql.createPool({
    host: 'localhost',
    user: 'root',
    password: 'Lmlyraa200kh!',
    database: 'todo',
    port: 3306,
    waitForConnections: true
})

module.exports = pool;