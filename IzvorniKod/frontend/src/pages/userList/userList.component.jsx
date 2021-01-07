import React from 'react'
import './userList.styles.scss'
import UserMetaDataContainer from '../../components/user-metadata-container/user-metadata-container.component'

class UserList extends React.Component{
    constructor(){
        super()
        this.state = {
            userList: []
        }
    }

    componentDidMount(){
        fetch(this.props.backendURL + 'userList', {
            method: 'GET',
            credentials: "include"
        }).then(response => {
            return response.json()
        }).then(userList => {
            this.setState({
                userList: userList
            })
        })
    }

    render() {
        return(

            <div className = 'userList-container'>
                <div className = 'userList-header-container'>
                    <h3>Ime korisnika</h3>
                    <h3>Prezime korisnika</h3>
                    <h3>Email</h3>
                    <h3>Korisničko ime</h3>
                    <h3>Uloga</h3>
                </div>
                {this.state.userList.map(user =>
                    <UserMetaDataContainer name = {user.name} surname = {user.surname} email = {user.email} username = {user.username} role = {user.role}/>
                )}
            </div>
        )
    }
}

export default UserList
