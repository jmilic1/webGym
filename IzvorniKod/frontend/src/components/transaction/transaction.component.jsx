import React from 'react'
import './transaction.styles.css'

const Transaction = ({ amount, dateWhen, senderUsername, receiverUsername, transactionType }) => {
    const datum = new Date(dateWhen)
    return (
        <div className='transaction-container'>
            <p>Iznos: {amount}</p>
            <p>Datum: {datum.getDate()}.{datum.getMonth() + 1}.{datum.getFullYear()}.</p>
            <p>Pošiljatelj: {senderUsername}</p>
            <p>Primatelj: {receiverUsername}</p>
            <p>Tip transakcije: {transactionType}</p>
        </div>
    )
}
export default Transaction