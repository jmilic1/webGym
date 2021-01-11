import React from 'react'
import './userInfoForCoach.styles.css'

const UserInfoForCoach = ({name, surname, email, username}) => (
    <div className = 'user-metadata-container-forcoach'>
        <p className = 'user-grid-content-forcoach'>{name}</p>
        <p className='user-grid-content-forcoach'>{surname}</p>
        <p className='user-grid-content-forcoach'>{email}</p>
        <p className='user-grid-content-forcoach'>{username}</p>
    </div>
)

export default UserInfoForCoach
