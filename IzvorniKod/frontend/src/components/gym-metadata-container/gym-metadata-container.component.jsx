import React from 'react'
import './gym-metadata-container.styles.scss'
import { AiOutlineInfoCircle } from "react-icons/ai";
import {Link} from 'react-router-dom'

const GymMetaDataContainer = ({id, name, description, email}) => (
    <div className = 'gym-metadata-container'>
        <p className = 'gym-grid-content'>{name}</p>
        <p className = 'gym-grid-content'>{description}</p>
        <p className = 'gym-grid-content'>{email}</p>
        <Link className = 'gym-grid-content' to ={`/gymInfo/${id}`}><AiOutlineInfoCircle /></Link>
    </div>
)

export default GymMetaDataContainer
