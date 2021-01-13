import React from 'react'
import './transaction.styles.css'

const Transaction = ({amount, dateWhen, senderUsername, receiverUsername, transactionType}) => {
    return (
        <div className='transaction-container'>
                <p>Iznos: {amount}</p>
                <p>Datum: {dateWhen}</p>
                <p>Pošiljatelj: {senderUsername}</p>
                <p>Primatelj: {receiverUsername}</p>
                <p>Tip transakcije: {transactionType}</p>
        </div>
    )
}

export default Transaction