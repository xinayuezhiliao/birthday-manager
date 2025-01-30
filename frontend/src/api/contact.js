import axios from 'axios'

const api = axios.create({
  baseURL: 'http://localhost:8080/api'
})

export const contactApi = {
  // 获取所有联系人
  getAllContacts() {
    return api.get('/contacts')
  },

  // 获取即将到来的生日
  getUpcomingBirthdays() {
    return api.get('/contacts/upcoming')
  },

  // 获取一周内的生日
  getWeekBirthdays() {
    return api.get('/contacts/week')
  },

  // 获取单个联系人
  getContact(id) {
    return api.get(`/contacts/${id}`)
  },

  // 创建新联系人
  createContact(contact) {
    return api.post('/contacts', contact)
  },

  // 更新联系人
  updateContact(id, contact) {
    return api.put(`/contacts/${id}`, contact)
  },

  // 删除联系人
  deleteContact(id) {
    return api.delete(`/contacts/${id}`)
  }
}
