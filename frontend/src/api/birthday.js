import axios from 'axios';

const API_PATH = '/api/birthdays';

export const birthdayApi = {
    // 获取所有生日信息
    getAllBirthdays() {
        return axios.get(API_PATH);
    },

    // 获取单个生日信息
    getBirthday(id) {
        return axios.get(`${API_PATH}/${id}`);
    },

    // 创建生日信息
    createBirthday(birthday) {
        return axios.post(API_PATH, birthday);
    },

    // 更新生日信息
    updateBirthday(id, birthday) {
        return axios.put(`${API_PATH}/${id}`, birthday);
    },

    // 删除生日信息
    deleteBirthday(id) {
        return axios.delete(`${API_PATH}/${id}`);
    },

    // 搜索生日信息
    searchBirthdays(params) {
        return axios.get(`${API_PATH}/search`, { params });
    },

    // 获取即将到来的生日
    getUpcomingBirthdays(days = 30) {
        return axios.get(`${API_PATH}/upcoming`, { params: { days } });
    }
};
