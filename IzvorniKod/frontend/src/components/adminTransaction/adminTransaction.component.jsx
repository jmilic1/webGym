import React from 'react'
import './adminTransaction.styles.css'

const AdminTransaction = ({date, sender, receiver, amount, type}) => (
    <div className = 'user-metadata-container'>
        <p className = 'user-grid-content'>{date}</p>
        <p className='user-grid-content'>{sender}</p>
        <p className='user-grid-content'>{receiver}</p>
        <p className='user-grid-content'>{amount}</p>
        <p className = 'user-grid-content'>{type}</p>
    </div>
)

export default AdminTransaction
