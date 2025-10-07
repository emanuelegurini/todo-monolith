const userRepository = require('../repositories/user.repository')
const userDto = require('../dto/user.dto')

const getAllUsers = async () => {
    const users = await userRepository.findAll()
    console.log(userDto.userResponseArrayDTO(users))
    return userDto.userResponseArrayDTO(users)
}

const getUserById = async (id) => {
    const user = await userRepository.findById(id)
    return userDto.userResponseDTO(user)
}

module.exports = {
    getAllUsers,
    getUserById
}