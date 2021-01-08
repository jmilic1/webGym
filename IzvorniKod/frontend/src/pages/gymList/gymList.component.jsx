import React from 'react'
import './gymList.styles.scss'
import GymMetaDataContainer from '../../components/gym-metadata-container/gym-metadata-container.component'

class GymList extends React.Component{
    constructor(){
        super()
        this.state = {
            gymList: []
        }
    }

    componentDidMount(){
        fetch(this.props.backendURL + 'gymList', {
            method: 'GET',
            credentials: "include"
        }).then(response => {
            return response.json()
        }).then(gymList => {
            this.setState({
                gymList: gymList
            })
        })
    }

    render() {
        return(

            <div className = 'gymList-container'>
                <div className = 'gymList-header-container'>
                    <h3>Ime teretane</h3>
                    <h3>Opis</h3>
                    <h3>E-mail</h3>
                    <h3>Informacije</h3>
                </div>
                {this.state.gymList.map(gym =>
                    <GymMetaDataContainer id = {gym.id} name = {gym.name} description = {gym.description} email = {gym.email} key = {gym.id}/>
                )}
            </div>
        )
    }
}

export default GymList
