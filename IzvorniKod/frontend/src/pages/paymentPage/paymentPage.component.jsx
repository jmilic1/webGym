import React from 'react'
import './paymentPage.styles.scss'
    
class PaymentPage extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            id: props.location.aboutProps.id,
            isPlan: props.location.aboutProps.isPlan,   
            showCard: false,
            payPalEmail:"",
            payPalPassword: "",
            cardName:"",
            cardSurname: "",
            cardEmail: "",
            cardStreet: "",
            cardCity: "",
            cardPostalCode: "",
            cardCountry: "",
            cardNumber: "",
            cardExpireDateDay: "",
            cardExpireDateMonth: "",
            cardCCV: ""
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

    handlePayWithPayPal = () => {
        if(this.state.payPalEmail !== "" && this.state.payPalPassword !== ""){
            this.state.isPlan ? this.handlePaymentPlanSubmit() : this.handlePaymentMembershipSubmit()
        } else {
            alert("Morate unijeti email i lozinku PayPal računa")
        }
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

    handleChange = event => {
        const { value, name } = event.target;

        this.setState({ [name]: value });
    };

    CheckCardDetails = () => {
        if(this.state.cardName === ""){
            alert("Polje za unos imena ne smije ostati prazno")
            return false
        } else if(this.state.cardSurname === ""){
            alert("Polje za unos prezimena ne smije ostati prazno")
            return false
        }else if(this.state.cardStreet === ""){
            alert("Polje za unos ulice ne smije ostati prazno")
            return false
        }else if(this.state.cardCity === ""){
            alert("Polje za unos grada ne smije ostati prazno")
            return false
        }else if(this.state.cardPostalCode === ""){
            alert("Polje za unos poštanskog broja ne smije ostati prazno")
            return false
        }else if(this.state.cardCountry === ""){
            alert("Polje za unos države ne smije ostati prazno")
            return false
        }else if(this.state.cardNumber === ""){
            alert("Polje za unos broja kartice ne smije ostati prazno")
            return false
        }else if(this.state.cardExpireDateDay === ""){
            alert("Polje za unos datuma isteka kartice ne smije ostati prazno")
            return false
        }else if(this.state.cardExpireDateMonth === ""){
            alert("Polje za unos datuma isteka kartice ne smije ostati prazno")
            return false
        }else if(this.state.cardCCV === ""){
            alert("Polje za unos CCV-a kartice ne smije ostati prazno")
            return false
        }
        return true
    }

    payWithCard = () => {
        if(this.CheckCardDetails()){
            this.state.isPlan ? this.handlePaymentPlanSubmit() : this.handlePaymentMembershipSubmit()
        }
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
                    <div className='grid-container-payment'>
                        <div className='card-detail'>
                            <label>Ime: </label>
                            <input type="text" onChange={this.handleChange} name="cardName"/>
                        </div>

                        <div className='card-detail'>
                            <label>Prezime: </label>
                            <input type="text" onChange={this.handleChange} name="cardSurname"/>
                        </div>

                        <div className='card-detail'>
                            <label>Email: </label>
                            <input type="text" onChange={this.handleChange} name="cardEmail"/>
                        </div>

                        <div className='card-detail'>
                            <label>Ulica i kućni broj: </label>
                            <input type="text" onChange={this.handleChange} name="cardStreet"/>
                        </div>

                        <div className='card-detail'>
                            <label>Grad: </label>
                            <input type="text" onChange={this.handleChange} name="cardCity"/>
                        </div>

                        <div className='card-detail'>
                            <label>Poštanski broj: </label>
                            <input type="text" onChange={this.handleChange} name="cardPostalCode"/>
                        </div>

                        <div className='card-detail'>
                            <label>Zemlja: </label>
                            <input type="text" onChange={this.handleChange} name="cardCountry"/>
                        </div>

                        <div className='card-detail'>
                            <label>Broj kartice: </label>
                            <input type="text" placeholder="XXXX XXXX XXXX XXXX" required minlength="16" maxlength="16" name="cardNumber" onChange={this.handleChange}/>
                        </div>

                        <div className='item9'>
                            <label>Datum isteka kartice: </label>
                            <input type="text" name="month" placeholder="MM" maxlength="2" size="2" onChange={this.handleChange} name="cardExpireDateDay"/>
                            <span>/</span>
                            <input type="text" name="year" placeholder="YY" maxlength="2" size="2" onChange={this.handleChange} name="cardExpireDateMonth"/>
                        </div>

                        <div className='card-detail'>
                            <label>CVV kod: </label>
                            <input type="text" placeholder="XXX" required minlength="3" maxlength="3" onChange={this.handleChange} name="cardCCV"/>
                        </div>

                        <div>
                            <button onClick={this.payWithCard}>Plati</button>
                        </div>
                    </div>    
                    :
                    <div className='grid-container-payment'>
                        <div className='card-detail'>
                            <label>Email: </label>
                            <input type='text' name="payPalEmail" onChange={this.handleChange} value={this.state.payPalEmail}/>
                        </div>
                        <div className='card-detail'>
                            <label>Šifra: </label>
                            <input type='password' name ="payPalPassword" onChange={this.handleChange} value={this.state.payPalPassword} required/>
                        </div>
                        <div>
                            <button onClick={this.handlePayWithPayPal}>Ulogiraj se i plati</button>
                        </div>
                    </div> 
                }
            </div>
        )
    }
}

export default PaymentPage