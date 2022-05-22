import React, { Component } from "react";
import { Redirect } from "react-router-dom";
import FilesService from "../../services/FilesService";

export default class Profile extends Component {
    constructor(props) {
        super(props);

        this.state = {
            currentUser: JSON.parse(localStorage.getItem('user'))
        };
    }


    render() {
        if (!this.state.currentUser) {
            return <Redirect to={this.state.redirect} />
        }
        return (
            <div className="container">
                <FilesService token={this.state.currentUser.accessToken} />
            </div>
        );
    }
}
