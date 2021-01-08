import React from 'react'
import './coachPlan.styles.css'

class CoachPlan extends React.Component{
    constructor(plan) {
        super();
        this.state = {
            id: plan.id,
            description: plan.description,
            dateFrom: plan.dateFrom,
            dateTo: plan.dateTo,
            price: plan.price,
            backendUrl: plan.backendURL,
            modifyPlan: false
        }
        console.log(plan.backendUrl)
    }

    handleChange = event => {
        const { value, name } = event.target;

        this.setState({ [name]: value });
    };

    handleChangePlanClick = () => {
        this.setState({
            modifyPlan: !this.state.modifyPlan
        })
    }

    handleChangePlanSubmit = () => {
        fetch(this.props.backendUrl + "modifyCoachPlan" , {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            credentials: 'include',
            body: JSON.stringify({id: this.state.id, description: this.state.description})
        }).then(response => {
            if (response.ok) {
                this.setState({
                    modifyPlan: false
                })
            } else {
                throw new Error("HTTP Error! " + response.status)
            }
        }).catch(e => {
            alert("Došlo je do pogreške: " + e.message)
        })
    }

    render() {
        return(
            <div className='grid-container'>
            
                <div className='item2'>
                    <p>Od: {this.state.dateFrom}</p>
                </div>

                <div className="item3">
                    <p>Do: {this.state.dateTo}</p>
                </div>

                <div className="item4">
                    <p>Cijena: {this.state.price} kn</p>
                </div>

                {!this.state.modifyPlan ?
                            <div className="item5">
                                 <p>
                                    <button onClick={this.handleChangePlanClick}>Uredi</button>
                                </p>
                            </div>
                            :
                        	<div className="item5">
                                <p>
                                    <button className="btn" onClick={this.handleChangePlanClick}>Otkaži</button>
                                    <button onClick = {this.handleChangePlanSubmit}> Spremi</button>
                                </p>
                            </div>
                }
                
                {!this.state.modifyPlan ?
                            <div className="item1">
                                 <p>Opis: <br/> {this.state.description}</p>
                            </div>
                            :
                        	<div className="item1">
                                <p>Opis: <br/>
                                    <div className="plan-description-update">
                                        <textarea className="textarea" name='description' value={this.state.description} onChange={this.handleChange} required/>
                                    </div>                 
                                </p>
                            </div>
                }

            </div>
        )
    }   
}

export default CoachPlan