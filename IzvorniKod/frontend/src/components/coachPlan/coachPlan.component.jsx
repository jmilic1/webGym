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
            modifyPlan: false
        }
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
        fetch(this.props.backendURL + "modifyCoachPlan" , {
            method: 'POST',
            credentials: 'include',
            body: JSON.stringify({id: this.state.id, description: this.state.description})
        })

        this.setState({
            modifyPlan: false
        })
    }

    render() {
        return(
            <div className='grid-container'>
            
                <div className='item2'>
                    <p>From: {this.state.dateFrom}</p>
                </div>

                <div className="item3">
                    <p>To: {this.state.dateTo}</p>
                </div>   

                <div className="itempom">
                    <p></p>
                </div>

                <div className="item4">
                    <p>Price: {this.state.price} kn</p>
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
                                    <button class="btn" onClick={this.handleChangePlanClick}>Otkaži</button>
                                    <button onClick = {this.handleChangePlanSubmit}> Spremi</button>
                                </p>
                            </div>
                }
                
                {!this.state.modifyPlan ?
                            <div className="item1">
                                 <p>Description: <br></br> {this.state.description}</p>
                            </div>
                            :
                        	<div className="item1">
                                <p>Description: <br></br>
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