// 生成随机的联系人数据
const mockContacts = [
  {
    id: 1,
    name: '张三',
    relationship: '家人',
    birthday: '1990-02-01',
    daysUntil: 2,
    avatar: 'https://placekitten.com/100/100',
    notes: '最爱吃妈妈做的红烧肉'
  },
  {
    id: 2,
    name: '李四',
    relationship: '朋友',
    birthday: '1992-02-05',
    daysUntil: 6,
    avatar: 'https://placekitten.com/101/101',
    notes: '高中同学，在北京工作'
  },
  {
    id: 3,
    name: '王五',
    relationship: '同事',
    birthday: '1988-02-10',
    daysUntil: 11,
    avatar: 'https://placekitten.com/102/102',
    notes: '项目组长，喜欢打篮球'
  },
  {
    id: 4,
    name: '赵六',
    relationship: '家人',
    birthday: '1995-03-15',
    daysUntil: 44,
    avatar: 'https://placekitten.com/103/103',
    notes: '表弟，在读研究生'
  },
  {
    id: 5,
    name: '小红',
    relationship: '朋友',
    birthday: '1993-04-20',
    daysUntil: 80,
    avatar: 'https://placekitten.com/104/104',
    notes: '大学室友，现在在上海'
  }
]

export const mockApi = {
  // 获取所有联系人
  getAllContacts() {
    return Promise.resolve(mockContacts)
  },

  // 获取即将到来的生日（7天内）
  getUpcomingBirthdays() {
    return Promise.resolve(mockContacts.filter(c => c.daysUntil <= 7))
  },

  // 获取本月生日
  getMonthBirthdays(month) {
    return Promise.resolve(mockContacts.filter(c => {
      const birthMonth = new Date(c.birthday).getMonth() + 1
      return birthMonth === month
    }))
  },

  // 获取单个联系人
  getContact(id) {
    const contact = mockContacts.find(c => c.id === id)
    return Promise.resolve(contact)
  },

  // 创建新联系人
  createContact(contact) {
    const newContact = {
      ...contact,
      id: mockContacts.length + 1,
      daysUntil: Math.floor(Math.random() * 365)
    }
    mockContacts.push(newContact)
    return Promise.resolve(newContact)
  },

  // 更新联系人
  updateContact(id, contact) {
    const index = mockContacts.findIndex(c => c.id === id)
    if (index !== -1) {
      mockContacts[index] = { ...mockContacts[index], ...contact }
      return Promise.resolve(mockContacts[index])
    }
    return Promise.reject(new Error('Contact not found'))
  },

  // 删除联系人
  deleteContact(id) {
    const index = mockContacts.findIndex(c => c.id === id)
    if (index !== -1) {
      mockContacts.splice(index, 1)
      return Promise.resolve()
    }
    return Promise.reject(new Error('Contact not found'))
  }
}
