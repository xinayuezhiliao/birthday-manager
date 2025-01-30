import axios from 'axios';

const API_BASE_URL = 'http://localhost:8080/api';

const api = axios.create({
  baseURL: API_BASE_URL,
  timeout: 10000,
  headers: {
    'Content-Type': 'application/json'
  }
});

// Birthday API
export const birthdayApi = {
  getAllBirthdays() {
    return api.get('/birthdays');
  },
  
  getBirthdayById(id) {
    return api.get(`/birthdays/${id}`);
  },
  
  createBirthday(birthday) {
    return api.post('/birthdays', birthday);
  },
  
  updateBirthday(id, birthday) {
    return api.put(`/birthdays/${id}`, birthday);
  },
  
  deleteBirthday(id) {
    return api.delete(`/birthdays/${id}`);
  },
  
  searchBirthdays(params) {
    return api.get('/birthdays/search', { params });
  },
  
  getUpcomingBirthdays(days = 30) {
    return api.get(`/birthdays/upcoming?days=${days}`);
  }
};

// Contact API
export const contactApi = {
  getAllContacts() {
    return api.get('/contacts');
  },
  
  getContactById(id) {
    return api.get(`/contacts/${id}`);
  },
  
  createContact(contact) {
    return api.post('/contacts', contact);
  },
  
  updateContact(id, contact) {
    return api.put(`/contacts/${id}`, contact);
  },
  
  deleteContact(id) {
    return api.delete(`/contacts/${id}`);
  },
  
  searchContacts(params) {
    return api.get('/contacts/search', { params });
  }
};

export default api;
