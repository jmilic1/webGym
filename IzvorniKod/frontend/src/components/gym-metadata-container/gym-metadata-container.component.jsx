import React from 'react'
import './gym-metadata-container.styles.scss'
import { AiOutlineInfoCircle } from "react-icons/ai";
import {Link} from 'react-router-dom'

const GymMetaDataContainer = ({name, city}) => (
    <div className = 'gym-metadata-container'>
        <p className = 'gym-grid-content'>{name}</p>
        <p className = 'gym-grid-content'>{city}</p>
        <Link className = 'gym-grid-content' to = '/gymList'><AiOutlineInfoCircle /></Link>
    </div>
)

export default GymMetaDataContainer
