import React from 'react'
import './coach-metadata-container.styles.scss'

const CoachMetadataContainer = ({username, name, surname}) => (
    <div className = 'coach-metadata-container'>
        <p className = 'coach-grid-content'>{username}</p>
        <p className = 'coach-grid-content'>{name}</p>
        <p className = 'coach-grid-content'>{surname}</p>
    </div>
)

export default CoachMetadataContainer
