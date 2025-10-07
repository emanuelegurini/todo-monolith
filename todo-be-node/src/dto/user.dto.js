const userResponseDTO = (user) => {
    return {
        id: user.id,
        name: user.name,
        surname: user.surname,
        email: user.email
    }
}

const userResponseArrayDTO = (users) => {
   return users.map(user => {
       userResponseDTO(user)
   })
}

module.exports = {
    userResponseDTO,
    userResponseArrayDTO
}