import React from 'react'
import './coachPlanForCoachPage.styles.css'
import CustomButton from "../custom-buttom/custom-button.component";

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

    buyPlan = () => {
        const trening = this.props.isTraining ? "treninga" : "prehrane"
        if(window.confirm("Želite li kupiti plan " + trening + " od " + this.props.coachUsername + "?") === true){
            console.log("Kupljeno")
        } else{
            console.log("Nije kupljeno")
        }
    }

    render() {
        return (
            <div className='userPlan-container'>
                <div className='plan-metadata' onClick={this.handleExtendLinkClick}>
                    <p>{this.props.isTraining ? "Trening" : "Prehrana"}</p>
                    <p>Trener: {this.props.coachUsername}</p>
                    <p>{!this.state.extended ? "Proširi" : "Smanji"}</p>
                </div>
                {this.state.extended &&
                <div className='userplans-details-container'>
                    <hr/>
                    <textarea disabled>{this.props.description}</textarea>
                    <div className='user-plan-details'>
                        <p>Datum početka: {this.props.dateFrom}</p>
                        <p>Datum kraja: {this.props.dateTo}</p>
                        <CustomButton onClick={this.buyPlan}>Kupi</CustomButton>
                    </div>
                </div>
                }
            </div>
        )
    }
}

export default CoachPlanForCoachPage