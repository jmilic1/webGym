import React from 'react'
import './paymentPage.styles.scss'
    
class PaymentPage extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            id: props.location.aboutProps.id,
            isPlan: props.location.aboutProps.isPlan,   
            showCard: false
        }
    }
    
    handlePaymentPlanSubmit = () => {
        fetch(this.props.backendURL + "buyPlan" , {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            credentials: 'include',
            body: JSON.stringify({id: this.state.id})
        }).then(response => {
            if (response.ok) {
                alert("Plaćanje uspješno!")
            } else {
                throw new Error("HTTP Error! " + response.status)
            }
        }).catch(e => {
            alert("Došlo je do pogreške: " + e.message)
        })
    }

    handlePaymentMembershipSubmit = () => {
        fetch(this.props.backendURL + "buyMembership" , {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            credentials: 'include',
            body: JSON.stringify({id: this.state.id})
        }).then(response => {
            if (response.ok) {
                alert("Plaćanje uspješno!")
            } else {
                throw new Error("HTTP Error! " + response.status)
            }
        }).catch(e => {
            alert("Došlo je do pogreške: " + e.message)
        })
    }

    setshowCard = () => {
        this.setState({
            showCard: true
        })
    }

    removeshowCard = () => {
        this.setState({
            showCard: false
        })
    }
    
    render() {
        return (
            <div className='payment-page-container'>
                
                <div className='payment-header-container'>
                        {this.state.isPlan ?
                            <p className='font'>
                                Odaberite način plaćanja za odabrani plan
                            </p>
                            :
                            <p className='font'>
                                Odaberite način plaćanja za odabranu članarina
                            </p>
                        }   
                </div>
                
                <div className='payment-plansHeaderBtn'>
                    <div className='payment-plansHeader'>
                        <div className={`${this.state.showCard ? "marked" : ''}`} onClick={this.setshowCard}><p>Kartica</p></div>
                        <div style={{textAlign: "right"}} className={`${!this.state.showCard ? "marked" : ''}`} onClick={this.removeshowCard}><p>Paypal</p></div>
                    </div>
                </div>

                {this.state.showCard ?
                    <div className='grid-container'>
                        <div className="item1">
                            <label>Ime: </label>
                            <input type="text" />
                        </div>

                        <div className="item2"> 
                            <label>Prezime: </label>
                            <input type="text" />
                        </div>

                        <div className="item3">
                            <label>Email: </label>
                            <input type="text" />
                        </div>

                        <div className="item4">
                            <label>Ulica i kućni broj: </label>
                            <input type="text" />
                        </div>

                        <div className="item5">
                            <label>Grad: </label>
                            <input type="text" />
                        </div>

                        <div className="item6">
                            <label>Poštanski broj: </label>
                            <input type="text" />
                        </div>

                        <div className="item7">
                            <label>Zemlja: </label>
                            <input type="text" />
                        </div>

                        <div className="item8">
                            <label>Broj kartice: </label>
                            <input type="text" placeholder="XXXX XXXX XXXX XXXX" required minlength="16" maxlength="16"/>
                        </div>

                        <div className="item9">
                            <label>Datum isteka kartice: </label>
                            <input type="text" name="month" placeholder="MM" maxlength="2" size="2" />
                            <span>/</span>
                            <input type="text" name="year" placeholder="YY" maxlength="2" size="2" />
                        </div>

                        <div className="item10">
                            <label>CVV kod: </label>
                            <input type="text" placeholder="XXX" required minlength="3" maxlength="3"/>
                        </div>

                        <div className="item11">
                            <button onClick={this.state.isPlan ? this.handlePaymentPlanSubmit : this.handlePaymentMembershipSubmit}>Plati</button>
                        </div>
                    </div>    
                    :
                    <div className='grid-container'>
                        <div className="item1">
                            <label>Email: </label>
                            <input type="text"/>
                        </div>
                        <div className="item2">
                            <label>Šifra: </label>
                            <input type="password" required/>
                        </div>
                        <div className="item3">
                            <p/>
                        </div>
                        <div className="item6">
                            <button onClick={this.state.isPlan ? this.handlePaymentPlanSubmit : this.handlePaymentMembershipSubmit}>Ulogiraj se i plati</button>
                        </div>
                    </div> 
                }
            </div>
        )
    }
}

export default PaymentPage