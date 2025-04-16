import axios from 'axios';

// Create axios instance with default configuration
const api = axios.create({
  baseURL: '/api',
  withCredentials: true,
  headers: {
    'Content-Type': 'application/json'
  }
});

// Add interceptor for handling errors
api.interceptors.response.use(
  response => response.data,
  error => {
    console.error('API Error:', error.response);
    return Promise.reject(error.response?.data || error);
  }
);

// Auth API
export const apiLogin = credentials => api.post('/auth/login', credentials);
export const apiLogout = () => api.post('/auth/logout');
export const apiCheckAuth = () => api.get('/auth/me');
export const apiRegister = userData => api.post('/auth/register', userData);

// Channels API
export const apiGetChannels = () => api.get('/channels');
export const apiGetChannel = channelId => api.get(`/channels/${channelId}`);
export const apiCreateChannel = channelData => api.post('/channels', channelData);
export const apiUpdateChannel = (channelId, data) => api.put(`/channels/${channelId}`, data);
export const apiDeleteChannel = channelId => api.delete(`/channels/${channelId}`);
export const apiJoinChannel = channelId => api.post(`/channels/${channelId}/join`);
export const apiLeaveChannel = channelId => api.post(`/channels/${channelId}/leave`);

// Messages API
export const apiGetMessages = channelId => api.get(`/channels/${channelId}/messages`);
export const apiSendMessage = (channelId, content) => api.post(`/channels/${channelId}/messages`, { content });
export const apiEditMessage = (messageId, content) => api.put(`/messages/${messageId}`, { content });
export const apiDeleteMessage = messageId => api.delete(`/messages/${messageId}`);

// Users API
export const apiGetUsers = () => api.get('/users');
export const apiGetUser = userId => api.get(`/users/${userId}`);
export const apiUpdateUser = (userId, data) => api.put(`/users/${userId}`, data);
export const apiSearchUsers = query => api.get(`/users/search?q=${query}`);

// Direct Messages API
export const apiGetDirectMessages = userId => api.get(`/direct-messages/${userId}`);
export const apiSendDirectMessage = (userId, content) => api.post(`/direct-messages/${userId}`, { content });

export default api; 