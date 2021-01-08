import React from "react";
import './gymDetails.styles.scss';

class GymDetailsComponent extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            modifyGymInfo: false
        };
    }

    render() {
        return (
            <div>
                <h3 id='header'>Općenito</h3>
                <div className='gymDetails-container'>
                    <div className='user-info-form'>
                        <div className='userInfo-formInput'>
                            <label htmlFor='name'>Ime teretane</label>
                            <input type='text' value={this.props.name} name='name' disabled={!this.state.modifyGymInfo}/>
                        </div>
                        <div className='userInfo-formInput'>
                            <label htmlFor='email'>E-mail</label>
                            <input type='text' value={this.props.email} name='email' disabled={!this.state.modifyGymInfo}/>
                        </div>
                        <div className='userInfo-formInput'>
                            <label htmlFor='description'>Opis</label>
                            <input type='text' value={this.props.description} name='description' disabled={!this.state.modifyGymInfo}/>
                        </div>
                    </div>
                </div>

            </div>
        )
    }
}

export default GymDetailsComponent;