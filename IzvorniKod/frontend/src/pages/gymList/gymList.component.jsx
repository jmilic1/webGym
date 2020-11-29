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
        fetch(this.props.backendURL, {
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

    render(){
        return(

            <div className = 'gymList-container'>
                <div className = 'gymList-header-container'>
                    <h3>Ime teretane</h3>
                    <h3>Grad</h3>
                    <h3>Informacije</h3>
                </div>
                {this.state.gymList.map(gym =>
                    <GymMetaDataContainer name = {gym.name} city = {gym.city} key = {gym.id}/>
                )}
            </div>
        )
    }
}

export default GymList
