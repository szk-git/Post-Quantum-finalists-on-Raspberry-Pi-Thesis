import axios from "axios";

import {API_BASE_URL} from '../common/constants';

class AuthService {
  login(username, password) {
    return axios
      .post(API_BASE_URL + "/api/v1/auth/login", {
        username,
        password
      })
      .then(response => {
        if (response.data.accessToken) {
          localStorage.setItem("user", JSON.stringify(response.data));
        }

        return response.data;
      });
  }

  register(username, firstName, lastName, email, password) {
    return axios.post(API_BASE_URL + "/api/v1/auth/register", {
      username,
      firstName,
      lastName,
      email,
      password
    })
  }

  getCurrentUser() {
    return JSON.parse(localStorage.getItem('user'));
  }

  logout() {
    localStorage.removeItem("user");
  }
}

export default new AuthService();
