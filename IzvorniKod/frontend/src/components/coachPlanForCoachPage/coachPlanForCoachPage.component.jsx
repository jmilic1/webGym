import React from 'react'
import './coachPlanForCoachPage.styles.css'
import CustomButton from "../custom-buttom/custom-button.component";
import { Link } from 'react-router-dom'

class CoachPlanForCoachPage extends React.Component {
    constructor() {
        super();
        this.state = {
            extended: false
        }
    }

    handleExtendLinkClick = () => {
        this.setState({
            extended: !this.state.extended
        })
    }

    render() {
        return (
            <div className='userPlan-container'>
                <div className='plan-coach-metadata' onClick={this.handleExtendLinkClick}>
                    <p>{this.props.isTraining ? "Trening" : "Prehrana"}</p>
                    <p>Trener: {this.props.coachUsername}</p>
                    <p>{!this.state.extended ? "Proširi" : "Smanji"}</p>
                </div>
                {this.state.extended &&
                    <div className='userplans-details-container'>
                        <hr />
                        <textarea disabled>{this.props.description}</textarea>
                        <div className='user-plan-details'>
                            <p>Datum početka: {this.props.dateFrom}</p>
                            <p>Datum kraja: {this.props.dateTo}</p>
                            <Link to={{
                                pathname: '/paymentPage',
                                aboutProps: {
                                    id: this.props.id,
                                    isPlan: true
                                }
                            }} ><CustomButton>Kupi</CustomButton></Link>
                        </div>
                    </div>
                }
            </div>
        )
    }
}

export default CoachPlanForCoachPage