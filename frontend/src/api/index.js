import axios from 'axios'

const api = axios.create({
  baseURL: '/api',
  timeout: 10000,
  headers: { 'Content-Type': 'application/json' }
})

export const login = (data) => api.post('/users/login', data)
export const register = (data) => api.post('/users/register', data)

export const getProducts = () => api.get('/products')

export const getLikeList = (userId) => api.get('/likes', { params: { userId } })
export const addLike = (data) => api.post('/likes', data)
export const updateLike = (sn, data, viewerUserId) =>
    api.put(`/likes/${sn}?viewerUserId=${viewerUserId}`, data)
export const deleteLike = (sn, viewerUserId) =>
    api.delete(`/likes/${sn}?viewerUserId=${viewerUserId}`)

export default api
