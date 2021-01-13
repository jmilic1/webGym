import React from 'react'
import './homepage.styles.scss'
import homePagePicture from '../../assets/silhouette-1975689_1280.svg'

class Homepage extends React.Component{

    render(){
        return(
            <div className = 'homepage-container'>
                <img src={homePagePicture} height='300px'/>
            </div>
        )
    }
}

export default Homepage