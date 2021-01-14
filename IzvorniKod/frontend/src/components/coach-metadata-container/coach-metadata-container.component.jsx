import React from 'react'
import './coach-metadata-container.styles.scss'
import { Link } from 'react-router-dom'

const CoachMetadataContainer = ({username, name, surname}) => (
    <Link to={{ pathname: `/coachPage/${username}`}}>
        <div className = 'coach-metadata-container'>
            <p className = 'coach-grid-content'>{username}</p>
            <p className = 'coach-grid-content'>{name}</p>
            <p className = 'coach-grid-content'>{surname}</p>
        </div>
    </Link>
)

export default CoachMetadataContainer
