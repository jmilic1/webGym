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
        fetch('https://web-gym2.herokuapp.com/gymList', {
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
                 {this.state.gymList.map(gym => 
                            <GymMetaDataContainer name = {gym.name} key = {gym.name}/>
                        )}
            </div>
        )
    }
}

export default GymList
