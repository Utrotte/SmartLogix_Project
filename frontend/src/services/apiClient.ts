import axios from 'axios'

const API_BFF_URL = (import.meta.env.VITE_API_BFF_URL as string) || 'http://localhost:8080'

const apiClient = axios.create({
  baseURL: API_BFF_URL,
  timeout: 10000,
  headers: {
    'Content-Type': 'application/json',
  },
})

// Interceptor para agregar token a cada request
apiClient.interceptors.request.use((config) => {
  const token = localStorage.getItem('sessionToken')
  if (token) {
    config.headers['X-Session-Token'] = token
  }
  return config
})

// Interceptor para manejar errores (especialmente 401)
apiClient.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response?.status === 401) {
      // Token expirado o inválido
      localStorage.removeItem('sessionToken')
      localStorage.removeItem('usuario')
      window.location.href = '/login'
    }
    return Promise.reject(error)
  }
)

export default apiClient
