const userService = require('../services/user.service')

const getAllUsers = async (req, res) => {
    try{
        const allUsers = await userService.getAllUsers();
        res.status(200).json({
            success: true,
            count: allUsers.length,
            data: allUsers
        });
    } catch (error) {
        res.status(500).json({
            success: false,
            message: 'Error during get all users',
            error: error.message
        })
    }
}

module.exports = {
   getAllUsers,
}