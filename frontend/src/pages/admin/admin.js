import React, { Component } from "react";
import { Redirect } from "react-router-dom";
import FilesService from "../../services/FilesService";
import SubmitService from "../../services/SubmitService";

export default class Admin extends Component {
    constructor(props) {
        super(props);

        this.state = {
            currentUser: JSON.parse(localStorage.getItem('user')),
        };
    }


    render() {
        if (!this.state.currentUser) {
            return <Redirect to={this.state.redirect} />
        }
        return (
            <div className="container">
                <SubmitService token={this.state.currentUser.accessToken} />
                <FilesService token={this.state.currentUser.accessToken} />
            </div>
        );
    }
}
