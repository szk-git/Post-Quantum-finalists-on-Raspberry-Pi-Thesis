import React, {useContext} from 'react'
import AuthContext from '../context/AuthContext'

const LoginPage = () => {
    let {loginUser} = useContext(AuthContext)
    return (
        
        <div>
            <form onSubmit={loginUser}>
                <div class="form-group">
                    <label for="usernameInput">Email address</label>
                    <input type="text" class="form-control" id="usernameInput" name="username" aria-describedby="emailHelp" placeholder="Enter username"/>
                </div>
                <div class="form-group">
                    <label for="passwordInput">Password</label>
                    <input type="password" class="form-control" id="passwordInput" name="password" placeholder="Enter password"/>
                </div>
                <button type="submit" class="btn btn-primary">Submit</button>
            </form>
        </div>
    )
}

export default LoginPage
