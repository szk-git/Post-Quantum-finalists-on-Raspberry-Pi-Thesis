import React, { Component } from "react";
import { Redirect } from "react-router-dom";
import AuthService from "../../services/AuthService";

export default class Profile extends Component {
  constructor(props) {
    super(props);

    this.state = {
      redirect: null,
      userReady: false,
      currentUser: null,
      algorithmValue: 'AES',
      getListValue: 'AES',
      listValues: []
    };
    this.handleSubmitChange = this.handleSubmitChange.bind(this);
    this.handleGetListChange = this.handleGetListChange.bind(this);
    this.handleSubmit = this.handleSubmit.bind(this);
    this.handleGet = this.handleGet.bind(this);
    this.handleGetFile = this.handleGetFile.bind(this);
  }

  handleSubmitChange(event) {
    this.setState({
      algorithmValue: event.target.value
    });
  }

  handleGetListChange(event) {
    this.setState({
      getListValue: event.target.value
    });
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

  handleGet(event) {
    event.preventDefault();
    AuthService.getFileList(
      this.state.getListValue,
      this.state.currentUser.accessToken
    ).then(response => {
      this.setState({
        listValues: response
      });
    }
    );

    console.log(this.state.listValues)
  }

  handleGetFile(URL, fileName) {
    AuthService.getSpecificFile(
      URL,
      fileName,
      this.state.currentUser.accessToken
    );
  }

  componentDidMount() {
    const currentUser = AuthService.getCurrentUser();
    if (!currentUser) {
      this.setState({
        redirect: "/home"
      });
    } else {
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

    const currentListValues = this.state.listValues;
    const DATE_OPTIONS = { year: 'numeric', month: 'short', day: 'numeric', hour: '2-digit', minute: '2-digit' };

    const millisToMinutesAndSeconds = (millis) => {
      var minutes = Math.floor(millis / 60000);
      var seconds = ((millis % 60000) / 1000).toFixed(0);
      //ES6 interpolated literals/template literals 
      //If seconds is less than 10 put a zero in front.
      return `${minutes}:${(seconds < 10 ? "0" : "")}${seconds}`;
    }

    return (
      <div className="container">
        <form onSubmit={this.handleSubmit}>
          <label className="text-muted">With 'Submit' button you can trigger the server to run a specific algorithm</label>
          <div className="input-group">
            <select className="custom-select" value={this.state.value} onChange={this.handleSubmitChange}>
              <option value="AES">AES256</option>
              <option value="Frodo">Frodo</option>
              <option value="Kyber512">Kyber512</option>
              <option value="Kyber512_90s">Kyber512-90s</option>
              <option value="Kyber768">Kyber768</option>
              <option value="Kyber768_90s">Kyber768-90s</option>
              <option value="Kyber1024">Kyber1024</option>
              <option value="Kyber1024_90s">Kyber1024-90s</option>
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

        <form onSubmit={this.handleGet}>
          <label className="text-muted">With 'Submit' button you can trigger the server to get all algorithm with a specific algorithm</label>
          <div className="input-group">
            <select className="custom-select" value={this.state.value} onChange={this.handleGetListChange}>
              <option value="AES">AES256</option>
              <option value="Frodo">Frodo</option>
              <option value="NTRU">NTRU</option>
              <option value="NTRU701">NTRU701</option>
              <option value="NTRU4096">NTRU4096</option>
              <option value="NTRU2048v1">NTRU2048v1</option>
              <option value="NTRU2048v2">NTRU2048v2</option>
              <option value="Kyber">Kyber</option>
              <option value="Kyber512">Kyber512</option>
              <option value="Kyber512_90s">Kyber512-90s</option>
              <option value="Kyber768">Kyber768</option>
              <option value="Kyber768_90s">Kyber768-90s</option>
              <option value="Kyber1024">Kyber1024</option>
              <option value="Kyber1024_90s">Kyber1024-90s</option>
            </select>
            <div className="input-group-append">
              <input className="btn btn-info" type="submit" value="Submit" />
            </div>
          </div>
        </form>

        <table className="table">
          <thead>
            <tr>
              <th scope="col">Algorithm</th>
              <th scope="col">File name</th>
              <th scope="col">Created</th>
              <th scope="col">Running time</th>
              <th scope="col">#</th>
            </tr>
          </thead>
          <tbody>
            {currentListValues.map((item, i) => (
              <tr key={i}>
                <th>{item.algorithm}</th>
                <td>{item.name}</td>
                <td>{(new Date(item.creationTime)).toLocaleDateString('en-US', DATE_OPTIONS)}</td>
                <td>{millisToMinutesAndSeconds(item.time)}</td>
                <td>
                  <button onClick={() => this.handleGetFile(item.url, item.name)} type="button" class="btn btn-warning">Download</button>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    );
  }
}
