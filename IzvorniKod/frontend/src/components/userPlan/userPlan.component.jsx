import React from 'react'
import './userPlan.styles.css'

class UserPlan extends React.Component {
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
                <div className='plan-metadata'>
                    <p>Tip: {this.props.isTraining ? "Trening" : "Prehrana"}</p>
                    <p>Opis: {this.props.description}</p>
                    <p className = 'cursor-on-hover' onClick={this.handleExtendLinkClick}>{!this.state.extended ? "Proširi" : "Smanji"}</p>
                </div>
                {this.state.extended &&
                <div>
                    <p>Trener: {this.props.coachUsername}</p>
                    <p>Datum kupnje: {this.props.dateOfPurchase}</p>
                    <p>Datum početka: {this.props.dateFrom}</p>
                    <p>Datum kraja: {this.props.dateTo}</p>
                </div>
                }
            </div>
        )
    }
}

export default UserPlan