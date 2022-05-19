import axios from "axios";

import {API_BASE_URL} from '../common/constants';

class AuthService {
  login(username, password) {
    return axios
      .post(API_BASE_URL + "/auth/login", {
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
    return axios.post(API_BASE_URL + "/auth/register", {
      username,
      firstName,
      lastName,
      email,
      password
    })
  }

  runAlgorithm(type, token){
    const headers = {
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${token}`
    }

    const value = {
      type: type
    }

    const defHeads = {headers: headers};
    const URL = API_BASE_URL + "/run?type=" + type;

    return axios.post(URL, value, defHeads).catch((error) => {
        if( error.response ){
            console.log(error.response.data); // => the response payload 
        }
    });
  }

  getFileList(type, token){
    const headers = {
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${token}`
    }

    const value = {
      type: type
    }

    const defHeads = {headers: headers};
    const URL = API_BASE_URL + "/files?type=" + type;
    const options = Object.assign(value, defHeads);
    return fetch(URL, options)
      .then(response =>
        response.json().then(json => {
          return json;
      })
    ); 
  }

  getSpecificFile(URL, fileName, token){
    const headers = {
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${token}`
    }

    const FileDownload = require('js-file-download');

    return axios.get(URL, {
      responseType: 'blob',
      headers: headers
    }).then((response) => {
        FileDownload(response.data, fileName);
    });
  }

  getCurrentUser() {
    return JSON.parse(localStorage.getItem('user'));
  }

  logout() {
    localStorage.removeItem("user");
  }
}

export default new AuthService();
