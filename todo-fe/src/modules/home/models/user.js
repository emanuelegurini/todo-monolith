
import {fetchUser} from "../lib/api.js";
/*
* This functions return a clean version of the user
*/
export const getUser = async (id) => {
    return await fetchUser(id)
}