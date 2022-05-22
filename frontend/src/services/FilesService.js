import React, { Component } from "react";
import axios from "axios";
import { API_BASE_URL } from '../common/constants';

export default class FileService extends Component {
    constructor(props) {
        super(props);

        this.state = {
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${props.token}`
            },
            getListValue: 'AES',
            listValues: []
        };
        this.handleGetListChange = this.handleGetListChange.bind(this);
        this.getSpecificFile = this.getSpecificFile.bind(this);
        this.getFileList = this.getFileList.bind(this);
    }

    handleGetListChange(event) {
        this.setState({
            getListValue: event.target.value
        });
    }

    getFileList(event) {
        event.preventDefault();

        const value = {
            type: this.state.getListValue
        }

        const defHeads = { headers: this.state.headers };
        const URL = API_BASE_URL + "/files?type=" + this.state.getListValue;
        const options = Object.assign(value, defHeads);
        return fetch(URL, options)
            .then(response =>
                response.json().then(json => {
                    this.setState({
                        listValues: json
                    });
                })
            );
    }

    getSpecificFile(URL, fileName) {
        const FileDownload = require('js-file-download');

        return axios.get(URL, {
            responseType: 'blob',
            headers: this.state.headers
        }).then((response) => {
            FileDownload(response.data, fileName);
        });
    }



    render() {
        const currentListValues = this.state.listValues;
        const DATE_OPTIONS = { year: 'numeric', month: 'short', day: 'numeric', hour: '2-digit', minute: '2-digit' };

        const millisToMinutesAndSeconds = (millis) => {
            var minutes = Math.floor(millis / 60000);
            var seconds = ((millis % 60000) / 1000).toFixed(0);
            return `${minutes}:${(seconds < 10 ? "0" : "")}${seconds}`;
        }
        
        return (
            <div className="container">
                <form onSubmit={this.getFileList}>
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
                                    <button onClick={() => this.getSpecificFile(item.url, item.name)} type="button" className="btn btn-warning">Download</button>
                                </td>
                            </tr>
                        ))}
                    </tbody>
                </table>
            </div>
        );
    }
}
