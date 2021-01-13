import React from 'react'
import './gym-metadata-coachPage.styles.css'
import { AiOutlineInfoCircle } from "react-icons/ai";
import {Link} from 'react-router-dom'

const GymMetaDataContainerCoachPage = ({id, name, description, email}) => (
    <div className = 'gym-metadata-container-coachPage'>
        <p className = 'gym-grid-content-coachPage'>{name}</p>
        <Link className = 'gym-grid-content-coachPage' to ={`/gymInfo/${id}`}><AiOutlineInfoCircle /></Link>
    </div>
)

export default GymMetaDataContainerCoachPage
