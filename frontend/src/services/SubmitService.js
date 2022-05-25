import React, { Component } from "react";
import axios from "axios";
import { API_BASE_URL } from '../common/constants';

export default class SubmitService extends Component {
    constructor(props) {
        super(props);

        this.state = {
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${props.token}`
            },
            algorithmValue: 'AES'
        };
        this.handleSubmitChange = this.handleSubmitChange.bind(this);
        this.handleSubmit = this.handleSubmit.bind(this);
        this.runAlgorithm = this.runAlgorithm.bind(this);
    }

    handleSubmitChange(event) {
        this.setState({
            algorithmValue: event.target.value
        });
    }

    handleSubmit(event) {
        event.preventDefault();
        this.setState({
            message: "",
        });

        this.runAlgorithm(
            this.state.algorithmValue
        )
    }

    runAlgorithm(type) {
        const value = {
            type: type
        }

        const defHeads = { headers: this.state.headers };
        const URL = API_BASE_URL + "/api/v1/run?type=" + type;

        return axios.post(URL, value, defHeads).catch((error) => {
            if (error.response) {
                console.log(error.response.data);
            }
        });
    }

    render() {
        return (
            <div className="container">
                <form onSubmit={this.handleSubmit}>
                    <label className="text-muted">With 'Submit' button you can trigger the server to run a specific algorithm</label>
                    <div className="input-group">
                        <select className="custom-select" value={this.state.value} onChange={this.handleSubmitChange}>
                            <option value="AES">AES256</option>
                            <option value="FrodoKEM_640">FrodoKEM-640</option>
                            <option value="FrodoKEM_976">FrodoKEM-976</option>
                            <option value="FrodoKEM_1344">FrodoKEM-1344</option>
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
            </div>
        );
    }
}
