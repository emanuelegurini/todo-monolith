/*
* This function return the token from the server 
*/
export const fetchToken = async (email, password) => {
	const response = await fetch(`${import.meta.env.VITE_BASE_URL}${import.meta.env.VITE_API_VERSION}/auth/login`, {
		method: "POST",
		headers: {
			"Content-Type": "application/json",
		},
		body: JSON.stringify({ email, password }),
	})
	if (!response.ok) {
		throw new Error(`HTTP Error: ${response.status}`)
	}
	return await response.json()
}