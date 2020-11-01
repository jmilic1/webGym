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
        fetch('https://a3f8f382-ec4c-455f-817b-45bef89e1ceb.mock.pstmn.io', {
            method: 'GET'
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