import React from 'react'
import './gym-metadata-container.styles.scss'

const GymMetaDataContainer = ({name}) => (
    <div className = 'gym-metadata-container'>
        <p>{name}</p>
        <p>Prostor za ostale podatke</p>
    </div>
)

export default GymMetaDataContainer