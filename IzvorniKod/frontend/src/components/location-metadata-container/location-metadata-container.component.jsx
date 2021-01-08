import React from 'react'
import './location-metadata-container.styles.scss'
import { AiOutlineInfoCircle } from "react-icons/ai";
import {Link} from 'react-router-dom'

const LocationMetadataContainer = ({country, city, street, opensAt, closesAt, phoneNumber}) => (
    <div className = 'location-metadata-container'>
        <p className = 'location-grid-content'>{country}</p>
        <p className = 'location-grid-content'>{city}</p>
        <p className = 'location-grid-content'>{street}</p>
        <p className = 'location-grid-content'>{opensAt}</p>
        <p className = 'location-grid-content'>{closesAt}</p>
        <p className = 'location-grid-content'>{phoneNumber}</p>
    </div>
)

export default LocationMetadataContainer
