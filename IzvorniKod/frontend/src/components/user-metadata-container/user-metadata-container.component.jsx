import React from 'react'
import './user-metadata-container.styles.scss'
import { AiOutlineUserAdd } from 'react-icons/ai'
import { Link } from 'react-router-dom'

const UserMetaDataContainer = ({ name, surname, email, username, role }) => (
    <div className='user-metadata-container'>
        <p className='user-grid-content'>{name}</p>
        <p className='user-grid-content'>{surname}</p>
        <p className='user-grid-content'>{email}</p>
        <p className='user-grid-content'>{username}</p>
        {role === 'OWNER' ?
            <p className='user-grid-content'><Link to={`/addOwner/${username}`}><AiOutlineUserAdd /></Link></p>
            :
            <p className='user-grid-content'>{role}</p>

        }
    </div>
)

export default UserMetaDataContainer
