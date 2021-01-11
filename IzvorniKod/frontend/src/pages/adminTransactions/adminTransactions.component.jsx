import React from 'react'
import './adminTransactions.styles.css'
import AdminTransaction from "../../components/adminTransaction/adminTransaction.component";

class AdminTransactions extends React.Component{
    constructor(){
        super()
        this.state = {
            transactions: []
        }
    }

    componentDidMount(){
        fetch(this.props.backendURL + 'myTransactions', {
            method: 'GET',
            credentials: "include"
        }).then(response => {
            if(response.status === 200) return response.json()
            else return Promise.reject()
        }).then(transactions => {
            this.setState({
                transactions: transactions
            })
        }, function(){
            alert("Došlo je do pogreške")
        })
    }

    render() {
        return(

            <div className = 'userList-container'>
                <div className = 'userList-header-container'>
                    <h3>Datum</h3>
                    <h3>Pošiljatelj</h3>
                    <h3>Primatelj</h3>
                    <h3>Iznos</h3>
                    <h3>Kategorija</h3>
                </div>
                {this.state.transactions.map(t =>
                    <AdminTransaction sender={t.senderUsername} receiver={t.receiverUsername} date={t.dateWhen} type={t.transactionType} amount={t.amount}/>
                )}
            </div>
        )
    }
}

export default AdminTransactions
