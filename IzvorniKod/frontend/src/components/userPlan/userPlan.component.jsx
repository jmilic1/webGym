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
                <div className='plan-metadata' onClick={this.handleExtendLinkClick}>
                    <p>{this.props.isTraining ? "Trening" : "Prehrana"}</p>
                    <p>Cijena: {this.props.price}</p>
                    <p>Trener: {this.props.coachUsername}</p>
                    <p>{this.state.extended ? 'Smanji' : 'Proširi'}</p>
                </div>
                {this.state.extended &&
                    <div className='userplans-details-container'>
                        <hr />
                        <textarea disabled>{this.props.description}</textarea>
                        <div className='user-plan-details'>
                            <p>Datum kupnje: {this.props.dateOfPurchase}</p>
                            <p>Datum početka: {this.props.dateFrom}</p>
                            <p>Datum kraja: {this.props.dateTo}</p>
                        </div>
                    </div>
                }
            </div>
        )
    }
}

export default UserPlan