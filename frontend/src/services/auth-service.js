import axios from "axios";

import {API_BASE_URL, ACCESS_TOKEN} from '../common/constants';

const request = (options) => {
  const headers = new Headers({
      'Content-Type': 'application/json',
  })

  if(localStorage.getItem(ACCESS_TOKEN)) {
      headers.append('Authorization', 'Bearer ' + localStorage.getItem(ACCESS_TOKEN))
  }

  const defaults = {headers: headers};
  options = Object.assign({}, defaults, options);

  return fetch(options.url, options)
  .then(response =>
      response.json().then(json => {
          if(!response.ok) {
            return Promise.reject(json);
          }
          return json;
      })
  ); 
};

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
    const inputData = Object.assign({}, defHeads, value);

    const URL = API_BASE_URL + "/run?type=" + type;

    console.log(inputData)

    return axios.post(URL, value, defHeads).catch((error) => {
        if( error.response ){
            console.log(error.response.data); // => the response payload 
        }
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
