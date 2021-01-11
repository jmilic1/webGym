import React from "react";
import './gymDetails.styles.scss';
import CustomButton from "../../components/custom-buttom/custom-button.component";

class GymDetailsComponent extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            name: this.props.name,
            email: this.props.email,
            description: this.props.description,
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
                            <input type='text' value={this.state.name} name='name' disabled={!this.state.modifyGymInfo}/>
                        </div>
                        <div className='userInfo-formInput'>
                            <label htmlFor='email'>E-mail</label>
                            <input type='text' value={this.state.email} name='email' disabled={!this.state.modifyGymInfo}/>
                        </div>
                        <div className='userInfo-formInput'>
                            <label htmlFor='description'>Opis</label>
                            <input type='text' value={this.state.description} name='description' disabled={!this.state.modifyGymInfo}/>
                        </div>

                    </div>
                </div>

            </div>
        )
    }
}

export default GymDetailsComponent;