import React, { Component } from "react";
import { Redirect } from "react-router-dom";
import AuthService from "../../services/auth-service";

export default class Profile extends Component {
  constructor(props) {
    super(props);

    this.state = {
      redirect: null,
      userReady: false,
      currentUser: null,
      algorithmValue: 'AES256'
    };
    this.handleChange = this.handleChange.bind(this);
    this.handleSubmit = this.handleSubmit.bind(this);
  }

  handleChange(event) {    
    this.setState({algorithmValue: event.target.value});  
  }

  handleSubmit(event) {
    event.preventDefault();
    this.setState({
      message: "",
    });

    AuthService.runAlgorithm(
        this.state.algorithmValue,
        this.state.currentUser.accessToken
      ).then(() => {
        //window.location.reload();
      }, error => {
        console.log(error.response.status);
      }
    );
  }

  componentDidMount() {
    const currentUser = AuthService.getCurrentUser();

    if (!currentUser){
      this.setState({
        redirect: "/home"
      });
    }else{
      this.setState({
        currentUser: currentUser,
        userReady: true
      });
    }
  }

  render() {
    if (this.state.redirect) {
      return <Redirect to={this.state.redirect} />
    }

    const currentUser = this.state.currentUser;

    return (
      <div className="container">
      <form onSubmit={this.handleSubmit}>
        <label className="text-muted">With 'Submit' button you can trigger the server to run a specific algorithm</label>
        <div className="input-group">
          <select className="custom-select" value={this.state.value} onChange={this.handleChange}>
            <option value="AES256">AES256</option>
            <option value="Frodo">Frodo</option>
            <option value="Kyber512">Kyber512</option>
            <option value="Kyber512-90s">Kyber512-90s</option>
            <option value="Kyber768">Kyber768</option>
            <option value="Kyber768-90s">Kyber768-90s</option>
            <option value="Kyber1024">Kyber1024</option>
            <option value="Kyber1024-90s">Kyber1024-90s</option>
            <option value="NTRU701">NTRU701</option>
            <option value="NTRU4096">NTRU4096</option>
            <option value="NTRU2048v1">NTRU2048v1</option>
            <option value="NTRU2048v2">NTRU2048v2</option>
          </select>
          <div className="input-group-append">
            <input className="btn btn-success" type="submit" value="Submit" />
          </div>
        </div>
      </form>
       

        {(this.state.userReady) ?
        <div>
        <header className="jumbotron">
          <h3>
            <strong>{currentUser.username}</strong> Profile
          </h3>
        </header>
        <p>
          <strong>Token:</strong>{" "}
          {currentUser.accessToken}
        </p>
        <p>
          <strong>Id:</strong>{" "}
          {currentUser.id}
        </p>
        <p>
          <strong>Email:</strong>{" "}
          {currentUser.email}
        </p>
        <strong>Authorities:</strong>
        <ul>
          {currentUser.roles &&
            currentUser.roles.map((role, index) => <li key={index}>{role}</li>
          )}
        </ul>
      </div>: null}
      </div>
    );
  }
}
