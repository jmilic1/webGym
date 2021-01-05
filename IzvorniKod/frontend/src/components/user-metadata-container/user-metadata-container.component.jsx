import React from 'react'
import './user-metadata-container.styles.scss'

const UserMetaDataContainer = ({name, surname, email, username, role}) => (
    <div className = 'user-metadata-container'>
        <p className = 'user-grid-content'>{name}</p>
        <p className='user-grid-content'>{surname}</p>
        <p className='user-grid-content'>{email}</p>
        <p className='user-grid-content'>{username}</p>
        <p className = 'user-grid-content'>{role}</p>
    </div>
)

export default UserMetaDataContainer
