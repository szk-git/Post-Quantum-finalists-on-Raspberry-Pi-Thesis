import React, { Component } from "react";
import Form from "react-validation/build/form";
import Input from "react-validation/build/input";
import CheckButton from "react-validation/build/button";

import AuthService from "../../services/AuthService";

import { isRequired } from "../../common/validator"
export default class Login extends Component {
  constructor(props) {
    super(props);

    this.state = {
      username: "",
      password: "",
      loading: false,
      message: ""
    };
  }

  onChangeUsername = (e) => {
    this.setState({
      username: e.target.value
    });
  }

  onChangePassword = (e) => {
    this.setState({
      password: e.target.value
    });
  }



  handleLogin = (e) => {
    e.preventDefault();

    this.setState({
      message: "",
      loading: true
    });

    this.form.validateAll();

    if (this.checkBtn.context._errors.length === 0) {
      AuthService.login(
          this.state.username,
          this.state.password
        ).then(() => {
          this.props.history.push("/profile");
          window.location.reload();
        }, error => {
          if (typeof(error.response) !== 'undefined') {
            const resMessage = error.response.data.message;
            this.setState({
              message: resMessage
            });
          }else{
            this.setState({
              message: "The server can't response :("
            });
          }
        }
      );
    } else {
      this.setState({
        loading: false
      });
    }
  }

  render() {

    return (
      <div className="container py-5 h-100">
        <div className="row d-flex justify-content-center align-items-center h-100">
          <div className="col-12 col-md-8 col-lg-6 col-xl-5">
            <div className="card-body p-5 text-center">
              <Form onSubmit={this.handleLogin} ref={c => { this.form = c; }} >
                <div className="form-outline mb-4">
                  <label htmlFor="username">Username</label>
                  <Input type="text" className="form-control" name="username" value={this.state.username} onChange={this.onChangeUsername} validations={[isRequired]} />
                </div>

                <div className="form-outline mb-4">
                  <label htmlFor="password">Password</label>
                  <Input type="password" className="form-control" name="password" value={this.state.password} onChange={this.onChangePassword} validations={[isRequired]} />
                </div>

                <div className="form-outline mb-4">
                  <button className="btn btn-success btn-block" disabled={this.state.loading}>
                    {this.state.loading && (
                      <span className="spinner-border spinner-border-sm"></span>
                    )}
                    <span>Login</span>
                  </button>
                </div>

                {this.state.message && (
                  <div className="form-group">
                    <div className={this.state.successful ? "alert alert-success" : "alert alert-danger"} role="alert">
                      {this.state.message}
                    </div>
                  </div>
                )}
                <CheckButton style={{ display: "none" }} ref={c => { this.checkBtn = c; }} />
              </Form>
            </div>
          </div>
        </div>
      </div>
    );
  }
}