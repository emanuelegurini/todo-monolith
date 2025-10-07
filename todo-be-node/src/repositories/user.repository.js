const db = require('../config/database')

// findAll
const findAll = async () => {
    const query = 'SELECT * FROM users WHERE 1=1'
    const [row] = await db.query(query)
    return row
}

// findById
const findById = async (id) => {
    const [row] = await db.query('SELECT * FROM users WHERE id = ?', [id])
    return row
}
// createUser
// deleteUser
const deleteUser = async (id) => {
    const [result] = await db.query(
        'DELETE FROM users WHERE id = ?',
        [id]
    )
    return result.affectedRows > 0
}

module.exports = {
    findAll,
    findById,
    deleteUser
}