import React from 'react'
import './gym-metadata-container.styles.scss'

const GymMetaDataContainer = ({name, city}) => (
    <div className = 'gym-metadata-container'>
        <p>{name}</p>
        <p>{city}</p>
        {/*<p>Prostor za ostale podatke</p>*/}
    </div>
)

export default GymMetaDataContainer