import React from 'react'
import './coachClients.styles.css'
import UserInfoForCoach from "../../components/userInfoForCoach/userInfoForCoach.component";

class CoachClients extends React.Component{
    constructor(){
        super()
        this.state = {
            clients: []
        }
    }

    componentDidMount(){
        fetch(this.props.backendURL + 'myClients', {
            method: 'GET',
            credentials: "include"
        }).then(response => {
            console.log(response.status)
            return response.json()
        }).then(clients => {
            this.setState({
                clients: clients
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
                </div>
                {this.state.clients.map(client =>
                    <UserInfoForCoach name = {client.name} surname = {client.surname} email = {client.email} username = {client.username}/>
                )}
            </div>
        )
    }
}

export default CoachClients
